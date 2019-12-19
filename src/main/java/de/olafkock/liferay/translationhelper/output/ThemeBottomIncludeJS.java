package de.olafkock.liferay.translationhelper.output;

import com.liferay.portal.kernel.servlet.taglib.BaseDynamicInclude;
import com.liferay.portal.kernel.servlet.taglib.DynamicInclude;
import com.liferay.portal.kernel.util.HtmlUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
		printWriter.println("<script>\n"
				+ "function getLiferayLanguageLookups() \n{\n"
				+ "  var liferayLanguageLookups = ([");
		Set<String> keySet = result.keySet();
		for (Iterator<String> keysIterator = keySet.iterator(); keysIterator.hasNext();) {
			String key = keysIterator.next();
			Set<String> valueContext = new HashSet<String>();
			
			// compute values and contexts. Stored in a set for deduplication
			for (Iterator<String[]> iterator = result.get(key).iterator(); iterator.hasNext();) {
				String[] currentValue = iterator.next();
				String tmp = "value: '" +  HtmlUtil.escapeJS(currentValue[0]) + "', "
						+ "context: '" + HtmlUtil.escape(currentValue[1] + "/" + currentValue[2]) + "'";
				valueContext.add(tmp);
			}
			
			// to make processing easier on the app side, introducing fake indexed keys in case there are
			// multiple lookups for the same keys
			boolean decorateWithIndex = valueContext.size() > 1;
			int counter = 0;
			for (Iterator<String> iterator = valueContext.iterator(); iterator.hasNext();) {
				String string = iterator.next();
				printWriter.print("    { key: '" + HtmlUtil.escape(key + (decorateWithIndex?"--"+counter++:"")) + "', ");
				printWriter.print(string);
				printWriter.print("}");
				if(iterator.hasNext()) {
					printWriter.println(",");
				}
			}
			if(keysIterator.hasNext()) {
				printWriter.println(",");
			}
		}
		printWriter.println("\n  ]);");
		printWriter.println("  return liferayLanguageLookups;\n}\n</script>");	
	}

	@Override
	public void register(DynamicIncludeRegistry dynamicIncludeRegistry) {
		dynamicIncludeRegistry.register("/html/common/themes/bottom.jsp#post");
	}

}