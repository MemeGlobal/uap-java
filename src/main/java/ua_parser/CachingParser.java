package ua_parser;

import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * When doing webanalytics (with for example PIG) the main pattern is to process
 * weblogs in clickstreams. A basic fact about common clickstreams is that in
 * general the same browser will do multiple requests in sequence. This has the
 * effect that the same useragent will appear in the logfiles and we will see
 * the need to parse the same useragent over and over again.
 * <p>
 * This class introduces a very simple LRU cache to reduce the number of times
 * the parsing is actually done.
 *
 * @author Niels Basjes
 */
public class CachingParser extends Parser {
    private static final long DEFAULT_EXPIRE_AFTER_ACCESS = 600000;
    private static final long DEFAULT_CACHE_SIZE = 1000;

    private final LoadingCache<String, Client>      cacheClient;
    private final LoadingCache<String, UserAgent>   cacheUserAgent;
    private final LoadingCache<String, Device>      cacheDevice;
    private final LoadingCache<String, OS>          cacheOS;

    private final long expireAfterAccessMS;
    private final long cacheSize;

    public CachingParser() {
        this(DEFAULT_CACHE_SIZE, DEFAULT_EXPIRE_AFTER_ACCESS);
    }

    public CachingParser(long cacheSize, long expireAfterAccess) {
        this(CachingParser.class.getResourceAsStream(REGEX_YAML_PATH), cacheSize, expireAfterAccess);
    }

    public CachingParser(InputStream regexYaml) {
        this(regexYaml, DEFAULT_CACHE_SIZE, DEFAULT_EXPIRE_AFTER_ACCESS);
    }

    public CachingParser(InputStream regexYaml, long cacheSize, long expireAfterAccessMS) {
        super(regexYaml);

        this.cacheSize = cacheSize;
        this.expireAfterAccessMS = expireAfterAccessMS;

        this.cacheClient = createCache(super::parse);
        this.cacheUserAgent = createCache(super::parseUserAgent);
        this.cacheDevice = createCache(super::parseDevice);
        this.cacheOS = createCache(super::parseOS);
    }

    @Override
    public Client parse(String agentString) {
        if (Strings.isNullOrEmpty(agentString))
            return null;

        return cacheClient.getUnchecked(agentString);
    }

    @Override
    public UserAgent parseUserAgent(String agentString) {
        if (Strings.isNullOrEmpty(agentString))
            return null;

        return cacheUserAgent.getUnchecked(agentString);
    }

    @Override
    public Device parseDevice(String agentString) {
        if (Strings.isNullOrEmpty(agentString))
            return null;

        return cacheDevice.getUnchecked(agentString);
    }

    @Override
    public OS parseOS(String agentString) {
        if (Strings.isNullOrEmpty(agentString))
            return null;

        return cacheOS.getUnchecked(agentString);
    }

    public LoadingCache<String, Client> getCacheClient() {
        return cacheClient;
    }

    public LoadingCache<String, UserAgent> getCacheUserAgent() {
        return cacheUserAgent;
    }

    public LoadingCache<String, Device> getCacheDevice() {
        return cacheDevice;
    }

    public LoadingCache<String, OS> getCacheOS() {
        return cacheOS;
    }

    private <T> LoadingCache<String, T> createCache(Function<String, T> loader) {
        return CacheBuilder.newBuilder()
                .maximumSize(cacheSize)
                .expireAfterAccess(expireAfterAccessMS, TimeUnit.MILLISECONDS)
                .recordStats()
                .build(new CacheLoader<String, T>() {
                    @Override
                    public T load(String key) throws Exception {
                        return loader.apply(key);
                    }
                });
    }
}
