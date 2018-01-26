var EbOrderDetailWin = function (id) {
    var orderId = id;
    var keyword = null;

    this.cmp = new OrderSearchSource();

    this.init = function (id) {
        orderId = id;
        this.orderDetailStore.load();
    };

    this.isFrozenMap = function () {
        var map = new Map();
        map.put(true, '冻结');
        map.put(false, '可用');
        return map;
    };

    this.orderDetailStore = Ext.create("Ext.data.JsonStore", {
        fields: ['detailLineNo', 'cnorDocLineNo', 'itemCode', 'itemName', 'eom', 'planQty', 'allocatedQty', 'pickedQty', 'mergedQty', 'finishQty', 'packedQty'],
        scope: this,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = {'orderId': orderId, 'keyword' : keyword};
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/ebOrderSearch/searchDetail",
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
        features: [
            {
                ftype: 'summary'
            }
        ],
        tbar: [this.cmp.txt_keyword, {
            text: '搜索',
            scope: this,
            handler: function () {
                keyword = this.cmp.txt_keyword.getValue();
                this.orderDetailStore.load();
            }
        }
        ],
        columns: [
            {text: "行号", dataIndex: "detailLineNo", width: "5%"},
            {text: "商品代码", dataIndex: "itemCode", width: "10%"},
            {text: "商品名称", dataIndex: "itemName", width: "10%"},
            {text: "单位", dataIndex: "eom", width: "5%"},
            {
                text: "订单数量", dataIndex: "planQty", summaryType: 'sum', width: "10%",
                summaryRenderer: function (value, summaryData, dataIndex) {
                    return Ext.String.format('{0}{1}', '总数：', value);
                }
            },
            {
                text: "已分配数", dataIndex: "allocatedQty", scope: this, summaryType: 'sum', width: "10%",
                summaryRenderer: function (value, summaryData, dataIndex) {
                    return Ext.String.format('{0}{1}', '总分配数：', value);
                },
                renderer: function (value, cellmeta, record, rowIndex, columnIndex, stroe) {
                    var planQty = record.data.planQty;
                    return getCellStyle(planQty, value);
                }
            },
            {
                text: "已拣选数", dataIndex: "pickedQty", scope: this, summaryType: 'sum', width: "10%",
                summaryRenderer: function (value, summaryData, dataIndex) {
                    return Ext.String.format('{0}{1}', '总拣选数：', value);
                },
                renderer: function (value, cellmeta, record, rowIndex, columnIndex, stroe) {
                    var planQty = record.data.planQty;
                    return getCellStyle(planQty, value);
                }
            },
            {
                text: "已打包数", dataIndex: "packedQty", scope: this, summaryType: 'sum', width: "10%",
                summaryRenderer: function (value, summaryData, dataIndex) {
                    return Ext.String.format('{0}{1}', '总打包数：', value);
                },
                renderer: function (value, cellmeta, record, rowIndex, columnIndex, stroe) {
                    var planQty = record.data.planQty;
                    return getCellStyle(planQty, value);
                }
            },
            {
                text: "已集货数", dataIndex: "mergedQty", scope: this, summaryType: 'sum', width: "10%",
                summaryRenderer: function (value, summaryData, dataIndex) {
                    return Ext.String.format('{0}{1}', '总集货数：', value);
                },
                renderer: function (value, cellmeta, record, rowIndex, columnIndex, stroe) {
                    var planQty = record.data.planQty;
                    return getCellStyle(planQty, value);
                }
            },
            {
                text: "已发货数", dataIndex: "finishQty", flex: 1, scope: this, summaryType: 'sum', width: "10%",
                summaryRenderer: function (value, summaryData, dataIndex) {
                    return Ext.String.format('{0}{1}', '总发货数：', value);
                },
                renderer: function (value, cellmeta, record, rowIndex, columnIndex, stroe) {
                    var planQty = record.data.planQty;
                    return getCellStyle(planQty, value);
                }
            }
        ],
        viewConfig: {
            enableTextSelection: true,
            getRowClass: function (record, rowIndex, rowParams, store) {
                if (record.data.zt == 0) {
                    return 'x-grid-record-red';
                } else {
                    return '';
                }
            }
        }
    });

    this.cmp.txt_keyword.on('specialKey', function (field, e) {
        if (e.getKey() == e.ENTER) {
            keyword = this.cmp.txt_keyword.getValue();
            this.orderDetailStore.load();
        }
    }, this);


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

    function getCellStyle(planQty, currentQty) {
        if (planQty == currentQty) {
            return '<span style="color:green;">' + currentQty + '</span>';
        } else {
            return '<span style="color:red;">' + currentQty + '</span>';
        }
    }
};