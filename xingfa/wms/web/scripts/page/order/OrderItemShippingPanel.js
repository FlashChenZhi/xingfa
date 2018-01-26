var OrderItemShippingPanel = function (data) {

    this.source = new CommonSource(data);
    this.source.txt_batchNo.allowBlank = false;

    this.form = Ext.create("Ext.form.Panel", {
        title: '波次商品发货汇总',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.txt_batchNo,
                    this.source.com_skuCode
                ]
            },
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.txt_sendCarOrderNo
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
        fields: ['lineNo','itemCode', 'itemName', 'planQty','itemEom'],
        pageSize: 20,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/batchRetrievalResult/searchSku",
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


    this.grid = Ext.create("Ext.grid.Panel", {
            store: this.store,
            scope: this,
            features: [
                {
                    ftype: 'summary'
                }
            ],
            columns: [
                {text: "行号", dataIndex: "lineNo", width: "10%"},
                {text: "商品代码", dataIndex: "itemCode", width: "20%"},
                {text: "商品名称", dataIndex: "itemName", width: "30%"},
                {text: "发货数量", dataIndex: "planQty", width: "20%"},
                {text: "发货单位", dataIndex: "itemEom", width: "20%",flex:1}
            ],
            selType: 'cellmodel',
            viewConfig: {
                enableTextSelection: true
            }
        }
    );


    this.panel = Ext.create("Ext.panel.Panel", {
        tabConfig: {
            tooltip: '波次商品发货汇总'
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

};