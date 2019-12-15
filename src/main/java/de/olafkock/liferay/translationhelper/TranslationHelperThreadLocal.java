package de.olafkock.liferay.translationhelper;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Keep a context for translation during the current thread.
 * 
 * Provides a global (thread local) context that can be retrieved upon lookup
 * 
 * @author Olaf Kock
 */

public class TranslationHelperThreadLocal {

	//// STATIC CONVENIENCE METHODS
	
	/**
	 * LanguageWrapper is applied unconditionally, and will register information with
	 * this ThreadLocal. However, the filter isn't applied to each and every request,
	 * thus this flag will signal if a ThreadLocal value is used or not. Without this
	 * safeguard, we'd risk to leak ThreadLocals.
	 */
	public static void activate() {
		tl.get().setActive();
	}

	/**
	 * Remember a key, value and context that has been looked up
	 * @param key the localization key
	 * @param value the value that was resolved by the key
	 * @param context 2-element array of context during resolving of this key/value
	 */
	
	public static void add(String key, String value, String[] context) {
		TranslationHelperThreadLocal holder = tl.get();
		if(holder.isActive()) {
			holder.addResult(key, combine(value, context));
		}
		else {
			// don't leak ThreadLocals
			clear();
		}
	}

	/**
	 * retrieve everything that has been looked up so far.
	 * @return map of keys to values with context (a 3-element-array)
	 */
	public static HashMap<String,HashSet<String[]>> retrieve() {
		return tl.get().getResults();
	}

	/**
	 * remove the underlying ThreadLocal, to make sure we're never leaking those values
	 */
	public static void clear() {
		tl.remove();
	}
	
	/**
	 * Retrieve the current lookup context ("no-context-available" if unset) 
	 * @return
	 */
	public static String retrieveContext() {
		return tl.get().getContext();
	}

	/**
	 * Set the name of the current lookup context (use "no-context-available" if unset) 
	 * @return
	 */
	public static void initContext(String context) {
		TranslationHelperThreadLocal holder = tl.get();
		if(holder.isActive()) {
			holder.setContext(context);
		}
	}

	//// STATIC IMPLEMENTATION
	
	private static String[] combine(String value, String[] context) {
		String[] result = new String[context.length+1];
		result[0] = value;
		for (int i = 0; i < context.length; i++) {
			result[i+1] = context[i];
		}
		return result;
	}
	

	//// THREAD LOCAL IMPLEMENTATION - to be used from static convenience methods only.
	
	private HashMap<String,HashSet<String[]>> results;
	private boolean active = false;
	private String context = "no-context-available";
	
	private TranslationHelperThreadLocal() {
		this.results = new HashMap<String, HashSet<String[]>>();
	}
	
	private void addResult(String key, String[] value) {
		if(value[1] == null || value[1].equals("") || value[1].equals("-")) {
			value[1] = "context: " + context;
		}
		HashSet<String[]> values = this.results.get(key);
		if(values == null) {
			values = new HashSet<String[]>();
			results.put(key, values);
		}
		values.add(value);
	}
	
	private HashMap<String,HashSet<String[]>> getResults() {
		return this.results;
	}

	private boolean isActive() {
		return active;
	}

	private void setActive() {
		this.active = true;
	}
	
	private String getContext() {
		return context;
	}
	
	private void setContext(String context) {
		this.context = context;
	}
	
	private static final ThreadLocal<TranslationHelperThreadLocal> tl = new ThreadLocal<TranslationHelperThreadLocal>() {
		@Override
		protected TranslationHelperThreadLocal initialValue() {
			return new TranslationHelperThreadLocal();
		}
	};


}
