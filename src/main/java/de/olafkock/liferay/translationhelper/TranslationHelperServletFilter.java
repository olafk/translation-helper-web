package de.olafkock.liferay.translationhelper;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;


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
	      configurationPid = "de.olafkock.liferay.translationhelper.Configuration",
	      property = {
	    		  "servlet-context-name=",
	    		  "servlet-filter-name=Translation Filter",
//	    		  "after-filter=Valid Host Name Filter",
	    		  "url-pattern=/*",
	    		  "url-pattern=/"
	      },
		service=Filter.class
)
public class TranslationHelperServletFilter implements Filter {
	private static final Log log = LogFactoryUtil.getLog(TranslationHelperServletFilter.class);
	private volatile Configuration configuration;
	private RoleLocalService roleLocalService;
	private long roleId = 0;
	private volatile boolean initialized = false;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info(TranslationHelperServletFilter.class.getName() + " " + "initializing");
		log.warn("Note: This plugin makes several assumptions about its environment - you shouldn't deploy it in production systems");
	}
	
	
	@Reference
	public void setConfigurationProvider(ConfigurationProvider configurationProvider) {
		roleId = 0;
	}

	@Activate
	@Modified
	private void activate(Map<String, Object> properties) {
		configuration = ConfigurableUtil.createConfigurable(Configuration.class, properties);
		roleId = 0;
	}
	
	
	@Reference
	public void setRoleLocalService(RoleLocalService roleLocalService) {
		this.roleLocalService = roleLocalService;
		roleId = 0;
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if(roleId == 0) {
			try {
				// Apparently, ServletFilters are too early in the chain - no ThemeDisplay is available yet,
				// so we'll have to get the required context through other means.
				// Note: This caching of the roleId will fail in situations with multiple instances, but
				// due to the debugging nature of this plugin, this circumstance is well accepted. Nobody 
				// wants to deploy a plugin like this in production systems. 
				Company company = PortalUtil.getCompany((HttpServletRequest) request);
				Role role = roleLocalService.fetchRole(company.getCompanyId(), configuration.showTranslatedOutputRole().trim());
				if(role != null) {
					roleId = role.getRoleId();
					log.info("roleId = " + roleId);
				}
			} catch (PortalException e) {
				log.error(e);
			}
		}
		if(! initialized) {
			// initializing late, to cater for unknown start order of modules. 
			// When we're asked to filter a request for the first time, 
			// the portal is surely all initialized and ready to be manipulated.
			new LanguageUtil().setLanguage(new LanguageWrapper(LanguageUtil.getLanguage()));
			this.initialized = true;
		}
		try {
			TranslationHelperThreadLocal.clear();
			User user = PortalUtil.initUser((HttpServletRequest) request);
			long[] roleIds = user.getRoleIds();
			if(ArrayUtil.contains(roleIds, roleId)) {
				TranslationHelperThreadLocal.activate();
			}
			chain.doFilter(request, response);
		} catch (Exception e) {
			log.error(e);
		} finally {
			TranslationHelperThreadLocal.clear();
		}
	}

	@Override
	public void destroy() {
		if(initialized) {

			// Note: This de-initialization will fail if other plugins use
			// the same ugly means of manually rewiring this spring-wired 
			// component as well. As this plugin shouldn't be deployed in 
			// production systems, that's well accepted.
			
			Language language = LanguageUtil.getLanguage();
			while(language instanceof LanguageWrapper) {
				language = ((LanguageWrapper)language).unwrap();
			}
			new LanguageUtil().setLanguage(language);
			initialized = false;
		}
		log.info(TranslationHelperServletFilter.class.getName() + " " + "shut down");
	}

}
