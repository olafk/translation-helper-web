package de.olafkock.liferay.translationhelper;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ResourceBundleLoader;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Supplier;

import javax.portlet.PortletConfig;
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

	public LanguageWrapper(Language wrappee) {
		delegate = wrappee;
	}

	public Language unwrap() {
		return delegate;
	}

	private String[] retrieveContext(Locale locale, String key) {
		String context = TranslationHelperThreadLocal.retrieveContext();
		return new String[] {"Locale:" + (locale==null?"null":locale.getLanguage()), context};
	}
	
	private String[] retrieveContext(ResourceBundle rb, String key) {
		String context = TranslationHelperThreadLocal.retrieveContext();
		return new String[] {"ResourceBundle", context};
	}
	
	private String[] retrieveContext(HttpServletRequest httpServletRequest, String key) {
		PortletConfig portletConfig =
				(PortletConfig)httpServletRequest.getAttribute(
					JavaConstants.JAVAX_PORTLET_CONFIG);

		Locale locale = _getLocale(httpServletRequest);

		if (portletConfig == null) {
			return new String[] {"null", "portal"};
		}

		ResourceBundle resourceBundle = portletConfig.getResourceBundle(locale);

		if (resourceBundle.containsKey(key)) {
			return new String[] { portletConfig.getPortletName(), "portlet" };
		} else {
			return new String[] { portletConfig.getPortletName(), "portal" };
		}
	}

	private Locale _getLocale(HttpServletRequest httpServletRequest) {
		Locale locale = null;

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		if (themeDisplay != null) {
			locale = themeDisplay.getLocale();
		}
		else {
			locale = httpServletRequest.getLocale();

			if (!isAvailableLocale(locale)) {
				locale = LocaleUtil.getDefault();
			}
		}

		return locale;
	}

	private Language delegate;

	@Override
	public String format(HttpServletRequest httpServletRequest, String pattern,
			com.liferay.portal.kernel.language.LanguageWrapper argument) {
		String result = delegate.format(httpServletRequest, pattern, argument);
		String[] context = retrieveContext(httpServletRequest, pattern);
		TranslationHelperThreadLocal.add(pattern, result, context);
		return result;
	}

	@Override
	public String format(HttpServletRequest httpServletRequest, String pattern,
			com.liferay.portal.kernel.language.LanguageWrapper argument, boolean translateArguments) {
		String result = delegate.format(httpServletRequest, pattern, argument, translateArguments);
		String[] context = retrieveContext(httpServletRequest, pattern);
		TranslationHelperThreadLocal.add(pattern, result, context);
		return result;
	}

	@Override
	public String format(HttpServletRequest httpServletRequest, String pattern,
			com.liferay.portal.kernel.language.LanguageWrapper[] arguments) {
		String result = delegate.format(httpServletRequest, pattern, arguments);
		String[] context = retrieveContext(httpServletRequest, pattern);
		TranslationHelperThreadLocal.add(pattern, result, context);
		return result;
	}

	@Override
	public String format(HttpServletRequest httpServletRequest, String pattern,
			com.liferay.portal.kernel.language.LanguageWrapper[] arguments, boolean translateArguments) {
		String result = delegate.format(httpServletRequest, pattern, arguments, translateArguments);
		String[] context = retrieveContext(httpServletRequest, pattern);
		TranslationHelperThreadLocal.add(pattern, result, context);
		return result;
	}

	@Override
	public String format(HttpServletRequest httpServletRequest, String pattern, Object argument) {
		String result = delegate.format(httpServletRequest, pattern, argument);
		String[] context = retrieveContext(httpServletRequest, pattern);
		TranslationHelperThreadLocal.add(pattern, result, context);
		return result;
	}

	@Override
	public String format(HttpServletRequest httpServletRequest, String pattern, Object argument,
			boolean translateArguments) {
		String result = delegate.format(httpServletRequest, pattern, argument, translateArguments);
		String[] context = retrieveContext(httpServletRequest, pattern);
		TranslationHelperThreadLocal.add(pattern, result, context);
		return result;
	}

	@Override
	public String format(HttpServletRequest httpServletRequest, String pattern, Object[] arguments) {
		String result = delegate.format(httpServletRequest, pattern, arguments);
		String[] context = retrieveContext(httpServletRequest, pattern);
		TranslationHelperThreadLocal.add(pattern, result, context);
		return result;
	}

	@Override
	public String format(HttpServletRequest httpServletRequest, String pattern, Object[] arguments,
			boolean translateArguments) {
		String result = delegate.format(httpServletRequest, pattern, arguments, translateArguments);
		String[] context = retrieveContext(httpServletRequest, pattern);
		TranslationHelperThreadLocal.add(pattern, result, context);
		return result;
	}

	@Override
	public String format(Locale locale, String pattern, List<Object> arguments) {
		String result = delegate.format(locale, pattern, arguments);
		String[] context = retrieveContext(locale, pattern);
		TranslationHelperThreadLocal.add(pattern, result, context);
		return result;
	}

	@Override
	public String format(Locale locale, String pattern, Object argument) {
		String result = delegate.format(locale, pattern, argument);
		String[] context = retrieveContext(locale, pattern);
		TranslationHelperThreadLocal.add(pattern, result, context);
		return result;
	}

	@Override
	public String format(Locale locale, String pattern, Object argument, boolean translateArguments) {
		String result = delegate.format(locale, pattern, argument, translateArguments);
		String[] context = retrieveContext(locale, pattern);
		TranslationHelperThreadLocal.add(pattern, result, context);
		return result;
	}

	@Override
	public String format(Locale locale, String pattern, Object[] arguments) {
		String result = delegate.format(locale, pattern, arguments);
		String[] context = retrieveContext(locale, pattern);
		TranslationHelperThreadLocal.add(pattern, result, context);
		return result;
	}

	@Override
	public String format(Locale locale, String pattern, Object[] arguments, boolean translateArguments) {
		String result = delegate.format(locale, pattern, arguments, translateArguments);
		String[] context = retrieveContext(locale, pattern);
		TranslationHelperThreadLocal.add(pattern, result, context);
		return result;
	}

	@Override
	public String format(ResourceBundle resourceBundle, String pattern, Object argument) {
		String result = delegate.format(resourceBundle, pattern, argument);
		String[] context = retrieveContext(resourceBundle, pattern);
		TranslationHelperThreadLocal.add(pattern, result, context);
		return result;
	}

	@Override
	public String format(ResourceBundle resourceBundle, String pattern, Object argument, boolean translateArguments) {
		String result = delegate.format(resourceBundle, pattern, argument, translateArguments);
		String[] context = retrieveContext(resourceBundle, pattern);
		TranslationHelperThreadLocal.add(pattern, result, context);
		return result;
	}

	@Override
	public String format(ResourceBundle resourceBundle, String pattern, Object[] arguments) {
		String result = delegate.format(resourceBundle, pattern, arguments);
		String[] context = retrieveContext(resourceBundle, pattern);
		TranslationHelperThreadLocal.add(pattern, result, context);
		return result;
	}

	@Override
	public String format(ResourceBundle resourceBundle, String pattern, Object[] arguments,
			boolean translateArguments) {
		String result = delegate.format(resourceBundle, pattern, arguments, translateArguments);
		String[] context = retrieveContext(resourceBundle, pattern);
		TranslationHelperThreadLocal.add(pattern, result, context);
		return result;
	}

	@Override
	public String formatStorageSize(double size, Locale locale) {
		return delegate.formatStorageSize(size, locale);
	}

	@Override
	public String get(HttpServletRequest httpServletRequest, ResourceBundle resourceBundle, String key) {
		String result = delegate.get(httpServletRequest, resourceBundle, key);
		String[] context = retrieveContext(httpServletRequest, key);
		TranslationHelperThreadLocal.add(key, result, context);
		return result;
	}

	@Override
	public String get(HttpServletRequest httpServletRequest, ResourceBundle resourceBundle, String key,
			String defaultValue) {
		String result = delegate.get(httpServletRequest, resourceBundle, key, defaultValue);
		String[] context = retrieveContext(httpServletRequest, key);
		TranslationHelperThreadLocal.add(key, result, context);
		return result;
	}

	@Override
	public String get(HttpServletRequest httpServletRequest, String key) {
		String result = delegate.get(httpServletRequest, key);
		String[] context = retrieveContext(httpServletRequest, key);
		TranslationHelperThreadLocal.add(key, result, context);
		return result;
	}

	@Override
	public String get(HttpServletRequest httpServletRequest, String key, String defaultValue) {
		String result = delegate.get(httpServletRequest, key, defaultValue);
		String[] context = retrieveContext(httpServletRequest, key);
		TranslationHelperThreadLocal.add(key, result, context);
		return result;
	}

	@Override
	public String get(Locale locale, String key) {
		String result = delegate.get(locale, key);
		String[] context = retrieveContext(locale, key);
		TranslationHelperThreadLocal.add(key, result, context);
		return result;
	}

	@Override
	public String get(Locale locale, String key, String defaultValue) {
		String result = delegate.get(locale, key, defaultValue);
		String[] context = retrieveContext(locale, key);
		TranslationHelperThreadLocal.add(key, result, context);
		return result;
	}

	@Override
	public String get(ResourceBundle resourceBundle, String key) {
		String result = delegate.get(resourceBundle, key);
		String[] context = retrieveContext(resourceBundle, key);
		TranslationHelperThreadLocal.add(key, result, context);
		return result;
	}

	@Override
	public String get(ResourceBundle resourceBundle, String key, String defaultValue) {
		String result = delegate.get(resourceBundle, key, defaultValue);
		String[] context = retrieveContext(resourceBundle, key);
		TranslationHelperThreadLocal.add(key, result, context);
		return result;
	}

	@Override
	public Set<Locale> getAvailableLocales() {
		return delegate.getAvailableLocales();
	}

	@Override
	public Set<Locale> getAvailableLocales(long groupId) {
		return delegate.getAvailableLocales(groupId);
	}

	@Override
	public String getBCP47LanguageId(HttpServletRequest httpServletRequest) {
		return delegate.getBCP47LanguageId(httpServletRequest);
	}

	@Override
	public String getBCP47LanguageId(Locale locale) {
		return delegate.getBCP47LanguageId(locale);
	}

	@Override
	public String getBCP47LanguageId(PortletRequest portletRequest) {
		return delegate.getBCP47LanguageId(portletRequest);
	}

	@Override
	public Set<Locale> getCompanyAvailableLocales(long companyId) {
		return delegate.getCompanyAvailableLocales(companyId);
	}

	@Override
	public String getLanguageId(HttpServletRequest httpServletRequest) {
		return delegate.getLanguageId(httpServletRequest);
	}

	@Override
	public String getLanguageId(Locale locale) {
		return delegate.getLanguageId(locale);
	}

	@Override
	public String getLanguageId(PortletRequest portletRequest) {
		return delegate.getLanguageId(portletRequest);
	}

	@Override
	public long getLastModified() {
		return delegate.getLastModified();
	}

	@Override
	public Locale getLocale(long groupId, String languageCode) {
		return delegate.getLocale(groupId, languageCode);
	}

	@Override
	public Locale getLocale(String languageCode) {
		return delegate.getLocale(languageCode);
	}

	@Override
	public ResourceBundleLoader getPortalResourceBundleLoader() {
		return delegate.getPortalResourceBundleLoader();
	}

	@Override
	public Set<Locale> getSupportedLocales() {
		return delegate.getSupportedLocales();
	}

	@Override
	public String getTimeDescription(HttpServletRequest httpServletRequest, long milliseconds) {
		return delegate.getTimeDescription(httpServletRequest, milliseconds);
	}

	@Override
	public String getTimeDescription(HttpServletRequest httpServletRequest, long milliseconds, boolean approximate) {
		return delegate.getTimeDescription(httpServletRequest, milliseconds, approximate);
	}

	@Override
	public String getTimeDescription(HttpServletRequest httpServletRequest, Long milliseconds) {
		return delegate.getTimeDescription(httpServletRequest, milliseconds);
	}

	@Override
	public String getTimeDescription(Locale locale, long milliseconds) {
		return delegate.getTimeDescription(locale, milliseconds);
	}

	@Override
	public String getTimeDescription(Locale locale, long milliseconds, boolean approximate) {
		return delegate.getTimeDescription(locale, milliseconds, approximate);
	}

	@Override
	public String getTimeDescription(Locale locale, Long milliseconds) {
		return delegate.getTimeDescription(locale, milliseconds);
	}

	@Override
	public void init() {
		delegate.init();
	}

	@Override
	public boolean isAvailableLanguageCode(String languageCode) {
		return delegate.isAvailableLanguageCode(languageCode);
	}

	@Override
	public boolean isAvailableLocale(Locale locale) {
		return delegate.isAvailableLocale(locale);
	}

	@Override
	public boolean isAvailableLocale(long groupId, Locale locale) {
		return delegate.isAvailableLocale(groupId, locale);
	}

	@Override
	public boolean isAvailableLocale(long groupId, String languageId) {
		return delegate.isAvailableLocale(groupId, languageId);
	}

	@Override
	public boolean isAvailableLocale(String languageId) {
		return delegate.isAvailableLocale(languageId);
	}

	@Override
	public boolean isBetaLocale(Locale locale) {
		return delegate.isBetaLocale(locale);
	}

	@Override
	public boolean isDuplicateLanguageCode(String languageCode) {
		return delegate.isDuplicateLanguageCode(languageCode);
	}

	@Override
	public boolean isInheritLocales(long groupId) throws PortalException {
		return delegate.isInheritLocales(groupId);
	}

	@Override
	public boolean isSameLanguage(Locale locale1, Locale locale2) {
		return delegate.isSameLanguage(locale1, locale2);
	}

	@Override
	public String process(Supplier<ResourceBundle> resourceBundleSupplier, Locale locale, String content) {
		return delegate.process(resourceBundleSupplier, locale, content);
	}

	@Override
	public void resetAvailableGroupLocales(long groupId) {
		delegate.resetAvailableGroupLocales(groupId);
	}

	@Override
	public void resetAvailableLocales(long companyId) {
		delegate.resetAvailableLocales(companyId);
	}

	@Override
	public void updateCookie(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Locale locale) {
		delegate.updateCookie(httpServletRequest, httpServletResponse, locale);
	}
}
