package de.olafkock.liferay.translationhelper;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.util.ResourceBundleLoader;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Supplier;

import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Keep track of all lookups for language keys.
 * 
 * @author Olaf Kock
 *
 */
public class LanguageWrapper implements Language {
	/**
	 * If this class isn't embedded here, I've ran into classloading problems
	 * during runtime - nice quickfix to make sure it's available
	 * 
	 * @author Olaf Kock
	 *
	 */
	public static class LookupResult {
		private final String key;
		private final String value;

		public LookupResult(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return this.value;
		}

		public String toString() {
			return key + MAGIC + value;
		}

		@Override
		public int hashCode() {
			return toString().hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof LookupResult) {
				return toString().equals(obj.toString());
			} else {
				return false;
			}
		}

		private static final String MAGIC = "------";

	};

	// Development time: Not really undeploying old ones on updates.
	// ToDo: Unregister wrapper when undeploying
	public LanguageWrapper(Language wrappee) {
		if (wrappee instanceof LanguageWrapper) {
			wrappee = ((LanguageWrapper) wrappee).unwrap();
		}
		delegate = wrappee;
	}

	public Language unwrap() {
		return delegate;
	}

	private Language delegate;

	public String format(HttpServletRequest httpServletRequest, String pattern,
			com.liferay.portal.kernel.language.LanguageWrapper argument) {
		String result = delegate.format(httpServletRequest, pattern, argument);
		TranslationHelperThreadLocal.add(new LookupResult(pattern, result));
		return result;
	}

	public String format(HttpServletRequest httpServletRequest, String pattern,
			com.liferay.portal.kernel.language.LanguageWrapper argument, boolean translateArguments) {
		String result = delegate.format(httpServletRequest, pattern, argument, translateArguments);
		TranslationHelperThreadLocal.add(new LookupResult(pattern, result));
		return result;
	}

	public String format(HttpServletRequest httpServletRequest, String pattern,
			com.liferay.portal.kernel.language.LanguageWrapper[] arguments) {
		String result = delegate.format(httpServletRequest, pattern, arguments);
		TranslationHelperThreadLocal.add(new LookupResult(pattern, result));
		return result;
	}

	public String format(HttpServletRequest httpServletRequest, String pattern,
			com.liferay.portal.kernel.language.LanguageWrapper[] arguments, boolean translateArguments) {
		String result = delegate.format(httpServletRequest, pattern, arguments, translateArguments);
		TranslationHelperThreadLocal.add(new LookupResult(pattern, result));
		return result;
	}

	public String format(HttpServletRequest httpServletRequest, String pattern, Object argument) {
		String result = delegate.format(httpServletRequest, pattern, argument);
		TranslationHelperThreadLocal.add(new LookupResult(pattern, result));
		return result;
	}

	public String format(HttpServletRequest httpServletRequest, String pattern, Object argument,
			boolean translateArguments) {
		String result = delegate.format(httpServletRequest, pattern, argument, translateArguments);
		TranslationHelperThreadLocal.add(new LookupResult(pattern, result));
		return result;
	}

	public String format(HttpServletRequest httpServletRequest, String pattern, Object[] arguments) {
		String result = delegate.format(httpServletRequest, pattern, arguments);
		TranslationHelperThreadLocal.add(new LookupResult(pattern, result));
		return result;
	}

	public String format(HttpServletRequest httpServletRequest, String pattern, Object[] arguments,
			boolean translateArguments) {
		String result = delegate.format(httpServletRequest, pattern, arguments, translateArguments);
		TranslationHelperThreadLocal.add(new LookupResult(pattern, result));
		return result;
	}

	public String format(Locale locale, String pattern, List<Object> arguments) {
		String result = delegate.format(locale, pattern, arguments);
		TranslationHelperThreadLocal.add(new LookupResult(pattern, result));
		return result;
	}

	public String format(Locale locale, String pattern, Object argument) {
		String result = delegate.format(locale, pattern, argument);
		TranslationHelperThreadLocal.add(new LookupResult(pattern, result));
		return result;
	}

	public String format(Locale locale, String pattern, Object argument, boolean translateArguments) {
		String result = delegate.format(locale, pattern, argument, translateArguments);
		TranslationHelperThreadLocal.add(new LookupResult(pattern, result));
		return result;
	}

	public String format(Locale locale, String pattern, Object[] arguments) {
		String result = delegate.format(locale, pattern, arguments);
		TranslationHelperThreadLocal.add(new LookupResult(pattern, result));
		return result;
	}

	public String format(Locale locale, String pattern, Object[] arguments, boolean translateArguments) {
		String result = delegate.format(locale, pattern, arguments, translateArguments);
		TranslationHelperThreadLocal.add(new LookupResult(pattern, result));
		return result;
	}

	public String format(ResourceBundle resourceBundle, String pattern, Object argument) {
		String result = delegate.format(resourceBundle, pattern, argument);
		TranslationHelperThreadLocal.add(new LookupResult(pattern, result));
		return result;
	}

	public String format(ResourceBundle resourceBundle, String pattern, Object argument, boolean translateArguments) {
		String result = delegate.format(resourceBundle, pattern, argument, translateArguments);
		TranslationHelperThreadLocal.add(new LookupResult(pattern, result));
		return result;
	}

	public String format(ResourceBundle resourceBundle, String pattern, Object[] arguments) {
		String result = delegate.format(resourceBundle, pattern, arguments);
		TranslationHelperThreadLocal.add(new LookupResult(pattern, result));
		return result;
	}

	public String format(ResourceBundle resourceBundle, String pattern, Object[] arguments,
			boolean translateArguments) {
		String result = delegate.format(resourceBundle, pattern, arguments, translateArguments);
		TranslationHelperThreadLocal.add(new LookupResult(pattern, result));
		return result;
	}

	public String get(HttpServletRequest httpServletRequest, ResourceBundle resourceBundle, String key) {
		String result = delegate.get(httpServletRequest, resourceBundle, key);
		TranslationHelperThreadLocal.add(new LookupResult(key, result));
		return result;
	}

	public String get(HttpServletRequest httpServletRequest, ResourceBundle resourceBundle, String key,
			String defaultValue) {
		String result = delegate.get(httpServletRequest, resourceBundle, key, defaultValue);
		TranslationHelperThreadLocal.add(new LookupResult(key, result));
		return result;
	}

	public String get(HttpServletRequest httpServletRequest, String key) {
		String result = delegate.get(httpServletRequest, key);
		TranslationHelperThreadLocal.add(new LookupResult(key, result));
		return result;
	}

	public String get(HttpServletRequest httpServletRequest, String key, String defaultValue) {
		String result = delegate.get(httpServletRequest, key, defaultValue);
		TranslationHelperThreadLocal.add(new LookupResult(key, result));
		return result;
	}

	public String get(Locale locale, String key) {
		String result = delegate.get(locale, key);
		TranslationHelperThreadLocal.add(new LookupResult(key, result));
		return result;
	}

	public String get(Locale locale, String key, String defaultValue) {
		String result = delegate.get(locale, key, defaultValue);
		TranslationHelperThreadLocal.add(new LookupResult(key, result));
		return result;
	}

	public String get(ResourceBundle resourceBundle, String key) {
		String result = delegate.get(resourceBundle, key);
		TranslationHelperThreadLocal.add(new LookupResult(key, result));
		return result;
	}

	public String get(ResourceBundle resourceBundle, String key, String defaultValue) {
		String result = delegate.get(resourceBundle, key, defaultValue);
		TranslationHelperThreadLocal.add(new LookupResult(key, result));
		return result;
	}

	public Set<Locale> getAvailableLocales() {
		return delegate.getAvailableLocales();
	}

	public Set<Locale> getAvailableLocales(long groupId) {
		return delegate.getAvailableLocales(groupId);
	}

	public String getBCP47LanguageId(HttpServletRequest httpServletRequest) {
		return delegate.getBCP47LanguageId(httpServletRequest);
	}

	public String getBCP47LanguageId(Locale locale) {
		return delegate.getBCP47LanguageId(locale);
	}

	public String getBCP47LanguageId(PortletRequest portletRequest) {
		return delegate.getBCP47LanguageId(portletRequest);
	}

	public Set<Locale> getCompanyAvailableLocales(long companyId) {
		return delegate.getCompanyAvailableLocales(companyId);
	}

	public String getLanguageId(HttpServletRequest httpServletRequest) {
		return delegate.getLanguageId(httpServletRequest);
	}

	public String getLanguageId(Locale locale) {
		return delegate.getLanguageId(locale);
	}

	public String getLanguageId(PortletRequest portletRequest) {
		return delegate.getLanguageId(portletRequest);
	}

	public long getLastModified() {
		return delegate.getLastModified();
	}

	public Locale getLocale(long groupId, String languageCode) {
		return delegate.getLocale(groupId, languageCode);
	}

	public Locale getLocale(String languageCode) {
		return delegate.getLocale(languageCode);
	}

	public ResourceBundleLoader getPortalResourceBundleLoader() {
		return delegate.getPortalResourceBundleLoader();
	}

	public Set<Locale> getSupportedLocales() {
		return delegate.getSupportedLocales();
	}

	public String getTimeDescription(HttpServletRequest httpServletRequest, long milliseconds) {
		return delegate.getTimeDescription(httpServletRequest, milliseconds);
	}

	public String getTimeDescription(HttpServletRequest httpServletRequest, long milliseconds, boolean approximate) {
		return delegate.getTimeDescription(httpServletRequest, milliseconds, approximate);
	}

	public String getTimeDescription(HttpServletRequest httpServletRequest, Long milliseconds) {
		return delegate.getTimeDescription(httpServletRequest, milliseconds);
	}

	public String getTimeDescription(Locale locale, long milliseconds) {
		return delegate.getTimeDescription(locale, milliseconds);
	}

	public String getTimeDescription(Locale locale, long milliseconds, boolean approximate) {
		return delegate.getTimeDescription(locale, milliseconds, approximate);
	}

	public String getTimeDescription(Locale locale, Long milliseconds) {
		return delegate.getTimeDescription(locale, milliseconds);
	}

	public void init() {
		delegate.init();
	}

	public boolean isAvailableLanguageCode(String languageCode) {
		return delegate.isAvailableLanguageCode(languageCode);
	}

	public boolean isAvailableLocale(Locale locale) {
		return delegate.isAvailableLocale(locale);
	}

	public boolean isAvailableLocale(long groupId, Locale locale) {
		return delegate.isAvailableLocale(groupId, locale);
	}

	public boolean isAvailableLocale(long groupId, String languageId) {
		return delegate.isAvailableLocale(groupId, languageId);
	}

	public boolean isAvailableLocale(String languageId) {
		return delegate.isAvailableLocale(languageId);
	}

	public boolean isBetaLocale(Locale locale) {
		return delegate.isBetaLocale(locale);
	}

	public boolean isDuplicateLanguageCode(String languageCode) {
		return delegate.isDuplicateLanguageCode(languageCode);
	}

	public boolean isInheritLocales(long groupId) throws PortalException {
		return delegate.isInheritLocales(groupId);
	}

	public boolean isSameLanguage(Locale locale1, Locale locale2) {
		return delegate.isSameLanguage(locale1, locale2);
	}

	/**
	 * @deprecated
	 */
	public String process(ResourceBundle resourceBundle, Locale locale, String content) {
		return delegate.process(resourceBundle, locale, content);
	}

	public String process(Supplier<ResourceBundle> resourceBundleSupplier, Locale locale, String content) {
		return delegate.process(resourceBundleSupplier, locale, content);
	}

	public void resetAvailableGroupLocales(long groupId) {
		delegate.resetAvailableGroupLocales(groupId);
	}

	public void resetAvailableLocales(long companyId) {
		delegate.resetAvailableLocales(companyId);
	}

	public void updateCookie(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Locale locale) {
		delegate.updateCookie(httpServletRequest, httpServletResponse, locale);
	}

}
