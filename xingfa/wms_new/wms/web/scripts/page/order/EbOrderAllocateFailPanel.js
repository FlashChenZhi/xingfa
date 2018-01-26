var EbOrderAllocateFailPanel = function () {

    this.form = Ext.create('Ext.form.Panel', {
        title: '订单分配失败查询与重置',
        layout: 'column',
        scope: this,
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
                    mask.show();
                    this.store.loadPage(1);
                }
            }
        ]
    });

    this.store = Ext.create("Ext.data.JsonStore", {
        fields: ['logicZoneName', 'orderNo', 'logicZoneCode', 'skuCode', 'skuName', 'qty', 'systemQty'],
        pageSize: 20,
        scope: this,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            },
            load: function (store, obj) {
                mask.hide();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/allocateFailOrderSearch/search",
            actionMethods: {read: "POST"},
            reader: {
                type: "json",
                rootProperty: "res",
                totalProperty: "count"
            },
            timeout: 120000,
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
            {text: "订单号", dataIndex: "orderNo", width: "15%"},
            {text: "作业区代码", dataIndex: "logicZoneCode", width: "15%"},
            {text: "作业区名称", dataIndex: "logicZoneName", width: "15%"},
            {text: "商品代码", dataIndex: "skuCode", width: "15%"},
            {text: "商品名称", dataIndex: "skuName", width: "15%"},
            {text: "需要数量", dataIndex: "qty", width: "15%"},
            {text: "系统数量", dataIndex: "systemQty", flex: 1}
        ],
        viewConfig: {
            enableTextSelection: true
        }
    });

    this.panel = Ext.create("Ext.panel.Panel", {
        tabConfig: {
            tooltip: '订单分配失败查询与重置'
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