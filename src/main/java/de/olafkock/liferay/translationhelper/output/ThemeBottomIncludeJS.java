package de.olafkock.liferay.translationhelper.output;

import com.liferay.portal.kernel.servlet.taglib.BaseDynamicInclude;
import com.liferay.portal.kernel.servlet.taglib.DynamicInclude;
import com.liferay.portal.kernel.util.HtmlUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;

import de.olafkock.liferay.translationhelper.TranslationHelperThreadLocal;

/**
 * A Dynamic Include component to add all the values translated in this request 
 * to the bottom of the page as a JS Map with a well known name. This map can be 
 * used with a more sophisticated application when the display of a table is not 
 * enough.
 * 
 * @author Olaf Kock
 */

@Component(
		immediate = true, 
		service = DynamicInclude.class
	)

public class ThemeBottomIncludeJS extends BaseDynamicInclude {

	@Override
	public void include(
			HttpServletRequest request, HttpServletResponse response,
			String dynamicIncludeKey) throws IOException {
		HashMap<String,HashSet<String[]>> result = TranslationHelperThreadLocal.retrieve();
		if(result.size()==0) {
			return;
		}
		PrintWriter printWriter = response.getWriter();
		printWriter.println("<script>"
				+ "var liferayLanguageLookups = new Map([");
		for (String key : result.keySet()) {
			printWriter.print(" ['" + HtmlUtil.escapeJS(key) + "', [");
			for (Iterator<String[]> iterator = result.get(key).iterator(); iterator.hasNext();) {
				String[] value = iterator.next();
				printWriter.print("'" + HtmlUtil.escapeJS(value[0]) + "'");
				if(iterator.hasNext()) {
					printWriter.print(", ");
				} else {
					printWriter.print("]");
				}
			}
			printWriter.println( "],");
		}
		printWriter.println("]);");
		printWriter.println("</script>");	
	}

	@Override
	public void register(DynamicIncludeRegistry dynamicIncludeRegistry) {
		dynamicIncludeRegistry.register("/html/common/themes/bottom.jsp#post");
	}

}