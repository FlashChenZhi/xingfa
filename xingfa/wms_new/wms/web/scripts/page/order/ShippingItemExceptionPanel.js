var ShippingItemExceptionPanel = function (data) {

    this.source = new CommonSource(data);
    this.source.txt_batchNo.allowBlank = false;

    this.form = Ext.create("Ext.form.Panel", {
        title: '订单缺货查询',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.txt_batchNo
                ]
            }, {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.txt_skuCode
                ]
            }
        ],
        buttons: [
            {
                text: '重置',
                handler: function () {
                    this.up('form').getForm().reset();
                }
            },
            {
                text: '查询',
                scope: this,
                formBind: true,
                handler: function () {
                    var form = this.form.getForm();
                    if (form.isValid()) {
                        this.store.loadPage(1);
                    }
                }
            }
        ]
    });

    this.store = Ext.create("Ext.data.JsonStore", {
        scope: this,
        fields: ['lineNo', 'itemCode', 'itemName', 'planQty', 'itemEom', 'inventoryQty', 'exceptionQty', 'hasInventoryFlag', 'wareHouseCode'],
        pageSize: 20,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/batchRetrievalResult/searchExceptionSku",
            timeout: 960000,
            actionMethods: {read: "POST"},
            reader: {
                type: "json",
                rootProperty: "res",
                totalProperty: "count"
            },
            listeners: {
                exception: function (thiz, request) {
                    extStoreLoadFail(thiz, request);

                }
            }
        }
    });

    var boolMap = new Map();
    boolMap.put(true, '有库存');
    boolMap.put(false, '缺库存');

    this.grid = Ext.create("Ext.grid.Panel", {
            store: this.store,
            scope: this,
            features: [
                {
                    ftype: 'summary'
                }
            ],
            columns: [
                {text: "行号", dataIndex: "lineNo", width: "5%"},
                {text: "仓库代码", dataIndex: "wareHouseCode", width: "10%"},
                {text: "商品代码", dataIndex: "itemCode", width: "10%"},
                {text: "商品名称", dataIndex: "itemName", width: "20%"},
                {text: "发货数量", dataIndex: "planQty", width: "10%"},
                {text: "发货单位", dataIndex: "itemEom", width: "15%"},
                {
                    text: "分配数量", dataIndex: "inventoryQty", width: "10%",
                    renderer: function (value, cellmeta, record, rowIndex, columnIndex, stroe) {
                        var totalQty = record.data.planQty;
                        return getCellStyle(totalQty, value);
                    }
                },
                {
                    text: "缺货数量", dataIndex: "exceptionQty", width: "10%",
                    renderer: function (value) {
                        return getExceptionStyle(value);
                    }
                },
                {
                    text: "是否缺货", dataIndex: "hasInventoryFlag", width: "10%", renderer: function (value) {
                    return boolMap.get(value);
                }, flex: 1
                }
            ],
            selType: 'cellmodel',
            viewConfig: {
                enableTextSelection: true
            }, dockedItems: [
                {
                    xtype: "pagingtoolbar",
                    store: this.store,
                    dock: "bottom",
                    displayInfo: true
                }
            ]
        }
    );


    this.panel = Ext.create("Ext.panel.Panel", {
        tabConfig: {
            tooltip: '订单缺货查询'
        },
        overflowY: 'auto',
        items: [
            this.form, this.grid
        ]
    });

    function getCellStyle(planQty, currentQty) {
        if (planQty == currentQty) {
            return '<span style="color:green;">' + currentQty + '</span>';
        } else {
            return '<span style="color:red;">' + currentQty + '</span>';
        }
    }

    function getExceptionStyle(exceptionQty) {
        if (exceptionQty == 0) {
            return '<span style="color:green;">' + exceptionQty + '</span>';
        } else {
            return '<span style="color:red;">' + exceptionQty + '</span>';
        }
    }

};