package com.adobe.aem.people.mboucher.commerce.custom.impl;

import com.adobe.aem.people.mboucher.commerce.custom.*;
import com.adobe.cq.commerce.api.CommerceConstants;
import com.adobe.cq.commerce.api.CommerceServiceFactory;
import com.adobe.cq.commerce.api.Product;
import com.adobe.cq.commerce.common.AbstractJcrCommerceServiceFactory;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceUtil;
import org.osgi.service.component.ComponentContext;

@Component(metatype = true, label = "Day CQ Commerce Factory for My Custom Provider")
@Service(value = {CommerceServiceFactory.class, CustomFactory.class})
@Properties(value = {
        @Property(name = "service.description", value = "My Custom Provider - specific factory for commerce service"),
        @Property(name = "commerceProvider", value = "custom-provider")
})
public class CustomFactoryImpl extends AbstractJcrCommerceServiceFactory implements CustomFactory {
    protected ComponentContext context;

    @Activate
    protected void activate(ComponentContext ctx) {
        this.context = ctx;
    }


    public CustomConfiguration getServiceContext() {
        return new CustomConfiguration(this);
    }

    public CustomSession getSession(CustomService service, SlingHttpServletRequest request, SlingHttpServletResponse response, CustomSessionInfo sessionInfo) {
        return null;
    }


    public CustomService getCommerceService(Resource res) {
        return new CustomServiceImpl(res,getServiceContext());
    }

    public Product getProduct(Resource originalResource) {
        // We've either been given a path to a product reference (on a catalog page), or a
        // path to product data (in /etc/commerce/products).  Figure out which.
        //
        String dataPath = ResourceUtil.getValueMap(originalResource).get(CommerceConstants.PN_PRODUCT_DATA, String.class);



        Resource dataResource;
        Resource refResource;

        if (dataPath == null) {
            dataResource = originalResource;
            refResource = null;
        } else {
            dataResource = originalResource.getResourceResolver().getResource(dataPath);
            refResource = originalResource;
        }

        return new CustomProduct(refResource, dataResource);
    }

}
