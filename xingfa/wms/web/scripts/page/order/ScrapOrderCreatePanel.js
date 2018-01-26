var ScrapOrderCreatePanel = function (data) {
    this.source = new CommonSource(data);
    this.cmp = new InventorySrouce();

    this.source.com_ownerCode.allowBlank = false;
    this.source.com_ownerCode.fieldLabel = '<font style="color: red;">*</font>' + this.source.com_ownerCode.fieldLabel;

    this.form = Ext.create("Ext.form.Panel", {
        title: '报废出库分配',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.com_ownerCode,
                    this.source.com_logicZoneCode,
                    this.source.date_productDate
                ]
            },
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.com_skuCode,
                    this.source.txt_locationNo
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
                    var form = this.form.getForm();
                    if (form.isValid()) {
                        this.store.load();
                    }
                }
            }
        ]
    });

    this.store = Ext.create("Ext.data.JsonStore", {
        scope: this,
        fields: ['inventoryId', 'logicZoneCode', 'logicZoneName', 'locationNo', 'containerBarcode', 'providerCode', 'providerName', 'ownerCode', 'ownerName', 'skuCode', 'skuName', 'qty', 'eom', 'productDate', 'qaStatus', 'frozenType', 'frozenFlag', 'reservedType', 'reservedFlag', 'returnSkuFlag', 'returnSkuType', 'serialNumber', 'memo'],
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/scrapOrderCreate/search",
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

    this.finishBtn = Ext.create("Ext.button.Button", {
        text: "报废出库",
        scope: this,
        handler: function () {
            Ext.Msg.show({
                title: '确认',
                message: '确认报废出库？',
                buttons: Ext.Msg.YESNO,
                icon: Ext.Msg.QUESTION,
                scope: this,
                fn: function (btn) {
                    if (btn === 'yes') {
                        var results = [];
                        var sels = this.grid.getSelection();
                        if (sels.length == 0) {
                            Ext.warn('请至少选择一条数据');
                            return;
                        }
                        for (var res in sels) {
                            var data = sels[res].data;
                            results.push({inventoryId: data.inventoryId});
                        }

                        var ownerCode = this.source.com_ownerCode.getValue();
                        if (ownerCode == null) {
                            Ext.warn('货主代码不能为空');
                            return;
                        }

                        mask.show();
                        Ext.Ajax.request({
                            url: 'wms/scrapOrderCreate/confirm?ownerCode=' + ownerCode,
                            headers: {'Content-Type': "application/json; charset=utf-8"},
                            method: 'POST',
                            params: Ext.util.JSON.encode(results),
                            scope: this,
                            success: function (response, options) {
                                mask.hide();
                                var result = Ext.util.JSON.decode(response.responseText);
                                if (result.success) {
                                    Ext.success(result.msg);
                                    this.store.load();
                                } else {
                                    Ext.error(result.msg);
                                }
                            },
                            failure: function (response, options) {
                                mask.hide();
                                console.log(response.responseText);
                                extAjaxFail(response, options);
                            }
                        });
                    } else {
                        //todo cancel
                    }
                }
            });
        }
    });


    var scrapTypeMap = this.cmp.scrapTypeMap();
    var qaStatusMap = this.cmp.qaStatusMap();
    var reservedTypeMap = this.cmp.reservedTypeMap();
    var yesOrNoMap = this.cmp.yesOrNoMap();
    var frozenTypeMap = this.cmp.frozenTypeMap();

    this.sm = Ext.create("Ext.selection.CheckboxModel", {
        checkOnly: true
    });

    this.grid = Ext.create("Ext.grid.Panel", {
            selModel: this.sm,
            store: this.store,
            scope: this,
            columns: [
                {text: "ID", dataIndex: "inventoryId", hidden: true},
                {
                    text: "报废类型", dataIndex: "returnSkuType", scope: this, renderer: function (value) {
                    return scrapTypeMap.get(value);
                }
                },
                {text: "作业区代码", dataIndex: "logicZoneCode"},
                {text: "作业区名称", dataIndex: "logicZoneName"},
                {text: "货位号", dataIndex: "locationNo"},
                {text: "容器号", dataIndex: "containerBarcode"},
                {text: "货主代码", dataIndex: "ownerCode"},
                {text: "货主名称", dataIndex: "ownerName"},
                {text: "商品代码", dataIndex: "skuCode"},
                {text: "商品名称", dataIndex: "skuName"},
                {text: "数量", dataIndex: "qty"},
                {text: "单位", dataIndex: "eom"},
                {text: "生产日期", dataIndex: "productDate"},
                {
                    text: "质检状态", dataIndex: "qaStatus", scope: this, renderer: function (value) {
                    return qaStatusMap.get(value);
                }
                },
                {
                    text: "冻结类型", dataIndex: "frozenType", scope: this, renderer: function (value) {
                    return frozenTypeMap.get(value);
                }
                },
                {
                    text: "是否冻结", dataIndex: "frozenFlag", scope: this, renderer: function (value) {
                    return yesOrNoMap.get(value);
                }
                },
                {
                    text: "预约类型", dataIndex: "reservedType", scope: this, renderer: function (value) {
                    return reservedTypeMap.get(value);
                }
                },
                {
                    text: "是否预约", dataIndex: "reservedFlag", scope: this, renderer: function (value) {
                    return yesOrNoMap.get(value);
                }
                },
                {text: "流水号", dataIndex: "serialNumber"},
                {text: "备注", dataIndex: "memo", flex: 1}
            ],
            listeners: {},
            selType: 'cellmodel',
            viewConfig: {
                enableTextSelection: true
            },
            dockedItems: [
                {
                    xtype: "toolbar",
                    dock: "top",
                    scope: this,
                    items: [
                        this.finishBtn
                    ]
                }
            ]
        }
    );


    this.panel = Ext.create("Ext.panel.Panel", {
        tabConfig: {
            tooltip: '报废出库分配'
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