/* Copyright (C) 2012 Buddy Platform, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.buddy.sdk;

/**
 * Represents the sum of a collection of metadata items with a similar key.
 * <p>
 * 
 * <pre>
 * {@code BuddyClient client = new BuddyClient("APPNAME", "APPPASS");}
 * {@code client.getMetadata().set("Test count1", "10", 0, 0, null)}
 * {@code client.getMetadata().set("Test count2", "20", 0, 0, null)}
 * {@code client.getMetadata().set("Test count3", "30", 0, 0, null)}
 * {@code client.getMetadata().set("Test count4", "40", 0, 0, null)}
 * {@code client.getMetadata().sum("Test count", 100, 0, 0, 10, new OnCallback<Response<MetadataSum>>()}
 * <code>{</code>
 *     {@code public void OnResponse(Response<MetadataSum> response, Object state)}
 *     <code>{</code>
 *         {@code MetadataSum sum = response.getResult();}
 *     <code>}</code>
 * <code>});</code>
 * </pre>
 */
public class MetadataSum {
    private double total;

    private int keysCounted;

    private String key;

    MetadataSum(double sum, int counted, String key) {
        this.total = sum;
        this.keysCounted = counted;
        this.key = key;
    }

    /**
     * Gets the total sum of the metadata items.
     */
    public double getTotal() {
        return total;
    }

    /**
     * Gets the number of items that were summed.
     */
    public int getKeysCounted() {
        return keysCounted;
    }

    /**
     * Gets the common key that was used to produce this sum.
     */
    public String getKey() {
        return key;
    }
}
