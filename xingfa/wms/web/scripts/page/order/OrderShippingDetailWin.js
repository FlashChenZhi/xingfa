var OrderShippingDetailWin = function (sendCarOrderNo) {
    var sendCarOrderNo = sendCarOrderNo;

    this.init = function (sendCarOrderNo) {
        this.sendCarOrderNo = sendCarOrderNo;
        this.orderDetailStore.load();
    };
    this.orderDetailStore = Ext.create("Ext.data.JsonStore", {
        fields: ['orderNo', 'totalQty', 'allocateQty', 'pickedQty', 'sortedQty', 'mergedQty', 'checkedQty'],
        scope: this,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = {'sendCarOrderNo': sendCarOrderNo};
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/batchRetrievalResult/searchSendCarDetail",
            actionMethods: {read: "POST"},
            timeout: 120000,
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
        width: 800,
        maxHeight: 500,
        columns: [
            {text: "订单号", dataIndex: "orderNo", width: "40%"},
            {text: "总数", dataIndex: "totalQty", width: "10%"},
            {text: "已分配数", dataIndex: "allocateQty", width: "10%", scope: this, renderer: function (value, cellmeta, record, rowIndex, columnIndex, stroe) {
                var totalQty = record.data.totalQty;
                return getCellStyle(totalQty, value);
            }},
            {text: "已拣选数", dataIndex: "pickedQty", width: "10%", scope: this, renderer: function (value, cellmeta, record, rowIndex, columnIndex, stroe) {
                var totalQty = record.data.totalQty;
                return getCellStyle(totalQty, value);
            }},
            {text: "已分拣数", dataIndex: "sortedQty", width: "10%", scope: this, renderer: function (value, cellmeta, record, rowIndex, columnIndex, stroe) {
                var totalQty = record.data.totalQty;
                return getCellStyle(totalQty, value);
            }},
            {text: "已集货数", dataIndex: "mergedQty", width: "10%", scope: this, renderer: function (value, cellmeta, record, rowIndex, columnIndex, stroe) {
                var totalQty = record.data.totalQty;
                return getCellStyle(totalQty, value);
            }},
            {text: "已复核数", dataIndex: "checkedQty", width: "10%", flex:1, scope: this, renderer: function (value, cellmeta, record, rowIndex, columnIndex, stroe) {
                var totalQty = record.data.totalQty;
                return getCellStyle(totalQty, value);
            }}
        ],
        viewConfig: {
            enableTextSelection: true
        }
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

    function getCellStyle(planQty, currentQty){
        if (planQty == currentQty) {
            return '<span style="color:green;">' + currentQty + '</span>';
        } else {
            return '<span style="color:red;">' + currentQty + '</span>';
        }
    }
};