package com.adobe.aem.people.mboucher.commerce.custom;

import org.apache.sling.api.resource.Resource;

import java.io.PrintWriter;

/**
 * Created by mboucher on 2016-05-16.
 */
public interface CustomImporter {
    /**
     * @return the name of the underlying JCR repository implementation
     */
    /**
     * Imports a catalog to a certain base resource.
     * @param base      The base resource
     * @param baseStore The base store to import from
     * @param catalog   The catalog to import
     * @param language  The language ISO code or <code>null</code> to choose language from base resource
     * @param writer    The log writer
     */
    void importCatalog(Resource base, String baseStore, String catalog, String language, PrintWriter writer);

}
