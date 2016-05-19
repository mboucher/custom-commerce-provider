package com.adobe.aem.people.mboucher.commerce.custom.impl;

import com.adobe.aem.people.mboucher.commerce.custom.CustomConfiguration;
import com.adobe.aem.people.mboucher.commerce.custom.CustomService;

import com.adobe.cq.commerce.api.CommerceConstants;
import com.adobe.cq.commerce.api.CommerceException;
import com.adobe.cq.commerce.api.CommerceSession;
import com.adobe.cq.commerce.api.Product;
import com.adobe.cq.commerce.common.AbstractJcrCommerceService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.List;

/**
 * Created by mboucher on 2016-05-16.
 */
public class CustomServiceImpl extends AbstractJcrCommerceService implements CustomService {
    //protected  My connection;
    protected CustomConfiguration options;
    protected ResourceResolver resolver;

    protected Resource baseStore;
    protected  String baseStoreId;

    protected static boolean pinging = false;
    protected static boolean online;
    protected static long lastPing;
    protected String language = "en";

    public CustomServiceImpl(Resource resource, CustomConfiguration config) {
        this(resource.getResourceResolver(),
                resource,
                "id",
                "en" ,
                config);
    }

    public CustomServiceImpl(ResourceResolver resolver, Resource baseResource, String baseStoreId, String lang, CustomConfiguration config) {
        super(config);
        this.options = config;
        this.resolver = resolver;
        //this.connection = config.connection;
        this.baseStore = baseResource;
        //this.baseStoreId = baseStoreId == null ? config.baseStore : baseStoreId;
        this.language = lang;
    }

    public boolean isAvailable(String serviceType) {
        if (CommerceConstants.SERVICE_COMMERCE.equals(serviceType)) {
            if (pinging || (System.currentTimeMillis() - lastPing) / 60000 < 80000) {
                return online;
            } else {
                pinging = true;
                //dellOnline = pingDell(connection, new HybrisConnection.ConnectionOptions(baseStoreId, context));
                online = true;
                lastPing = System.currentTimeMillis();
                pinging = false;
            }
            return online;
        } else {
            return false;
        }
    }

    public CommerceSession login(SlingHttpServletRequest request, SlingHttpServletResponse response) throws CommerceException {
        return null; //should return a sesion object
    }


    public List<String> getCountries() throws CommerceException {
        return null;
    }

    public String getBaseStore() {
        return null;  //Should return a store id
    }

    public List<String> getOrderPredicates() throws CommerceException {
        return null;
    }

    public Product getProductByOrderCode(final String productId) throws CommerceException {
        return null;
    }

    public String getLanguage() {
        return language;
    }

    public List<String> getCreditCardTypes() throws CommerceException {
        return null;
    }

    public Product getProduct(final String path) throws CommerceException {
        return null;
    }

    public boolean checkConnectException(CommerceException x, boolean rethrow) throws CommerceException {
        return false;
    }
}
