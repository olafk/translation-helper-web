package de.olafkock.liferay.translationhelper;

import java.util.HashMap;
import java.util.HashSet;

public class TranslationHelperThreadLocal {
	
	public static void add(String key, String value) {
		tl.get().addResult(key, value);
	}
	
	public static HashMap<String,HashSet<String>> retrieve() {
		return tl.get().getResults();
	}
	
	public static void clear() {
		tl.get().clearResults();
	}

	public static void remove() {
		tl.remove();
	}
	

	
	private HashMap<String,HashSet<String>> results;
	
	private TranslationHelperThreadLocal() {
		this.results = new HashMap<String, HashSet<String>>();
	}
	
	private void addResult(String key, String value) {
		HashSet<String> values = this.results.get(key);
		if(values == null) {
			values = new HashSet<String>();
			results.put(key, values);
		}
		values.add(value);
	}
	
	private HashMap<String,HashSet<String>> getResults() {
		return this.results;
	}

	private void clearResults() {
		this.results = new HashMap<String, HashSet<String>>();
	}

	private static final ThreadLocal<TranslationHelperThreadLocal> tl = new ThreadLocal<TranslationHelperThreadLocal>() {
		@Override
		protected TranslationHelperThreadLocal initialValue() {
			return new TranslationHelperThreadLocal();
		}
	};

}
