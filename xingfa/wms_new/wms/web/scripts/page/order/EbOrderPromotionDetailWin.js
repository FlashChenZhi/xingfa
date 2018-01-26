var EbOrderPromotionDetailWin = function (id) {
    var promotionId = id;

    this.init = function (id) {
        promotionId = id;
        this.orderDetailStore.load();
    };

    this.orderDetailStore = Ext.create("Ext.data.JsonStore", {
        fields: ['skuCode','skuName','qty'],
        scope: this,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = {'promotionId': promotionId};
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/promotion/searchDetail",
            actionMethods: {read: "POST"},
            reader: {
                type: "json",
                rootProperty: "res"
            },
            listeners: {
                exception: function (thiz, request) {
                    var result = Ext.util.JSON.decode(request.responseText);
                    Ext.error(result.msg);
                }
            }
        }
    });

    this.orderDetailStore.load();


    this.grid = Ext.create("Ext.grid.Panel", {
        store: this.orderDetailStore,
        scope: this,
        width: 1000,
        maxHeight: 500,
        columns: [
            {text: "商品代码", dataIndex: "skuCode", width: "40%"},
            {text: "商品名称", dataIndex: "skuName", width: "40%"},
            {text: "数量", dataIndex: "qty", width: "20%"},
        ]
    });

    this.win = Ext.create("Ext.window.Window", {
        bodyPadding: 5,
        resizable: false,
        modal: true,
        closeAction: "hide",
        scope: this,
        items: this.grid,
        buttons: [
            {
                text: '关闭',
                scope: this,
                handler: function () {
                    this.orderDetailStore.removeAll();
                    this.win.hide();
                }
            }
        ]
    });

};