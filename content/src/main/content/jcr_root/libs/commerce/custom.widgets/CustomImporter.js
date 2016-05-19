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
 *************************************************************************
 */
CQ.commerce = CQ.commerce || {};

/**
 * @class CQ.commerce.CustomImporter
 * @extends CQ.Ext.Viewport
 * The importer enables the user to import product catalogs from a remote Custom system.
 * @constructor
 * Creates a new importer.
 * @param {Object} config The config object
 */
CQ.commerce.CustomImporter = CQ.Ext.extend(CQ.Ext.Viewport, {
    constructor : function(config) {
        this.results = document.createElement("iframe");
        this.results.id = "results_cq-customimporter";
        this.results.name = "results_cq-customimporter";
        this.results.height = "100%";
        this.results.width = "100%";
        this.results.onload = this.onResultsLoad;
        this.results.onreadystatechange = this.onResultsLoad;

        var importer = this;
        CQ.commerce.CustomImporter.superclass.constructor.call(this, {
            "id" :"cq-customimporter",
            "layout":"border",
            "renderTo":CQ.Util.getRoot(),
            "items" : [
                {
                    "id":"cq-customimporter-wrapper",
                    "xtype":"panel",
                    "region":"center",
                    "layout":"border",
                    "border":false,
                    "items": [
                        {
                            "id":"cq-header",
                            "xtype":"container",
                            "autoEl":"div",
                            "region":"north",
                            "items": [
                                {
                                    "xtype":"panel",
                                    "border":false,
                                    "layout":"column",
                                    "cls": "cq-header-toolbar",
                                    "items": [
                                        new CQ.UserInfo({}),
                                        new CQ.HomeLink({})
                                    ]
                                }
                            ]
                        },{
                            "layout": "vbox",
                            "region": "center",
                            "items": [
                                {
                                    "xtype" :"form",
                                    "id" :"cq-customimporter-form",
                                    "title":CQ.I18n.getMessage("Custom Catalog Importer"),
//                                    "region":"center",
                                    "standardSubmit" : true,
                                    "autoScroll": true,
                                    "border":false,
                                    "margins":"5 5 5 5",
                                    "autoHeight": true,
                                    "defaults" : {
                                        "anchor" : "-54"
                                    },
                                    "style" : "background-color:white",
                                    "bodyStyle" : "padding:10px",
                                    "items" : [
                                        {
                                            "xtype" : "textfield",
                                            "fieldLabel" : CQ.I18n.getMessage("Base Store"),
                                            "fieldDescription" : CQ.I18n.getMessage("Custom base store name"),
                                            "name" : "store",
                                            "allowBlank" : false
                                        },{
                                            "xtype" : "textfield",
                                            "fieldLabel" : CQ.I18n.getMessage("Catalog"),
                                            "fieldDescription" : CQ.I18n.getMessage("Custom catalog name"),
                                            "name" : "catalog",
                                            "allowBlank" : false
                                        },{
                                            "xtype" : "textfield",
                                            "fieldLabel" : CQ.I18n.getMessage("Language code"),
                                            "fieldDescription" : CQ.I18n.getMessage("ISO language code (e.g. en), leave blank to choose language from selected page below."),
                                            "name" : "language",
                                            "allowBlank" : true
                                        },{
                                            "xtype" : "selection",
                                            "type" : "select",
                                            "fieldLabel" : CQ.I18n.getMessage("Commerce Provider"),
                                            "name" : "provider",
                                            "options" : "/libs/commerce/providers.json",
                                            "allowBlank" : false,
                                            "listeners" : {
                                                render : function(selection) {
                                                    var comboBox = selection.comboBox;
                                                    if (comboBox.store.getTotalCount() < 1) {
                                                        comboBox.setValue(CQ.I18n.getMessage("No commerce providers installed."));
                                                        comboBox.addClass(comboBox.emptyClass);
                                                        selection.el.addClass(selection.invalidClass);
                                                    } else if (!selection.getValue()) {
                                                        selection.setValue(comboBox.store.getAt(0).data.value);
                                                    }
                                                }
                                            }
                                        },{
                                            "xtype" : "pathfield",
                                            "fieldLabel" : CQ.I18n.getMessage("DAM path"),
                                            "name" : "damPath",
                                            "options" : "/libs/commerce/providers.json",
                                            "allowBlank" : true,
                                            "rootPath": "/content/dam",
                                            "rootTitle": "DAM Assets"
                                        },{
                                            "fieldLabel": CQ.I18n.getMessage("Incremental Import"),
                                            "xtype":"selection",
                                            "type":"checkbox",
                                            "name":"incrementalImport",
                                            "id":"incrementalImport",
                                            "listeners" : {
                                                "selectionchanged" : function() {
                                                    var iiCheckbox = CQ.Ext.getCmp("incrementalImport").items.items[0];
                                                    var euCheckbox = CQ.Ext.getCmp("expressUpdate").items.items[0];
                                                    if (iiCheckbox.getValue()) {
                                                        euCheckbox.setValue(false);
                                                    }
                                                }
                                            }
                                        },{
                                            "fieldLabel": CQ.I18n.getMessage("Express Update"),
                                            "xtype":"selection",
                                            "type":"checkbox",
                                            "name":"expressUpdate",
                                            "id":"expressUpdate",
                                            "listeners" : {
                                                "selectionchanged" : function() {
                                                    var iiCheckbox = CQ.Ext.getCmp("incrementalImport").items.items[0];
                                                    var euCheckbox = CQ.Ext.getCmp("expressUpdate").items.items[0];
                                                    if (euCheckbox.getValue()) {
                                                        iiCheckbox.setValue(false);
                                                    }
                                                }
                                            }
                                        },{
                                            "xtype":"hidden",
                                            "name":"_charset_",
                                            "value":"utf-8"
                                        },{
                                            "xtype":"hidden",
                                            "name":":operation",
                                            "value":"import"
                                        },{
                                            "xtype":"hidden",
                                            "name":"fallbackProvider",
                                            "value":"custom"
                                        }
                                    ],
                                    "buttonAlign":"left",
                                    "buttons":[
                                        {
                                            "id":"cq-customimporter-btn-import",
                                            "text":CQ.I18n.getMessage("Import Catalog"),
                                            "handler":function() {
                                                var form = CQ.Ext.getCmp("cq-customimporter-form").getForm();
                                                if (form.isValid()) {
                                                    var btn = CQ.Ext.getCmp("cq-customimporter-btn-import");
                                                    btn.setDisabled(true);

                                                    var log = CQ.Ext.getCmp("cq-customimporter-log");
                                                    log.expand();

                                                    // submit form
                                                    form.getEl().dom.action = CQ.HTTP.externalize(config.productsUrl);
                                                    form.getEl().dom.method = "POST";
                                                    form.getEl().dom.target = "results_cq-customimporter";
                                                    form.submit();
                                                }
                                            }
                                        },
                                        new CQ.Ext.ProgressBar({
                                            "id":"cq-customimporter-progress",
                                            "width":400,
                                            "hidden":true
                                        })
                                    ]
                                }
                            ]
                        },{
                            "xtype":"panel",
                            "id" :"cq-customimporter-log",
                            "region":"south",
                            "title":CQ.I18n.getMessage("Import Log"),
                            "margins":"-5 5 5 5",
                            "height": 300,
                            "split":true,
                            "collapsible": true,
                            "collapsed": false,
                            "items":[
                                new CQ.Ext.BoxComponent({
                                    "autoEl": {
                                        "tag": "div"
                                    },
                                    "style": {
                                        "width": "100%",
                                        "height": "100%",
                                        "margin": "-2px"
                                    },
                                    "listeners":{
                                        render:function(wrapper) {
                                            new CQ.Ext.Element(importer.results).appendTo(wrapper.getEl());
                                        }
                                    }
                                })
                            ],
                            "plugins":[
                                {
                                    init: function(p) {
                                        if (p.collapsible) {
                                            var r = p.region;
                                            if ((r == "north") || (r == "south")) {
                                                p.on("collapse", function() {
                                                    var ct = p.ownerCt;
                                                    if (ct.layout[r].collapsedEl && !p.collapsedTitleEl) {
                                                        p.collapsedTitleEl = ct.layout[r].collapsedEl.createChild ({
                                                            tag:"span",
                                                            cls:"x-panel-collapsed-text",
                                                            html:p.title
                                                        });
                                                    }
                                                }, false, {single:true});
                                            }
                                        }
                                    }
                                }
                            ]
                        }
                    ]
                }
            ]
        })
    },

    onResultsLoad: function() {
        var btnCatalog = CQ.Ext.getCmp("cq-customimporter-btn-import");
        btnCatalog.setDisabled(false);
        var btnGroups = CQ.Ext.getCmp("cq-customgroups-btn-import");
        btnGroups.setDisabled(false);
    }
});
CQ.Ext.reg("customimporter", CQ.commerce.CustomImporter);

