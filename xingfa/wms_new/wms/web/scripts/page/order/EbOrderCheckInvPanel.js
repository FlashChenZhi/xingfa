var EbOrderCheckInvPanel = function (data) {

    this.source = new CommonSource(data);

    this.source.com_logicZoneCode.allowBlank = false;

    this.form = Ext.create('Ext.form.Panel', {
        title: '拣选区预分配订单库存检查',
        layout: 'column',
        scope: this,
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.com_logicZoneCode
                ]
            },
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.com_flatOnly
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
                    mask.show();
                    this.store.load({
                        callback: function (record, options, success) {
                            mask.hide();
                            if (record.length == 0) {
                                Ext.success('库存充足');
                            }
                        }
                    });

                }
            }
        ]
    });

    this.getDockedItems = function () {
        var dockedItems = [
            {
                xtype: "toolbar",
                dock: "top",
                scope: this,
                items: [
                    {
                        xtype: "button",
                        text: "补货",
                        scope: this,
                        handler: function () {
                            var form = this.form.getForm();
                            var store = this.store;
                            if (form.isValid()) {
                                Ext.Msg.show({
                                    title: '确认',
                                    message: '确认补货？',
                                    buttons: Ext.Msg.YESNO,
                                    icon: Ext.Msg.QUESTION,
                                    scope: this,
                                    fn: function (btn) {
                                        if (btn == 'yes') {
                                            var logicZoneCode = this.source.com_logicZoneCode.getValue();
                                            if (logicZoneCode == null) {
                                                Ext.warn('请选择作业区');
                                                return;
                                            }
                                            var results = [];
                                            var sels = this.grid.getSelection();
                                            for (var res in sels) {
                                                results.push({
                                                    skuCode: sels[res].data.skuCode,
                                                    skuName: sels[res].data.skuName,
                                                    qty: sels[res].data.qty,
                                                });
                                            }
                                            if (sels.length == 0) {
                                                Ext.warn('请至少选择一条数据');
                                                return;
                                            }


                                            mask.show();
                                            Ext.Ajax.request({
                                                url: 'wms/eborderCheckInv/createReplenish?logicZoneCode=' + logicZoneCode,
                                                headers: {'Content-Type': "application/json; charset=utf-8"},
                                                method: 'POST',
                                                params: Ext.util.JSON.encode(results),
                                                scope: this,
                                                timeout: 120000,
                                                success: function (response, options) {
                                                    mask.hide();
                                                    var result = Ext.util.JSON.decode(response.responseText);
                                                    if (result.success) {
                                                        Ext.success(result.msg);
                                                        this.store.loadPage(1);
                                                    } else {
                                                        Ext.error(result.msg);
                                                    }
                                                },
                                                failure: function (response, options) {
                                                    mask.hide();
                                                    var result = Ext.util.JSON.decode(response.responseText);
                                                    Ext.error(result.msg);
                                                }
                                            });
                                        } else {

                                        }
                                    }
                                });
                            }
                        }
                    },
                ]
            }
        ];
        return dockedItems;
    };

    this.store = Ext.create("Ext.data.JsonStore", {
        fields: ['skuCode', 'skuName', 'qty'],
        scope: this,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/eborderCheckInv/search",
            timeout: 960000,
            actionMethods: {read: "POST"},
            reader: {
                type: "json",
                rootProperty: "res",
                totalProperty: "count"
            },
            listeners: {
                exception: function (thiz, request) {
                    mask.hide();
                    extStoreLoadFail(thiz, request);

                }
            }
        }
    });

    this.sm = Ext.create("Ext.selection.CheckboxModel", {
        checkOnly: true
    });

    this.grid = Ext.create("Ext.grid.Panel", {
        selModel: this.sm,
        store: this.store,
        scope: this,
        columns: [
            {text: "商品代码", dataIndex: "skuCode", width: "20%"},
            {text: "商品名称", dataIndex: "skuName", width: "20%"},
            {text: "缺货数量", dataIndex: "qty", width: "20%"},
        ],
        viewConfig: {
            enableTextSelection: true
        },
        dockedItems: this.getDockedItems()
    });

    this.panel = Ext.create("Ext.panel.Panel", {
        tabConfig: {
            tooltip: '拣选区预分配订单库存检查'
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