var EbOrderPromotionPanel = function (data) {

        this.source = new CommonSource(data);

        this.form = Ext.create('Ext.form.Panel', {
            title: '订单活动查询',
            layout: 'column',
            scope: this,
            items: [
                {
                    layout: "form", columnWidth: .5, defaults: {layout: "form"},
                    items: [
                        this.source.txt_promotionName
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
                        this.store.load();

                    }
                }
            ]
        });

        var matchType = function () {
            var map = new Map();
            map.put(true, '验证数量');
            map.put(false, '不验证数量');
            return map;
        };


        this.store = Ext.create("Ext.data.JsonStore", {
            fields: ['id', 'name','matchFlag', 'logicZoneCode', 'createUserId', 'createDate'],
            scope: this,
            pageSize: 20,
            listeners: {
                scope: this,
                beforeload: function (store) {
                    store.proxy.extraParams = this.form.getValues();
                }
            },
            proxy: {
                type: "ajax",
                url: "wms/promotion/search",
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

        this.grid = Ext.create("Ext.grid.Panel", {
            selModel: this.sm,
            store: this.store,
            scope: this,
            columns: [
                {text: "id", dataIndex: "id", width: "20%"},
                {text: "姓名", dataIndex: "name", width: "20%"},
                {text: "作业区", dataIndex: "logicZoneCode", width: "20%"},
                {
                    text: "匹配模式", dataIndex: "matchFlag", width: "20%", scope: this, renderer: function (value) {
                    return matchType.get(value);
                }
                },
                {text: "创建人", dataIndex: "createUserId", width: "20%"},
                {text: "创建时间", dataIndex: "createDate", width: "20%"},

            ], listeners: {
                scope: this,
                rowdblclick: function (thiz, record, tr, rowIndex, e, eOpts) {
                },
                rowcontextmenu: function (thiz, record, tr, rowIndex, e, eOpts) {
                    e.preventDefault();
                    if (rowIndex < 0) {
                        return;
                    }
                    var menu = Ext.create("Ext.menu.Menu", {
                            items: [
                                {
                                    text: "明细", handler: function () {
                                    var orderId = record.data.id;
                                    this.orderDetailWin = new EbOrderPromotionDetailWin(orderId);
                                    this.orderDetailWin.win.show();
                                }
                                }, {
                                    text: "删除", handler: function () {
                                        Ext.Msg.show({
                                            title: '确认',
                                            message: '确认删除？',
                                            buttons: Ext.Msg.YESNO,
                                            icon: Ext.Msg.QUESTION,
                                            scope: this,
                                            fn: function (btn) {
                                                if (btn === 'yes') {

                                                    var orderId = record.data.id;

                                                    mask.show();
                                                    Ext.Ajax.request({
                                                        url: 'wms/promotion/delete?id=' + orderId,
                                                        headers: {'Content-Type': "application/json; charset=utf-8"},
                                                        method: 'POST',
                                                        scope: this,
                                                        success: function (response, options) {
                                                            mask.hide();
                                                            var result = Ext.util.JSON.decode(response.responseText);
                                                            if (result.success) {
                                                                Ext.success(result.msg);
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
                                            }
                                        });
                                    }
                                }
                            ]
                        })
                        ;
                    menu.showAt(e.getPoint());
                }
            },
            dockedItems: [
                {
                    xtype: "pagingtoolbar",
                    store: this.store,
                    dock: "bottom",
                    displayInfo: true
                }
            ],
            viewConfig: {
                enableTextSelection: true
            }
        })
        ;

        this.panel = Ext.create("Ext.panel.Panel", {
            tabConfig: {
                tooltip: '订单活动查询'
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

    }
    ;