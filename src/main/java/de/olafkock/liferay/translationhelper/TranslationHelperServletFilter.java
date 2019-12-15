package de.olafkock.liferay.translationhelper;

import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.osgi.service.component.annotations.Component;


/**
 * This ServletFilter wraps Liferay's translation lookup with its own implementation by
 * injecting a different object to intercept all lookups. Note: Deactivation only works gracefully
 * if no other plugin uses the same trick. We're manually re-wiring an implementation that has
 * been initialized by Liferay core's spring wiring.
 * 
 * @author Olaf
 */


@Component(
	      immediate = true,
	      property = {
	    		  "servlet-context-name=",
	    		  "servlet-filter-name=Translation Filter",
	    		  "url-pattern=/*"
	      },
		service=Filter.class
)

public class TranslationHelperServletFilter implements Filter {
	private static final Log log = LogFactoryUtil.getLog(TranslationHelperServletFilter.class);
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info(TranslationHelperServletFilter.class.getName() + " " + "initializing");
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if(!initialized) {
			// initializing late, to cater for unknown start order of modules. 
			// When we're asked to filter a request for the first time, 
			// the portal is surely all initialized and ready to be manipulated.
			new LanguageUtil().setLanguage(new LanguageWrapper(LanguageUtil.getLanguage()));
			this.initialized = true;
		}
		try {
			TranslationHelperThreadLocal.clear();
			TranslationHelperThreadLocal.activate();
			chain.doFilter(request, response);
		} finally {
			TranslationHelperThreadLocal.clear();
		}
	}

	@Override
	public void destroy() {
		if(initialized) {
			LanguageUtil languageUtil = new LanguageUtil();
			Language language = LanguageUtil.getLanguage();
			if(language instanceof LanguageWrapper) {
				languageUtil.setLanguage(((LanguageWrapper)language).unwrap());
			}
			initialized = false;
		}
		log.info(TranslationHelperServletFilter.class.getName() + " " + "shut down");
	}

	private boolean initialized = false;
}
