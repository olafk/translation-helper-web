package de.olafkock.liferay.translationhelper;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

import aQute.bnd.annotation.metatype.Meta;

@ExtendedObjectClassDefinition(category = "localization")
@Meta.OCD(
	    id = "de.olafkock.liferay.translationhelper.Configuration"
	    , localization = "content/Language"
	    , name = "translationhelper-documentation-configuration-name"
	)
public interface Configuration {
    
	@Meta.AD(
			deflt = "Administrator",
			description = "show-translated-output-role-description",
			name = "show-translated-output-role",
			required = false
			)
	public String showTranslatedOutputRole();
	
	@Meta.AD(
			deflt = "/translationhelper/app.js",
			description = "application-url-description",
			name = "application-url",
			required = false
			)
	public String applicationUrl();
	
	@Meta.AD(
			deflt = "background-color:red;",
			description = "suspicious-entry-styling-description",
			name = "suspicious-entry-styling",
			required = false
			)
	public String suspiciousEntryStyling();
	
	@Meta.AD(
			deflt = "5",
			description = "stacktrace-skip-description",
			name = "stacktrace-skip",
			required = false
			)
	public int stacktraceSkip();
	
	@Meta.AD(
			deflt = "17",
			description = "stacktrace-max-description",
			name = "stacktrace-max",
			required = false
			)
	public int stacktraceMax();
	
		
	
	
	
	
}