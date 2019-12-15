package de.olafkock.liferay.translationhelper;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;

import javax.portlet.filter.PortletFilter;
import javax.portlet.filter.RenderFilter;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

/**
 * Registers or Unregisters a PortletFilter for each portlet service that's
 * being started or stopped.
 * 
 * This is done dynamically as we'd like to register a filter for each and every portlet
 * installed in the system, without knowing about them upfront.
 * 
 * @author Olaf Kock
 *
 */
final class PortletServiceListener implements ServiceListener {
	public static final Log log = LogFactoryUtil.getLog(PortletServiceListener.class);

	PortletServiceListener(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}
	
	@Override
	public void serviceChanged(ServiceEvent event) {
		switch (event.getType()) {
		case ServiceEvent.REGISTERED:
			registerFilter(event.getServiceReference());
			break;
		case ServiceEvent.UNREGISTERING:
			unregisterFilter(event.getServiceReference());
			break;
		case ServiceEvent.MODIFIED:
			unregisterFilter(event.getServiceReference());
			registerFilter(event.getServiceReference());
			break;
		default:
			break;
		}
	}

	private void registerFilter(ServiceReference<?> portletServiceReference) {
		String portletName = (String) portletServiceReference.getProperty("javax.portlet.name");
		RenderFilter portletFilter;
		portletFilter = new TranslationContextProviderPortletFilter();

		ServiceRegistration<?> portletFilterReference = bundleContext.registerService(
				new String[] {PortletFilter.class.getName()}, 
				portletFilter, 
				getServiceProperties(portletName));
		services.put(portletName, portletFilterReference);
	}

	private void unregisterFilter(ServiceReference<?> portletServiceReference) {
		String portletName = (String) portletServiceReference.getProperty("javax.portlet.name");
		if(services.containsKey(portletName)) {
			services.remove(portletName).unregister();
		}
	}

	private Dictionary<String, Object> getServiceProperties(String portletName) {
		Dictionary<String,Object> result = new Hashtable<String,Object>();
		result.put("javax.portlet.name", portletName);
		result.put("service.ranking", new Integer(10000));
		return result;
	}

	private BundleContext bundleContext;
	private HashMap<String, ServiceRegistration<?>> services = new HashMap<String, ServiceRegistration<?>>();
}