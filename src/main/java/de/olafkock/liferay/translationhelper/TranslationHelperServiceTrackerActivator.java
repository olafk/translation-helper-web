package de.olafkock.liferay.translationhelper;

import javax.portlet.Portlet;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;

/**
 * Initializes the {@link PortletServiceListener}
 * @author Olaf Kock
 */
public class TranslationHelperServiceTrackerActivator implements BundleActivator {

	private PortletServiceListener sl;

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		sl = new PortletServiceListener(bundleContext);
		String filter = "(objectclass="+Portlet.class.getName()+")";
		try {
			// get notified of all new portlet services added in future, to add filter
			bundleContext.addServiceListener(sl, filter);
			
			// initialize filters for all portlets that have already been registered
			@SuppressWarnings("rawtypes")
			ServiceReference[] srl = bundleContext.getServiceReferences(Portlet.class.getName(), filter);
			for (int i = 0; srl != null && i < srl.length; i++) {
				sl.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, srl[i]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		String filter = "(objectclass="+Portlet.class.getName()+")";
		try {
			@SuppressWarnings("rawtypes")
			ServiceReference[] srl = bundleContext.getServiceReferences(Portlet.class.getName(), filter);
			for (int i = 0; srl != null && i < srl.length; i++) {
				sl.serviceChanged(new ServiceEvent(ServiceEvent.UNREGISTERING, srl[i]));
			}
			bundleContext.removeServiceListener(sl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}