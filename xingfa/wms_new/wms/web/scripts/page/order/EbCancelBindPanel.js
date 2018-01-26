var EbCancelBindPanel = function (data) {

    this.source = new CommonSource(data);

    this.source.com_logicZoneCode.allowBlank = false;

    this.form = Ext.create('Ext.form.Panel', {
        title: '取消容器绑定',
        layout: 'column',
        scope: this,
        items: [
            {
                layout: "form", columnWidth: .33, defaults: {anchor: '100%'},
                items: [
                    this.source.com_logicZoneCode
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

    this.store = Ext.create("Ext.data.JsonStore", {
        fields: ['bindId', 'expressNo', 'barCode'],
        scope: this,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/cancelBind/search",
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
            {text: "ID", dataIndex: "bindId", width: "20%"},
            {text: "快递单号", dataIndex: "expressNo", width: "20%"},
            {text: "周转箱号", dataIndex: "barCode", width: "20%"},
        ],
        listeners: {
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
                            text: "取消",
                            scope: this,
                            handler: function () {
                                var rowData = record.data;
                                Ext.Msg.show({
                                    title: '确认',
                                    message: '确认取消绑定？',
                                    buttons: Ext.Msg.YESNO,
                                    icon: Ext.Msg.QUESTION,
                                    scope: this,
                                    fn: function (btn) {
                                        if (btn === 'yes') {
                                            mask.show();
                                            Ext.Ajax.request({
                                                url: 'wms/cancelBind/cancel',
                                                method: 'POST',
                                                params: {bindId: rowData.bindId},
                                                scope: this,
                                                timeout: 120000,
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
                                                    var result = Ext.util.JSON.decode(response.responseText);
                                                    Ext.error(result.msg);
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    ]
                });
                menu.showAt(e.getPoint());
            }
        }
    });

    this.panel = Ext.create("Ext.panel.Panel", {
        tabConfig: {
            tooltip: '取消容器绑定'
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