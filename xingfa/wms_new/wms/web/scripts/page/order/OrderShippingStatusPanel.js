var OrderShippingStatusPanel = function (data) {

    this.source = new CommonSource(data);

    this.form = Ext.create("Ext.form.Panel", {
        title: '波次出库实绩查询',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.txt_batchNo,
                    this.source.date_beginDate
                ]
            },
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.txt_sendCarOrderNo,
                    this.source.date_endDate
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
                    var paraFlag = false;
                    if (this.source.txt_batchNo.getValue() != null && this.source.txt_batchNo.getValue() != '') {
                        paraFlag = true;
                    }
                    if (this.source.date_beginDate.getSubmitValue() != null && this.source.date_beginDate.getSubmitValue() != '') {
                        paraFlag = true;
                    }
                    if (this.source.txt_sendCarOrderNo.getValue() != null && this.source.txt_sendCarOrderNo.getValue() != '') {
                        paraFlag = true;
                    }
                    if (this.source.date_endDate.getSubmitValue() != null && this.source.date_endDate.getSubmitValue() != '') {
                        paraFlag = true;
                    }
                    if (paraFlag == false) {
                        Ext.error("无条件查询会增加服务器负荷，请输入适当的查询条件");
                        return;
                    }

                    this.store.loadPage(1);
                }
            }
        ]
    });

    this.store = Ext.create("Ext.data.JsonStore", {
        scope: this,
        fields: ['batchNo', 'sendCarOrderNo', 'totalQty', 'allocateQty', 'pickedQty', 'sortedQty', 'mergedQty', 'checkedQty'],
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/batchRetrievalResult/search",
            actionMethods: {read: "POST"},
            timeout: 120000,
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


    this.grid = Ext.create("Ext.grid.Panel", {
            store: this.store,
            scope: this,
            features: [
                {
                    ftype: 'summary'
                }
            ],
            columns: [
                {text: "波次号", dataIndex: "batchNo", width: "20%"},
                {text: "派车单号", dataIndex: "sendCarOrderNo", width: "20%"},
                {
                    text: "总数", dataIndex: "totalQty", width: "10%", scope: this, summaryType: 'sum',
                    summaryRenderer: function (value, summaryData, dataIndex) {
                        return Ext.String.format('{0}{1}', '总数：', value);
                    }
                },
                {
                    text: "已分配数", dataIndex: "allocateQty", width: "10%", scope: this, summaryType: 'sum',
                    summaryRenderer: function (value, summaryData, dataIndex) {
                        return Ext.String.format('{0}{1}', '总分配数：', value);
                    },
                    renderer: function (value, cellmeta, record, rowIndex, columnIndex, stroe) {
                        var totalQty = record.data.totalQty;
                        return getCellStyle(totalQty, value);
                    }
                },
                {
                    text: "已拣选数", dataIndex: "pickedQty", width: "10%", scope: this, summaryType: 'sum',
                    summaryRenderer: function (value, summaryData, dataIndex) {
                        return Ext.String.format('{0}{1}', '总拣选数：', value);
                    },
                    renderer: function (value, cellmeta, record, rowIndex, columnIndex, stroe) {
                        var totalQty = record.data.totalQty;
                        return getCellStyle(totalQty, value);
                    }
                },
                {
                    text: "已分拣数", dataIndex: "sortedQty", width: "10%", scope: this, summaryType: 'sum',
                    summaryRenderer: function (value, summaryData, dataIndex) {
                        return Ext.String.format('{0}{1}', '总分拣数：', value);
                    },
                    renderer: function (value, cellmeta, record, rowIndex, columnIndex, stroe) {
                        var totalQty = record.data.totalQty;
                        return getCellStyle(totalQty, value);
                    }
                },
                {
                    text: "已集货数", dataIndex: "mergedQty", width: "10%", scope: this, summaryType: 'sum',
                    summaryRenderer: function (value, summaryData, dataIndex) {
                        return Ext.String.format('{0}{1}', '总集货数：', value);
                    },
                    renderer: function (value, cellmeta, record, rowIndex, columnIndex, stroe) {
                        var totalQty = record.data.totalQty;
                        return getCellStyle(totalQty, value);
                    }
                },
                {
                    text: "已复核数", dataIndex: "checkedQty", width: "10%", scope: this, summaryType: 'sum',
                    summaryRenderer: function (value, summaryData, dataIndex) {
                        return Ext.String.format('{0}{1}', '总复核数：', value);
                    },
                    renderer: function (value, cellmeta, record, rowIndex, columnIndex, stroe) {
                        var totalQty = record.data.totalQty;
                        return getCellStyle(totalQty, value);
                    }
                }
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
                                text: "订单明细", handler: function () {
                                this.orderDetailWin = null;
                                var orderNo = record.data.sendCarOrderNo;
                                if (this.orderDetailWin == null) {
                                    this.orderDetailWin = new OrderShippingDetailWin(orderNo);
                                }
                                this.orderDetailWin.win.title = "派车单:" + orderNo + "明细一览";
                                this.orderDetailWin.win.show();
                            }
                            }
                        ]
                    });
                    menu.showAt(e.getPoint());
                }
            },
            selType: 'cellmodel',
            viewConfig: {
                enableTextSelection: true
            }
            //dockedItems: [
            //    {
            //        xtype: "pagingtoolbar",
            //        store: this.store,
            //        dock: "bottom",
            //        displayInfo: true
            //    }
            //]
        }
    );


    this.panel = Ext.create("Ext.panel.Panel", {
        tabConfig: {
            tooltip: '波次出库实绩查询'
        },
        overflowY: 'auto',
        items: [
            this.form, this.grid
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