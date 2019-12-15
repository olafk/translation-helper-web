package de.olafkock.liferay.translationhelper;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.HtmlUtil;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.filter.FilterChain;
import javax.portlet.filter.FilterConfig;
import javax.portlet.filter.RenderFilter;

/**
 * When looking up translation values, it'd be great to know which portlet triggered the lookup.
 * This portlet filter will provide the context of the currently rendered portlet.
 * 
 * It's dynamically installed through {@link PortletServiceListener}
 * 
 * @author Olaf Kock
 */


public class TranslationContextProviderPortletFilter implements RenderFilter {
	private static final Log log = LogFactoryUtil.getLog(TranslationContextProviderPortletFilter.class);
	private String context;
	
	@Override
	public void init(FilterConfig filterConfig) throws PortletException {
		context = HtmlUtil.escape(filterConfig.getPortletContext().getPortletContextName());
		log.info("FILTER INIT (" + context + ")");
	}

	@Override
	public void destroy() {
		log.info("FILTER DESTROY (" + context + ")");
	}

	@Override
	public void doFilter(RenderRequest request, RenderResponse response, FilterChain chain)
			throws IOException, PortletException {
		log.info("filtering");
		String oldContext = TranslationHelperThreadLocal.retrieveContext();
		try {
			TranslationHelperThreadLocal.initContext(context);
			chain.doFilter(request, response);
		} finally {
			TranslationHelperThreadLocal.initContext(oldContext);
		}
	}

}
