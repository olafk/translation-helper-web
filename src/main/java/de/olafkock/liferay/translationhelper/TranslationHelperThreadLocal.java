package de.olafkock.liferay.translationhelper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Keep a context for translation during the current thread.
 * 
 * Provides a global (thread local) context that can be retrieved upon lookup
 * 
 * @author Olaf Kock
 */

public class TranslationHelperThreadLocal {

	//// STATIC CONVENIENCE METHODS
	
	private static int stacktraceSkip = 4;
	private static int stacktraceMax = 17;

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

	public static HashMap<String,List<String>> retrieveStacktraces() {
		return tl.get().getStacktraces();
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

	public static void setStacktraceInterval(int skip, int max) {
		TranslationHelperThreadLocal.stacktraceSkip = skip;
		TranslationHelperThreadLocal.stacktraceMax = max;
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
	private HashMap<String,List<String>> stacktraces;
	private boolean active = false;
	private String context = "no-context-available";
	
	private TranslationHelperThreadLocal() {
		this.results = new HashMap<String, HashSet<String[]>>();
		this.stacktraces = new HashMap<String, List<String>>();
	}
	
	private void addResult(String key, String[] value) {
		if(value[1] == null || value[1].equals("") || value[1].equals("-")) {
			value[1] = "context: " + context;
		}
		HashSet<String[]> values = this.results.get(key);
		if(values == null) {
			values = new HashSet<String[]>();
			results.put(key, values);
			LinkedList<String> stacks = new LinkedList<String>();
			stacktraces.put(key, stacks);
		}
		values.add(value);
		stacktraces.get(key).add(currentStacktrace());
	}
	
	private String currentStacktrace() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		StringBuffer result = new StringBuffer();
		for (int i = stacktraceSkip; i < Math.min(stacktraceMax, stackTrace.length); i++) {
			StackTraceElement line = stackTrace[i];
			result.append(simplifyStackTraceLine(line.toString()))
			.append("\n");
		}
		return result.toString();
	}

	private String simplifyStackTraceLine(String line) {
		String result = line;
		int max = line.indexOf("._jspService("); 
		if(max>0) {
			String originalLine = line;
			result = "";
			line = line.substring(0, max);
			while(line.indexOf(".")>0) {
				int dot = line.indexOf(".");
				result += line.substring(0, dot).replace("_005f", "_");
				if(line.indexOf(".", dot+1)>0) {
					result += "/";
				} else {
					result += "/" + line.substring(dot+1).replace("_005f", "_").replace("_jsp", ".jsp");
				}
				line = line.substring(dot+1, line.length());
			}
			result += " (" + originalLine + ")";
			if(result.startsWith("org/apache/jsp/")) {
				result = result.substring("org/apache/jsp/".length());
			}
		}
		return result;
	}

	private HashMap<String,HashSet<String[]>> getResults() {
		return this.results;
	}

	private HashMap<String,List<String>> getStacktraces() {
		return stacktraces;
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
