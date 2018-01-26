var EbCancelConfirmPanel = function (data) {
    this.source = new CommonSource(data);

    this.form = Ext.create("Ext.form.Panel", {
        title: '电商取消确认',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.com_dsCarrierCode
                ]
            },
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.txt_expressNo
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
                handler: function () {
                    this.store.loadPage(1);
                }
            }
        ]
    });

    this.store = Ext.create("Ext.data.JsonStore", {
        scope: this,
        fields: ['locationNo', 'containerBarcode', 'skuCode', 'skuName', 'qty', 'eom'],
        pageSize: 20,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/ebCancelConfirm/list",
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

    this.confirmBtn = Ext.create("Ext.button.Button", {
        text: "取消确认",
        scope: this,
        handler: function () {
            Ext.Msg.show({
                title: '确认',
                message: '确认实物和账务一致,取消完成该张订单？',
                buttons: Ext.Msg.YESNO,
                icon: Ext.Msg.QUESTION,
                scope: this,
                fn: function (btn) {
                    if (btn === 'yes') {
                        var carrierCode = this.source.com_dsCarrierCode.getValue();
                        if (carrierCode == null || carrierCode == '') {
                            Ext.warn('承运商不能为空');
                            return;
                        }

                        var expressNo = this.source.txt_expressNo.getValue();
                        if (expressNo == null || expressNo == '') {
                            Ext.warn('运单号不能为空');
                            return;
                        }

                        mask.show();
                        Ext.Ajax.request({
                            url: 'wms/ebCancelConfirm/cancelConfirm?carrierCode=' + carrierCode + '&expressNo='+expressNo,
                            headers: {'Content-Type': "application/json; charset=utf-8"},
                            method: 'POST',
                            scope: this,
                            success: function (response, options) {
                                mask.hide();
                                var result = Ext.util.JSON.decode(response.responseText);
                                if (result.success) {
                                    Ext.success(result.msg);
                                    this.store.removeAll();
                                    this.source.txt_expressNo.reset();
                                    this.source.txt_expressNo.focus();
                                } else {
                                    Ext.error(result.msg);
                                }
                            },
                            failure: function (response, options) {
                                mask.hide();
                                extAjaxFail(response, options);
                            }
                        });

                    }
                    else {
                        //todo cancel
                    }
                }
            });
        }
    });

    this.grid = Ext.create("Ext.grid.Panel", {
            store: this.store,
            scope: this,
            columns: [
                {text: "货位", dataIndex: "locationNo", width: "20%"},
                {text: "容器条码", dataIndex: "containerBarcode", width: "20%"},
                {text: "商品代码", dataIndex: "skuCode", width: "20%"},
                {text: "商品名称", dataIndex: "skuName", width: "20%"},
                {text: "库存数量", dataIndex: "qty", width: "10%"},
                {text: "单位", dataIndex: "eom", width: "10%"}
            ],
            viewConfig: {
                enableTextSelection: true
            },
            selType: 'cellmodel',
            dockedItems: [
                {
                    xtype: "pagingtoolbar",
                    store: this.store,
                    dock: "bottom",
                    displayInfo: true
                },{
                    xtype: "toolbar",
                    dock: "top",
                    scope: this,
                    items: [
                        this.confirmBtn
                    ]
                }
            ]
        }
    );

    this.source.txt_expressNo.on('specialKey', function (field, e) {
        if (e.getKey() == e.ENTER) {
            this.store.loadPage(1);
        }
    }, this);

    this.panel = Ext.create("Ext.panel.Panel", {
        tabConfig: {
            tooltip: '电商取消确认'
        },
        overflowY: 'auto',
        items: [
            this.form, this.grid
        ]
    });

    var mask = new Ext.LoadMask({
        msg: '系统处理中...',
        target: this.panel
    });
};