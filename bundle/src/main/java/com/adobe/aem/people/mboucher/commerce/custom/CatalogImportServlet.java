/*************************************************************************
 *
 * ADOBE CONFIDENTIAL
 * __________________
 *
 *  Copyright 2011 Adobe Systems Incorporated
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Adobe Systems Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Adobe Systems Incorporated and its
 * suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Adobe Systems Incorporated.
 **************************************************************************/
package com.adobe.aem.people.mboucher.commerce.custom;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Imports a product catalog including categories below
 * the resource that is provided as request suffix.
 */
@SlingServlet(
    paths = { "/libs/commerce/products" },
    methods = { "POST" }
)
public class CatalogImportServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = -2569450911827743980L;
    
    private static final String DEFAULT_PRODUCTS_PATH = "/etc/commerce/products";

    @Reference
    private CustomImporter importer;

    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        doProducts(request, response);
    }

    /**
     * @deprecated this method is deprecated, use /libs/commerce/products
     * ({@link com.adobe.cq.commerce.pim.impl.ProductDataServlet}) instead.
     * @param request
     * @param response
     * @throws IOException
     */
    @Deprecated
    private void doProducts(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        String baseStore = request.getParameter("store");
        String catalog = request.getParameter("catalog");
        String targetPath = request.getParameter("path");
        String language = request.getParameter("language");

        if(targetPath == null) {
            targetPath = DEFAULT_PRODUCTS_PATH;
        }
        if(baseStore != null && catalog != null) {
            ResourceResolver resolver = request.getResourceResolver();
            Resource target = resolver.getResource(targetPath);

            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/plain");

            response.getWriter().println("Warning: using deprecated import servlet. Use /libs/commerce/products (ProductDataServlet) instead.\n");
            importer.importCatalog(target, baseStore, catalog, language, response.getWriter());
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
