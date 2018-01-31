package ua_parser;

import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import org.junit.Assert;
import org.junit.Test;

/**
 * These tests really only redo the same tests as in ParserTest but with a
 * different Parser subclass Also the same tests will be run several times on
 * the same user agents to validate the caching works correctly.
 *
 * @author niels
 */
public class CachingParserTest {
    private final CachingParser onTest = new CachingParser();

    @Test
    public void testCachedParseUserAgent() {
        String ua = "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:58.0) Gecko/20100101 Firefox/58.0";
        onTest.parseUserAgent(ua);
        onTest.parseUserAgent(ua);
        onTest.parseUserAgent(ua);

        assertCacheWorks(onTest.getCacheUserAgent(), 1, 2);
    }

    @Test
    public void testCachedParseOS() {
        String ua = "Mozilla/5.0 (Linux; Android 6.0; LEX626 Build/HBXCNCU5801811241S) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.98 Mobile Safari/537.36";
        onTest.parseOS(ua);
        onTest.parseOS(ua);
        onTest.parseOS(ua);

        assertCacheWorks(onTest.getCacheOS(), 1, 2);
    }

    @Test
    public void testCachedParseDevice() {
        String ua = "Mozilla/5.0 (Linux; Android 7.0; PRIME X MAX 2018 Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.111 Mobile Safari/537.36";
        onTest.parseDevice(ua);
        onTest.parseDevice(ua);
        onTest.parseDevice(ua);

        assertCacheWorks(onTest.getCacheDevice(), 1, 2);
    }

    @Test
    public void testCachedParse() {
        String ua = "Mozilla/5.0 (Linux; Android 7.0; PRIME X MAX 2018 Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.111 Mobile Safari/537.36";
        onTest.parse(ua);
        onTest.parse(ua);
        onTest.parse(ua);

        assertCacheWorks(onTest.getCacheClient(), 1, 2);
        assertCacheWorks(onTest.getCacheUserAgent(), 1, 0);
        assertCacheWorks(onTest.getCacheDevice(), 1, 0);
        assertCacheWorks(onTest.getCacheOS(), 1, 0);
    }

    private void assertCacheWorks(LoadingCache<String, ?> cache, long loadCount, long hitCount) {
        CacheStats stats = cache.stats();
        Assert.assertEquals(loadCount, stats.loadCount());
        Assert.assertEquals(hitCount, stats.hitCount());
    }
}
