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

import java.util.Map;

/**
 * Device parsed data class
 *
 * @author Steve Jiang (@sjiang) <gh at iamsteve com>
 */
public class Device {
    public final String family;

    public Device(String family) {
        this.family = family;
    }

    @VisibleForTesting
    static Device fromMap(Map<String, String> m) {
        return new Device(m.get(Constants.FAMILY));
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Device device = (Device) object;

        return family != null ? family.equalsIgnoreCase(device.family) : device.family == null;
    }

    @Override
    public int hashCode() {
        return family != null ? family.hashCode() : 0;
    }

    @Override
    public String toString() {
        return String.format("{\"family\": %s}",
                family == null ? Constants.EMPTY_STRING : '"' + family + '"');
    }
}