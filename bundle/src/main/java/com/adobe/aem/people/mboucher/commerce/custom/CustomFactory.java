package com.adobe.aem.people.mboucher.commerce.custom;

import com.adobe.cq.commerce.api.CommerceServiceFactory;
import com.adobe.cq.commerce.api.Product;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;

/**
 * Created by mboucher on 2016-05-16.
 */
public interface CustomFactory extends CommerceServiceFactory {

    /**
     * Get the current configuration options.
     */
    CustomConfiguration getServiceContext();

    /**
     * Get a {@link CustomService} implementation for a resource.
     */
    CustomService getCommerceService(Resource resource);

    /**
     * Get a {@link Product} implementation from a resource.
     * @param resource the resource containing the product data, or a reference to it.
     * @return a {@link Product} instance, or {@code null} if the resource did not contain the necessary information.
     */
    Product getProduct(Resource resource);


    /**
     * Get a {@link CustomSession} for a request. {@link CustomService#login} implementations should call through this
     * method, but that is not a requirement.
     *
     * @param service the Hybris service
     * @param request the request
     * @param response the response
     * @param sessionInfo the session info
     * @return the Hybris session object
     */
    CustomSession getSession(CustomService service, SlingHttpServletRequest request, SlingHttpServletResponse response, CustomSessionInfo sessionInfo);

}
