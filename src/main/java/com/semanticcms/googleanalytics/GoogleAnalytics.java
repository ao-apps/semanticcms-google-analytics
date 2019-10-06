/*
 * semanticcms-google-analytics - Includes the Google Analytics tracking code in SemanticCMS pages.
 * Copyright (C) 2016, 2017, 2019  AO Industries, Inc.
 *     support@aoindustries.com
 *     7262 Bull Pen Cir
 *     Mobile, AL 36695
 *
 * This file is part of semanticcms-google-analytics.
 *
 * semanticcms-google-analytics is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * semanticcms-google-analytics is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with semanticcms-google-analytics.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.semanticcms.googleanalytics;

import static com.aoindustries.encoding.JavaScriptInXhtmlEncoder.javaScriptInXhtmlEncoder;
import com.aoindustries.encoding.MediaWriter;
import static com.aoindustries.encoding.TextInJavaScriptEncoder.encodeTextInJavaScript;
import static com.aoindustries.encoding.TextInXhtmlAttributeEncoder.textInXhtmlAttributeEncoder;
import com.aoindustries.net.URIEncoder;
import com.semanticcms.core.model.Page;
import com.semanticcms.core.servlet.Component;
import com.semanticcms.core.servlet.ComponentPosition;
import com.semanticcms.core.servlet.View;
import java.io.IOException;
import java.io.Writer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Adds the Google Analytics <a href="https://support.google.com/analytics/answer/1008080?hl=en&ref_topic=1008079#GA">Global Site Tag</a> just after head start.
 * This is applied to all {@link View views} and all {@link Page pages}, even those that are "noindex".
 */
public class GoogleAnalytics implements Component {

	/**
	 * The context parameter name that contains the tracking ID.
	 */
	public static final String TRACKING_ID_INIT_PARAM = GoogleAnalytics.class.getName() + ".trackingId";

	private final String trackingId;

	public GoogleAnalytics(String trackingId) {
		this.trackingId = trackingId;
	}

	@Override
	public void doComponent(
		ServletContext servletContext,
		HttpServletRequest request,
		HttpServletResponse response,
		Writer out,
		View view,
		Page page,
		ComponentPosition position
	) throws ServletException, IOException {
		if(position == ComponentPosition.HEAD_START) {
			// See https://rehmann.co/blog/optimize-google-analytics-google-tag-manager-via-preconnect-headers/
			out.write("<link href=\"https://www.google-analytics.com\" rel=\"dns-prefetch\" />\n"
				+ "<link href=\"https://www.google-analytics.com\" rel=\"preconnect\" crossorigin=\"anonymous\" />\n"
				// + "<!-- Global site tag (gtag.js) - Google Analytics -->\n"
				+ "<script type=\"text/javascript\" async=\"async\" src=\"https://www.googletagmanager.com/gtag/js?id=");
			URIEncoder.encodeURIComponent(
				trackingId,
				textInXhtmlAttributeEncoder,
				out
			);
			out.write("\"></script>\n"
				+ "<script type=\"text/javascript\">\n");
			MediaWriter scriptOut = new MediaWriter(javaScriptInXhtmlEncoder, out);
			scriptOut.write("  window.dataLayer = window.dataLayer || [];\n"
				+ "  function gtag(){dataLayer.push(arguments);}\n"
				+ "  gtag('js', new Date());\n"
				// + "\n"
				+ "  gtag('config', '");
			encodeTextInJavaScript(trackingId, scriptOut);
			scriptOut.write("');\n");
			out.write("</script>");
		}
	}
}
