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
		StringBuffer suspicious = new StringBuffer();
		StringBuffer unsuspicious = new StringBuffer();
		for (String key : result.keySet()) {
			StringBuffer thisLine = new StringBuffer();
			StringBuffer target = unsuspicious;
			thisLine.append("<tr>\n  <td style=\"vertical-align:top;\">\n    " + HtmlUtil.escape(key) + "\n  </td>\n  <td>\n    <ul>\n");
			for (Iterator<String> iterator = result.get(key).iterator(); iterator.hasNext();) {
				String value = iterator.next();
				if(value == null) {
					thisLine.append("      <li class=\"translationhelper-unresolved\"><b><i>null</i></b></li>\n");
					target = suspicious;
				} else if(value.equals("")) {
					thisLine.append("      <li class=\"translationhelper-unresolved\"><b><i>empty</i></b></li>\n");
					target = suspicious;
				} else if(value.equals(key)){
					thisLine.append("      <li class=\"translationhelper-unresolved\">");
					thisLine.append(HtmlUtil.escape(value));
					thisLine.append("</li>\n");
					target = suspicious;
				} else {
					thisLine.append("      <li class=\"translationhelper-resolved\">");
					thisLine.append(HtmlUtil.escape(value));
					thisLine.append("</li>\n");
				}
			}
			thisLine.append( "    </ul>\n  </td>\n</tr>\n");
			target.append(thisLine.toString());
		}
		PrintWriter printWriter = response.getWriter();
		printWriter.println("<div id=\"translation-helper-table-result\">");
		printWriter.println("<style>.translationhelper-unresolved { background-color:red; }</style>");
		printWriter.println("<h1>Translations on this page</h1>");
		printWriter.println("<table border=\"1\" style=\"vertical-align:top;\">");
		printWriter.println(suspicious);
		printWriter.println(unsuspicious);
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