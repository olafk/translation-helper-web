package de.olafkock.liferay.translationhelper;

import aQute.bnd.annotation.metatype.Meta;

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
}