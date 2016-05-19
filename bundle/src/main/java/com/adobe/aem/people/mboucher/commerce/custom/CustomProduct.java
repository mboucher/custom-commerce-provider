package com.adobe.aem.people.mboucher.commerce.custom;

import com.adobe.cq.commerce.api.CommerceException;
import com.adobe.cq.commerce.api.Product;
import com.adobe.cq.commerce.common.AbstractJcrProduct;
import com.day.cq.commons.inherit.ComponentInheritanceValueMap;
import com.day.cq.commons.inherit.InheritanceValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import java.util.*;

/**
 * Created by mboucher on 2016-05-16.
 */
public class CustomProduct  extends AbstractJcrProduct {

    protected Resource productDataResource;
    protected InheritanceValueMap properties;

    public CustomProduct(final Resource productReference, final Resource productData) {
        super(productReference == null ? productData : productReference);
        this.productDataResource = productData;
    }

    public String getSKU() {
        return getProperties().get("sku", String.class);
    }

    @SuppressWarnings("unchecked")
    public Iterator<String> getVariantAxes() {
        String[] axes = getProperties().get("variantAttribute.name", String[].class);
        if (axes == null || axes.length == 0) {
            try {
                Iterator<Product> variants = getVariants();
                if (variants.hasNext()) {
                    List<String> values = new ArrayList<String>();
                    while(variants.hasNext()) {
                        Product variant = variants.next();
                        if (variant.getPath().equals(this.getPath()))
                            continue;

                        Iterator<String> axesItr = variant.getVariantAxes();
                        while (axesItr.hasNext()) {
                            String axis = axesItr.next();
                            if (!values.contains(axis)) {
                                values.add(axis);
                            }
                        }
                    }
                    return values.iterator();
                }
            } catch (CommerceException x) {
                log.error("Unexpected error: ", x);
            }
        }
        return axes != null ? Arrays.asList(axes).iterator() : Collections.EMPTY_LIST.iterator();
    }

    public Product getPIMProduct() {
        return productDataResource.adaptTo(Product.class);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public <AdapterType> AdapterType adaptTo(Class<AdapterType> type) {
        if (type == Resource.class) {
            return (AdapterType)resource;
        } else if (type == ValueMap.class || type == InheritanceValueMap.class) {
            return (AdapterType)getProperties();
        }

        AdapterType ret = super.adaptTo(type);
        if (ret == null) {
            ret = resource.adaptTo(type);
        }

        return ret;
    }

    protected InheritanceValueMap getProperties() {
        if (properties == null) {
            properties = new ComponentInheritanceValueMap(productDataResource);
        }

        return properties;
    }

    /**
     * Set properties map manually, e.g. for inline loading from cart.
     * @param properties The values
     */
    protected void setProperties(ValueMap properties) {
        this.properties = new ComponentInheritanceValueMap(properties);
    }

}
