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
 * Operating System parsed data class
 *
 * @author Steve Jiang (@sjiang) <gh at iamsteve com>
 */
public class OS {
    public final String family, major, minor, patch, patchMinor;

    public OS(String family, String major, String minor, String patch, String patchMinor) {
        this.family = family;
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.patchMinor = patchMinor;
    }

    @VisibleForTesting
    static OS fromMap(Map<String, String> m) {
        if (m.containsKey("version")) {
            if (m.get("version").isEmpty())
                return new OS(m.get("family"), null, null, null, null);

            String[] versions = Arrays.copyOf(m.get("version").split("\\."), 4);
            return new OS(m.get("family"), versions[0], versions[1], versions[2], versions[3]);
        } else
            return new OS(m.get("family"), m.get("major"), m.get("minor"), m.get("patch"), m.get("patch_minor"));
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        OS os = (OS) object;

        if (family != null ? !family.equalsIgnoreCase(os.family) : os.family != null) return false;
        if (major != null ? !major.equals(os.major) : os.major != null) return false;
        if (minor != null ? !minor.equals(os.minor) : os.minor != null) return false;
        if (patch != null ? !patch.equals(os.patch) : os.patch != null) return false;
        return patchMinor != null ? patchMinor.equals(os.patchMinor) : os.patchMinor == null;
    }

    @Override
    public int hashCode() {
        int result = family != null ? family.hashCode() : 0;
        result = 31 * result + (major != null ? major.hashCode() : 0);
        result = 31 * result + (minor != null ? minor.hashCode() : 0);
        result = 31 * result + (patch != null ? patch.hashCode() : 0);
        result = 31 * result + (patchMinor != null ? patchMinor.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("{\"family\": %s, \"major\": %s, \"minor\": %s, \"patch\": %s, \"patch_minor\": %s}",
                family == null ? Constants.EMPTY_STRING : '"' + family + '"',
                major == null ? Constants.EMPTY_STRING : '"' + major + '"',
                minor == null ? Constants.EMPTY_STRING : '"' + minor + '"',
                patch == null ? Constants.EMPTY_STRING : '"' + patch + '"',
                patchMinor == null ? Constants.EMPTY_STRING : '"' + patchMinor + '"');
    }
}