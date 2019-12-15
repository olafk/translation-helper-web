package de.olafkock.liferay.translationhelper;

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

@Component(immediate = true, service = DynamicInclude.class)

public class ThemeBottomIncludeHtml extends BaseDynamicInclude {

	@Override
	public void include(
			HttpServletRequest request, HttpServletResponse response,
			String dynamicIncludeKey) throws IOException {
		HashMap<String,HashSet<String>> result = TranslationHelperThreadLocal.retrieve();
		PrintWriter printWriter = response.getWriter();
		printWriter.println("<div id=\"translation-helper-table-result\">");
		printWriter.println("<style>.translationhelper-unresolved { background-color:red; }</style>");
		printWriter.println("<h1>Translations on this page</h1>");
		printWriter.println("<table border=\"1\" style=\"vertical-align:top;\">");
		for (String key : result.keySet()) {
			printWriter.print("<tr>\n  <td style=\"vertical-align:top;\">\n    " + HtmlUtil.escape(key) + "\n  </td>\n  <td>\n    <ul>\n");
			for (Iterator<String> iterator = result.get(key).iterator(); iterator.hasNext();) {
				String value = iterator.next();
				if(value == null) {
					printWriter.println("      <li class=\"translationhelper-unresolved\"><b><i>null</i></b></li>");
				} else if(value.equals("")) {
					printWriter.println("      <li class=\"translationhelper-unresolved\"><b><i>empty</i></b></li>");
				} else {
					String clazz = value.equals(key) ? "translationhelper-unresolved" : "translationhelper-resolved";
					printWriter.print("      <li class=\"" + clazz + "\">");
					printWriter.print(HtmlUtil.escape(value));
					printWriter.println("</li>");
				}
			}
			printWriter.println( "    </ul>\n  </td>\n</tr>");
		}
		printWriter.println("</table>");
		printWriter.println("</div>");
		printWriter.println("<script>"
				+ "var translationhelperTarget = document.getElementById(\"content\");"
				+ "var translationhelperSource = document.getElementById(\"translation-helper-table-result\");"
				+ "translationhelperTarget.appendChild(translationhelperSource);"
				+ "</script>"
		);
	}

	@Override
	public void register(DynamicIncludeRegistry dynamicIncludeRegistry) {
		dynamicIncludeRegistry.register("/html/common/themes/bottom.jsp#post");
	}

}