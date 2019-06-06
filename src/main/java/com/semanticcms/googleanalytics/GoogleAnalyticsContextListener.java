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

import com.semanticcms.core.servlet.SemanticCMS;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener("Registers the GoogleAnalytics component in SemanticCMS.")
public class GoogleAnalyticsContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext servletContext = event.getServletContext();
		String trackingId = servletContext.getInitParameter(GoogleAnalytics.TRACKING_ID_INIT_PARAM);
		if(trackingId != null) {
			trackingId = trackingId.trim();
			if(!trackingId.isEmpty()) {
				SemanticCMS.getInstance(servletContext).addComponent(new GoogleAnalytics(trackingId));
			}
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// Do nothing
	}
}
