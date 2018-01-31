/**
 * Copyright 2012 Twitter, Inc
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ua_parser;

import com.google.common.annotations.VisibleForTesting;

import java.util.Arrays;
import java.util.Map;

/**
 * User Agent parsed data class
 *
 * @author Steve Jiang (@sjiang) <gh at iamsteve com>
 */
public class UserAgent {
    public final String family, major, minor, patch;

    public UserAgent(String family, String major, String minor, String patch) {
        this.family = family;
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    @VisibleForTesting
    static UserAgent fromMap(Map<String, String> m) {
        if (m.containsKey(Constants.FULL_VERSION)) {
            String version = m.get(Constants.FULL_VERSION);

            if (version.isEmpty())
                return new UserAgent(m.get(Constants.FAMILY), null, null, null);

            String[] versions = Arrays.copyOf(version.split("\\."), 3);
            return new UserAgent(m.get(Constants.FAMILY), versions[0], versions[1], versions[2]);
        } else {
            return new UserAgent(m.get(Constants.FAMILY), m.get(Constants.MAJOR), m.get(Constants.MINOR), m.get(Constants.PATCH));
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        UserAgent userAgent = (UserAgent) object;

        if (family != null ? !family.equalsIgnoreCase(userAgent.family) : userAgent.family != null) return false;
        if (major != null ? !major.equals(userAgent.major) : userAgent.major != null) return false;
        if (minor != null ? !minor.equals(userAgent.minor) : userAgent.minor != null) return false;
        return patch != null ? patch.equals(userAgent.patch) : userAgent.patch == null;
    }

    @Override
    public int hashCode() {
        int result = family != null ? family.hashCode() : 0;
        result = 31 * result + (major != null ? major.hashCode() : 0);
        result = 31 * result + (minor != null ? minor.hashCode() : 0);
        result = 31 * result + (patch != null ? patch.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("{\"family\": %s, \"major\": %s, \"minor\": %s, \"patch\": %s}",
                family == null ? Constants.EMPTY_STRING : '"' + family + '"',
                major == null ? Constants.EMPTY_STRING : '"' + major + '"',
                minor == null ? Constants.EMPTY_STRING : '"' + minor + '"',
                patch == null ? Constants.EMPTY_STRING : '"' + patch + '"');
    }

}