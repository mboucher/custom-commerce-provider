package com.adobe.aem.people.mboucher.commerce.custom;

import com.adobe.cq.commerce.common.AbstractJcrCommerceServiceFactory;
import com.adobe.cq.commerce.common.ServiceContext;

/**
 * Created by mboucher on 2016-05-16.
 */
public class CustomConfiguration extends ServiceContext {
    public CustomConfiguration(AbstractJcrCommerceServiceFactory serviceFactory) {
        super(serviceFactory);
    }
}
