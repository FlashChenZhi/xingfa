var Zmm55OrderRetrievalPanel  = function(data){


    this.store = Ext.create("Ext.data.JsonStore", {
        scope: this,
        fields: ['orderNo'],
        pageSize: 20,
        data: {
            'res': [
                {'orderNo': '201503200001'}
            ]
        },
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: 'memory',
            reader: {
                type: 'json',
                rootProperty: 'res'
            }
        }
    });

    this.grid = Ext.create("Ext.grid.Panel", {
            store: this.store,
            scope: this,
            columns: [
                {text: "订单号", dataIndex: "orderNo",flex: 1}

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
                            {text: "开始拣选", handler: function () {

                            }},
                            {text: "发货确认", handler: function () {

                            }}
                        ]
                    });
                    menu.showAt(e.getPoint());
                }
            },
            selType: 'cellmodel',
            dockedItems: [
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
            tooltip: 'Zmm55出库'
        },
        overflowY: 'auto',
        items: [
            this.grid
        ]
    });
}