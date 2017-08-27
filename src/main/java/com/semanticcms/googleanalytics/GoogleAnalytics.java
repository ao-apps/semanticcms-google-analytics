/*
 * semanticcms-google-analytics - Includes the Google Analytics tracking code in SemanticCMS pages.
 * Copyright (C) 2016, 2017  AO Industries, Inc.
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

import com.aoindustries.encoding.TextInJavaScriptEncoder;
import com.semanticcms.core.model.Page;
import com.semanticcms.core.renderer.html.Component;
import com.semanticcms.core.renderer.html.ComponentPosition;
import com.semanticcms.core.renderer.html.View;
import java.io.IOException;
import java.io.Writer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Adds the Google Analytics tracking code script just before head end.
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
		if(position == ComponentPosition.HEAD_END) {
			out.write("<script type=\"text/javascript\">\n"
				+ "// <![CDATA[\n"
				+ "(function(i,s,o,g,r,a,m){i[\"GoogleAnalyticsObject\"]=r;i[r]=i[r]||function(){\n"
				+ "(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),\n"
				+ "m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)\n"
				+ "})(window,document,\"script\",\"https://www.google-analytics.com/analytics.js\",\"ga\");\n"
				+ "ga(\"create\",\"");
			TextInJavaScriptEncoder.encodeTextInJavaScript(trackingId, out);
			out.write("\",\"auto\");\n"
				+ "ga(\"send\",\"pageview\");\n"
				+ "// ]]>\n"
				+ "</script>");
		}
	}
}
