/*
 * Flight
 * Copyright 2023 Kiran Hart
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ca.tweetzy.flight.collection.expiringmap;

/**
 * A listener for expired object events.
 *
 * @param <K> Key type
 * @param <V> Value type
 */
public interface ExpirationListener<K, V> {
    /**
     * Called when a map entry expires.
     *
     * @param key   Expired key
     * @param value Expired value
     */
    void expired(K key, V value);
}
