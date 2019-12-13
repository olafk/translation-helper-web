package de.olafkock.liferay.translationhelper;

import java.util.Collection;
import java.util.HashSet;

import de.olafkock.liferay.translationhelper.LanguageWrapper.LookupResult;

public class TranslationHelperThreadLocal {
	public static void add(LookupResult lookupResult) {
		tl.get().addResult(lookupResult);
	}
	
	public static Collection<LookupResult> retrieve() {
		return tl.get().getResults();
	}
	
	public static void clear() {
		tl.get().clearResults();
	}

	public static void remove() {
		tl.remove();
	}
	
	
	
	private HashSet<LookupResult> results;
	
	private TranslationHelperThreadLocal() {
		this.results = new HashSet<LookupResult>();
	}
	
	private void addResult(LookupResult lookupResult) {
		this.results.add(lookupResult);
	}
	
	private Collection<LookupResult> getResults() {
		return this.results;
	}

	private void clearResults() {
		this.results = new HashSet<LookupResult>();
	}

	private static final ThreadLocal<TranslationHelperThreadLocal> tl = new ThreadLocal<TranslationHelperThreadLocal>() {
		@Override
		protected TranslationHelperThreadLocal initialValue() {
			return new TranslationHelperThreadLocal();
		}
	};

}
