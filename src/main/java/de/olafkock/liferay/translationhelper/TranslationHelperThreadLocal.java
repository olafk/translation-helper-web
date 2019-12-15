package de.olafkock.liferay.translationhelper;

import java.util.HashMap;
import java.util.HashSet;

public class TranslationHelperThreadLocal {
	
	public static void activate() {
		tl.get().setActive();
	}
	
	public static void add(String key, String value) {
		TranslationHelperThreadLocal holder = tl.get();
		if(holder.isActive()) {
			holder.addResult(key, value);
		}
		else {
			clear();
		}
	}
	
	public static HashMap<String,HashSet<String>> retrieve() {
		return tl.get().getResults();
	}
	
	public static void clear() {
		tl.remove();
	}
	

	
	private HashMap<String,HashSet<String>> results;
	private boolean active = false;
	
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

	private boolean isActive() {
		return active;
	}

	private void setActive() {
		this.active = true;
	}
	
	private static final ThreadLocal<TranslationHelperThreadLocal> tl = new ThreadLocal<TranslationHelperThreadLocal>() {
		@Override
		protected TranslationHelperThreadLocal initialValue() {
			return new TranslationHelperThreadLocal();
		}
	};

}
