package de.olafkock.liferay.translationhelper;

import com.liferay.portal.kernel.servlet.taglib.BaseDynamicInclude;
import com.liferay.portal.kernel.servlet.taglib.DynamicInclude;
import com.liferay.portal.kernel.util.HtmlUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;

import de.olafkock.liferay.translationhelper.LanguageWrapper.LookupResult;

@Component(immediate = true, service = DynamicInclude.class)
public class ThemeBottomInclude extends BaseDynamicInclude {

	@Override
	public void include(
			HttpServletRequest request, HttpServletResponse response,
			String key) throws IOException {
		Collection<LookupResult> result = TranslationHelperThreadLocal.retrieve();
		PrintWriter printWriter = response.getWriter();
		printWriter.println("<script>"
				+ "var liferayLanguageLookups = new Map([");
		for (LookupResult lookupResult : result) {
			printWriter.println(" ['" + HtmlUtil.escapeJS(lookupResult.getKey()) + "', '" + HtmlUtil.escapeJS(lookupResult.getValue()) + "'] ,");
		}
		printWriter.println("]);");
		printWriter.println("</script>");	
	}

	@Override
	public void register(DynamicIncludeRegistry dynamicIncludeRegistry) {
		dynamicIncludeRegistry.register("/html/common/themes/bottom.jsp#post");
	}

}