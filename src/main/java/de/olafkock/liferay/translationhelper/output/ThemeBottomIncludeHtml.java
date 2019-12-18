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
 * to the bottom of the page as a simple table.
 * 
 * @author Olaf Kock
 */

@Component(
		immediate = true, 
		service = DynamicInclude.class
	)

public class ThemeBottomIncludeHtml extends BaseDynamicInclude {

	@Override
	public void include(
			HttpServletRequest request, HttpServletResponse response,
			String dynamicIncludeKey) throws IOException {
		HashMap<String,HashSet<String[]>> result = TranslationHelperThreadLocal.retrieve();
		if(result.size()==0) {
			return;
		}
		StringBuffer suspicious = new StringBuffer();
		StringBuffer unsuspicious = new StringBuffer();
		for (String key : result.keySet()) {
			StringBuffer thisKey = new StringBuffer();
			StringBuffer target = unsuspicious;
			thisKey.append("<tr>\n  <td style=\"vertical-align:top;\">\n    " + HtmlUtil.escape(key) + "\n  </td>\n  <td>\n    <ul>\n");
			Iterator<String[]> iterator = result.get(key).iterator();
			HashSet<String> allValuesForThisKey = new HashSet<String>(); // For deduplication of individual lines without array-compare
			while (iterator.hasNext()) {
				StringBuffer thisValue = new StringBuffer();
				String[] value = iterator.next();
				if(value[0] == null) {
					thisValue.append("      <li class=\"translationhelper-suspicious\" title=\"" + value[1] + ", " + value[2] + "\" ><b><i>null</i></b>");
//					thisValue.append(" (" + value[1] + ", " + value[2] + ")");
					thisValue.append("</li>\n");
					target = suspicious;
				} else if(value[0].equals("")) {
					thisValue.append("      <li class=\"translationhelper-suspicious\" title=\"" + value[1] + ", " + value[2] + "\"><b><i>empty</i></b>");
//					thisValue.append(" (" + value[1] + ", " + value[2] + ")");
					thisValue.append("</li>\n");
					target = suspicious;
				} else if(value[0].equals(key)){
					thisValue.append("      <li class=\"translationhelper-suspicious\" title=\"" + value[1] + ", " + value[2] + "\">");
					thisValue.append(HtmlUtil.escape(value[0]));
//					thisValue.append(" (" + value[1] + ", " + value[2] + ")" );
					thisValue.append("</li>\n");
					target = suspicious;
				} else {
					thisValue.append("      <li class=\"translationhelper-unsuspicious\" title=\"" + value[1] + ", " + value[2] + "\">");
					thisValue.append(HtmlUtil.escape(value[0]));
//					thisValue.append(" (" + value[1] + ", " + value[2] + ")" );
					thisValue.append("</li>\n");
				}
				allValuesForThisKey.add(thisValue.toString()); // deduplicate
			}
			for (String string : allValuesForThisKey) {
				thisKey.append(string);
			}
			thisKey.append( "    </ul>\n  </td>\n</tr>\n");
			target.append(thisKey.toString());
		}
		PrintWriter printWriter = response.getWriter();
		printWriter.println("<div id=\"translation-helper-table-result\">");
		printWriter.println("<style>.translationhelper-suspicious { background-color:red; }</style>");
		printWriter.println("<h1>Translations on this page</h1>");
		printWriter.println("<table border=\"1\" style=\"vertical-align:top;\">");
		printWriter.println(suspicious);
		printWriter.println(unsuspicious);
		printWriter.println("</table>");
		printWriter.println("<p>Some values might be looked up multiple times - hover mouse over "
				+ "the translated value to see information about the context in which they have "
				+ "been looked up.</p>");
		printWriter.println("<p>Values marked red are 'suspect', e.g. empty, undefined, or values "
				+ "with identical lookup values. They're candidates to be missing translations, or "
				+ "duplicate lookups (e.g. already translated values that have been used as keys for "
				+ "a subsequent lookup)</p><br/>");
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