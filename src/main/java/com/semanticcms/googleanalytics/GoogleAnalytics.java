/*
 * semanticcms-google-analytics - Includes the Google Analytics tracking code in SemanticCMS pages.
 * Copyright (C) 2016, 2017, 2019, 2020  AO Industries, Inc.
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

import com.aoindustries.encoding.Doctype;
import com.aoindustries.html.Html;
import com.semanticcms.core.model.Page;
import com.semanticcms.core.renderer.html.Component;
import com.semanticcms.core.renderer.html.ComponentPosition;
import com.semanticcms.core.renderer.html.View;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * For {@link Doctype#HTML5}, adds the modern Google Analytics <a href="https://support.google.com/analytics/answer/1008080?hl=en&ref_topic=1008079#GA">Global Site Tag</a>.
 * For {@linkplain Doctype other doctypes}, adds an older-style Google Analytics <a href="https://developers.google.com/analytics/devguides/collection/analyticsjs">analytics.js tracking script</a>.
 * Both are added at {@link ComponentPosition#HEAD_START}.
 * This is applied to all {@link View views} and all {@link Page pages}, even those that are "noindex".
 *
 * @see com.aoindustries.html.util.GoogleAnalytics#writeGlobalSiteTag(com.aoindustries.html.Html, java.lang.String)
 * @see com.aoindustries.html.util.GoogleAnalytics#writeAnalyticsJs(com.aoindustries.html.Html, java.lang.String)
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
		Html html,
		View view,
		Page page,
		ComponentPosition position
	) throws ServletException, IOException {
		if(position == ComponentPosition.HEAD_START) {
			if(html.doctype == Doctype.HTML5) {
				com.aoindustries.html.util.GoogleAnalytics.writeGlobalSiteTag(html, trackingId);
			} else {
				com.aoindustries.html.util.GoogleAnalytics.writeAnalyticsJs(html, trackingId);
			}
		}
	}
}
