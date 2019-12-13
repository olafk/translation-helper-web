package de.olafkock.liferay.translationhelper;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * 
 * @author Olaf Kock
 */

public class TranslationHelperActivator implements BundleActivator {
	
	@Override
	public void start(BundleContext context) throws Exception {
		// initialization done in filter, as it's not safe to initialize here:
		// spring wiring of LanguageUtil might not be done yet when the server
		// is just starting new - 
	}
	
	@Override
	public void stop(BundleContext context) throws Exception {
		// de-initialization moved to filter.destroy method.
		// see start comment for reason
	}	
}