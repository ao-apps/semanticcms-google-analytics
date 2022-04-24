/*
 * semanticcms-google-analytics - Includes the Google Analytics tracking code in SemanticCMS pages.
 * Copyright (C) 2016, 2017, 2019, 2020, 2021, 2022  AO Industries, Inc.
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
 * along with semanticcms-google-analytics.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.semanticcms.googleanalytics;

import com.aoapps.encoding.Doctype;
import com.aoapps.html.servlet.DocumentEE;
import com.aoapps.lang.Strings;
import com.semanticcms.core.model.Page;
import com.semanticcms.core.renderer.html.Component;
import com.semanticcms.core.renderer.html.ComponentPosition;
import com.semanticcms.core.renderer.html.HtmlRenderer;
import com.semanticcms.core.renderer.html.View;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * For {@link Doctype#HTML5}, adds the modern Google Analytics <a href="https://support.google.com/analytics/answer/1008080?hl=en&amp;ref_topic=1008079#GA">Global Site Tag</a>.
 * For {@linkplain Doctype other doctypes}, adds an older-style Google Analytics <a href="https://developers.google.com/analytics/devguides/collection/analyticsjs">analytics.js tracking script</a>.
 * Both are added at {@link ComponentPosition#HEAD_START}.
 * This is applied to all {@link View views} and all {@link Page pages}, even those that are "noindex".
 *
 * @see com.aoapps.html.util.GoogleAnalytics#writeGlobalSiteTag(com.aoapps.html.any.AnyUnion_Metadata_Phrasing, java.lang.String)
 * @see com.aoapps.html.util.GoogleAnalytics#writeAnalyticsJs(com.aoapps.html.any.AnyScriptSupportingContent, java.lang.String)
 */
public class GoogleAnalytics implements Component {

  /**
   * The context parameter name that contains the tracking ID.
   */
  public static final String TRACKING_ID_INIT_PARAM = GoogleAnalytics.class.getName() + ".trackingId";

  @WebListener("Registers the GoogleAnalytics component in HtmlRenderer.")
  public static class Initializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
      ServletContext servletContext = event.getServletContext();
      String trackingId = Strings.trimNullIfEmpty(servletContext.getInitParameter(GoogleAnalytics.TRACKING_ID_INIT_PARAM));
      if (trackingId != null) {
        HtmlRenderer.getInstance(servletContext).addComponent(new GoogleAnalytics(trackingId));
      }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
      // Do nothing
    }
  }

  private final String trackingId;

  private GoogleAnalytics(String trackingId) {
    this.trackingId = trackingId;
  }

  @Override
  public void doComponent(
      ServletContext servletContext,
      HttpServletRequest request,
      HttpServletResponse response,
      DocumentEE document,
      View view,
      Page page,
      ComponentPosition position
  ) throws ServletException, IOException {
    if (position == ComponentPosition.HEAD_START) {
      if (document.encodingContext.getDoctype() == Doctype.HTML5) {
        com.aoapps.html.util.GoogleAnalytics.writeGlobalSiteTag(document, trackingId);
      } else {
        com.aoapps.html.util.GoogleAnalytics.writeAnalyticsJs(document, trackingId);
      }
    }
  }
}
