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
 * Loads entries on demand, with control over each value's expiry duration (i.e. variable expiration).
 *
 * @param <K> Key type
 * @param <V> Value type
 */
public interface ExpiringEntryLoader<K, V> {
  /**
   * Called to load a new value for the {@code key} into an expiring map.
   *
   * @param key to load a value for
   * @return contains new value to load along with its expiry duration
   */
  ExpiringValue<V> load(K key);
}
