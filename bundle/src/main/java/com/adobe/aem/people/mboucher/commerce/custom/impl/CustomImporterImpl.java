package com.adobe.aem.people.mboucher.commerce.custom.impl;

import com.adobe.aem.people.mboucher.commerce.custom.CustomImporter;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.jcr.api.SlingRepository;

import java.io.PrintWriter;


/**
 * One implementation of the {@link MyImporter}. Note that
 * the repository is injected, not retrieved.
 */
@Component(metatype = true, label = "Day CQ Commerce Sample Catalog Importer")
@Service
@Properties(value = {
        @Property(name = "commerceProvider", value = "custom-provider", propertyPrivate = true)
})
public class CustomImporterImpl implements CustomImporter{
    @Reference
    private SlingRepository repository;

    public void importCatalog(Resource base, String baseStore, String catalog, String language, PrintWriter writer) {
        // Do Something.
    }
}
