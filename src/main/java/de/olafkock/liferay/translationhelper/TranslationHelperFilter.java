package de.olafkock.liferay.translationhelper;

import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;

@Component(
	      immediate = true,
	      property = {
	    		  "servlet-context-name=",
	    		  "servlet-filter-name=LifeDev Document Filter",
	    		  "url-pattern=/web/*",
	    		  "url-pattern=/group/*",
	      },
		service=Filter.class
)

public class TranslationHelperFilter implements Filter {
	private static final Log log = LogFactoryUtil.getLog(TranslationHelperFilter.class);
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info(TranslationHelperFilter.class.getName() + " " + "initializing");
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if(!initialized) {
			new LanguageUtil().setLanguage(new LanguageWrapper(LanguageUtil.getLanguage()));
			this.initialized = true;
		}
		try {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			if("GET".equals(httpServletRequest.getMethod())) {
				log.info("FILTERING " + httpServletRequest.getMethod());
				TranslationHelperThreadLocal.clear();
				
				chain.doFilter(request, response);
	
				report();
			} else {
				log.info("IGNORING " + httpServletRequest.getMethod());
				chain.doFilter(request, response);
			}
		} finally {
			TranslationHelperThreadLocal.remove();
		}
	}

	private void report() {
		HashMap<String, HashSet<String>> result = TranslationHelperThreadLocal.retrieve();
//		for (LookupResult lookupResult : result) {
//			log.info(lookupResult);
//		}
		log.info("*************************" + result.size() + " keys looked up");
	}

	@Override
	public void destroy() {
		LanguageUtil languageUtil = new LanguageUtil();
		Language language = LanguageUtil.getLanguage();
		while(language instanceof LanguageWrapper) {
			languageUtil.setLanguage(((LanguageWrapper)language).unwrap());
			language = LanguageUtil.getLanguage();
		}
		initialized = false;
		log.info(TranslationHelperFilter.class.getName() + " " + "shut down");
	}

	private boolean initialized = false;
}
