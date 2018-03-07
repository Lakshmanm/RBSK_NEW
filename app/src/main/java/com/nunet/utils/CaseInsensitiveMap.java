package com.nunet.utils;

import java.util.HashMap;

public class CaseInsensitiveMap<K, V> extends HashMap<K, V> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public V put(K key, V value) {
		return super.put((K) key.toString().toLowerCase(), value);
	}

	// not @Override because that would require the key parameter to be of type
	// Object
	public V get(String key) {
		return super.get(key.toLowerCase());
	}
}