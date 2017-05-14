
package com.timweng.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * TagMap is a Key-Value map allows user set multiple tag into each key.
 * <p>
 * User can quickly find values associates tag by using {@link #getByTag}
 * <p>
 * Created by Tim Weng on 14/05/2017.
 */

public class TagMap<K, T, V> {
    final private static String TAG = "TagMap";

    private Map<K, V> mKey2ValueMap;
    private Map<T, Map<V, Integer>> mTag2ValuesMap;
    private Map<K, Set<T>> mKey2TagsMap;

    public TagMap() {
        mKey2ValueMap = new HashMap<>();
        mTag2ValuesMap = new HashMap<>();
        mKey2TagsMap = new HashMap<>();
    }

    /**
     * Removes all of the mappings from this map (optional operation). The map will be empty after
     * this call returns.
     */
    public void clear() {
        mKey2ValueMap.clear();
        mTag2ValuesMap.clear();
        mKey2TagsMap.clear();
    }

    /**
     * Returns the value to which the specified key is mapped, or {@code null} if this map contains
     * no mapping for the key.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or {@code null} if this map contains
     *         no mapping for the key
     */
    public V getByKey(K key) {
        return mKey2ValueMap.get(key);
    }

    /**
     * Returns the value {@link Set} to which contains this tag, or {@code null} if this map
     * contains no mapping for the tag.
     *
     * @param tag the tag whose associated value is to be returned
     * @return the value {@link Set} to which contains this tag, or {@code null} if this map
     *         contains no mapping for the tag.
     */
    public Set<V> getByTag(T tag) {
        Map<V, Integer> valueMap = mTag2ValuesMap.get(tag);
        if (valueMap != null) {
            return mTag2ValuesMap.get(tag).keySet();
        } else {
            return null;
        }
    }

    /**
     * Returns {@code true} if this map contains a mapping for the specified key.
     *
     * @param key key whose presence in this map is to be tested
     * @return {@code true} if this map contains a mapping for the specified key.
     */
    public boolean containsKey(K key) {
        return mKey2ValueMap.containsKey(key);
    }

    /**
     * Returns {@code true} if this map maps one or more tags to the specified key
     *
     * @param tag tag whose presence in this map is to be tested
     * @return {@code true} if this map maps one or more tags to the specified key
     */
    public boolean containsTag(T tag) {
        return mTag2ValuesMap.containsKey(tag);
    }

    /**
     * Returns {@code true} if this map maps one or more keys to the specified value.
     *
     * @param value value whose presence in this map is to be tested
     * @return {@code true} if this map maps one or more keys to the specified value.
     */
    public boolean containsValue(V value) {
        return mKey2ValueMap.containsValue(value);
    }

    /**
     * Returns the number of key-value mappings in this map.
     *
     * @return the number of key-value mappings in this map
     */
    public int size() {
        return mKey2ValueMap.size();
    }

    /**
     * Returns {@code true} if this map contains no key-value mappings.
     *
     * @return {@code true} if this map contains no key-value mappings
     */
    public boolean isEmpty() {
        return mKey2ValueMap.isEmpty();
    }

    /**
     * Returns a {@link Set} view of the keys contained in this map.
     *
     * @return a {@link Set} view of the keys contained in this map
     */
    public Set<K> keySet() {
        return mKey2ValueMap.keySet();
    }

    /**
     * Returns a {@link Set} view of the tags contained in this map.
     *
     * @return a {@link Set} view of the tags contained in this map
     */
    public Set<T> tagSet() {
        return mTag2ValuesMap.keySet();
    }

    /**
     * Returns a {@link Collection} view of the values contained in this map.
     *
     * @return a {@link Collection} view of the values contained in this map.
     */
    public Collection<V> values() {
        return mKey2ValueMap.values();
    }

    /**
     * Associates the specified key with the specified value and multiple tags in this map (optional
     * operation). If the map previously contained a mapping for the key, the old value is replaced
     * by specified value and the specified tag {@link Set}.
     *
     * @param key key with which the specified value is to be associated
     * @param tagSet tag {@link Set} to be associated with the specified key
     * @param value value to be associated with the specified key
     * @return the previous value associated with {@code key}, or {@code null} if there was no
     *         mapping for {@code key}.
     */
    public V put(K key, Set<T> tagSet, V value) {
        V previousValue = remove(key);
        mKey2ValueMap.put(key, value);
        if (tagSet != null && !tagSet.isEmpty()) {
            for (T tag : tagSet) {
                if (mTag2ValuesMap.containsKey(tag)) {
                    Map<V, Integer> valueMap = mTag2ValuesMap.get(tag);
                    if (valueMap.containsKey(value)) {
                        valueMap.put(value, valueMap.get(value) + 1);
                    } else {
                        valueMap.put(value, 1);
                    }
                } else {
                    Map<V, Integer> valueMap = new HashMap<>();
                    valueMap.put(value, 1);
                    mTag2ValuesMap.put(tag, valueMap);
                }
            }
        }
        mKey2TagsMap.put(key, tagSet);

        return previousValue;
    }

    /**
     * Replaces the entry for the specified key only if it is currently mapped to some value.
     *
     * @param key key with which the specified value is associated
     * @param tagSet tag {@link Set} to be associated with the specified key
     * @param value value to be associated with the specified key
     * @return the previous value associated with the specified key, or {@code null} if there was no
     *         mapping for the key.
     */
    public V replace(K key, Set<T> tagSet, V value) {
        if (containsKey(key)) {
            return put(key, tagSet, value);
        } else {
            return null;
        }
    }

    /**
     * Removes the mapping for a key from this map if it is present (optional operation).
     *
     * @param key key whose mapping is to be removed from the map
     * @return the previous value associated with {@code key}, or {@code null} if there was no
     *         mapping for {@code key}.
     */
    public V remove(K key) {
        V removedValue = null;
        if (containsKey(key)) {
            removedValue = mKey2ValueMap.remove(key);
            Set<T> removedTagSet = mKey2TagsMap.remove(key);
            if (removedTagSet != null) {
                for (T tag : removedTagSet) {
                    Map<V, Integer> valueMap = mTag2ValuesMap.get(tag);
                    int count = valueMap.get(removedValue);
                    if (count > 1) {
                        valueMap.put(removedValue, count - 1);
                    } else {
                        valueMap.remove(removedValue);
                    }
                    if (valueMap.isEmpty()) {
                        mTag2ValuesMap.remove(tag);
                    }
                }
            }
        }
        return removedValue;
    }
}
