var RetrievalDetailPanel = function (data) {
    this.source = new CommonSource(data);

    var exportExcelBtn = Ext.create("Ext.button.Button", {
            text: "导出",
            scope: this,
            handler: function () {
                var batchNo = this.source.txt_batchNo.getValue();
                if (batchNo == null) {
                    batchNo = '';
                }
                var skuCode = this.source.com_skuCode.getValue();
                if (skuCode == null) {
                    skuCode = '';
                }
                var outletId = this.source.com_outletId.getValue();
                if (outletId == null) {
                    outletId = '';
                }
                var retrievalOrderNo = this.source.txt_retrievalOrderNo.getValue();
                if (retrievalOrderNo == null) {
                    retrievalOrderNo = '';
                }
                var cnorDocNo = this.source.txt_cnorDocNo.getValue();
                if (cnorDocNo == null) {
                    cnorDocNo = '';
                }
                var beginDate = this.source.date_beginDate.getSubmitValue();
                if (beginDate == null) {
                    beginDate = '';
                }
                var endDate = this.source.date_endDate.getSubmitValue();
                if (endDate == null) {
                    endDate = '';
                }
                var sendCarOrderNo = this.source.txt_sendCarOrderNo.getValue();
                if(sendCarOrderNo == null){
                    sendCarOrderNo = '';
                }
                var wareHouseId = this.source.com_wareHouseId.getSubmitValue();
                if (wareHouseId == null)
                    wareHouseId = '';

                var exporturl = 'wms/retrievalResult/exportExcel?batchNo=' + batchNo + '&skuCode=' + skuCode + '&outletId='
                    + outletId + '&retrievalOrderNo=' + retrievalOrderNo + '&cnorDocNo=' + cnorDocNo + "&beginDate=" + beginDate + "&endDate=" + endDate+"&sendCarOrderNo="+sendCarOrderNo+'&wareHouseId='+wareHouseId;
                //window.location.href = exporturl;

                Ext.Msg.show({
                    title: '确认',
                    message: '确认导出？',
                    buttons: Ext.Msg.YESNO,
                    icon: Ext.Msg.QUESTION,
                    scope: this,
                    fn: function (btn) {
                        if (btn == 'yes') {
                            mask.show();
                            Ext.Ajax.request({
                                url: exporturl,
                                headers: {'Content-Type': "application/json; charset=utf-8"},
                                scope: this,
                                timeout: 120000,
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
    );

    this.form = Ext.create("Ext.form.Panel", {
        title: '出库订单明细查询',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.txt_batchNo,
                    this.source.com_skuCode,
                    this.source.date_beginDate,
                    this.source.com_outletId,
                    this.source.date_lot
                ]
            },
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.txt_retrievalOrderNo,
                    this.source.txt_cnorDocNo,
                    this.source.date_endDate,
                    this.source.txt_sendCarOrderNo,
                    this.source.com_wareHouseId
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
                    this.store.loadPage(1);
                }
            }
        ]
    });

    this.store = Ext.create("Ext.data.JsonStore", {
        scope: this,
        fields: ['warehouseCode', 'batachNo', 'retrievalOrderNo', 'cnorDocNo', 'sendCarOrderNo', 'outletCode', 'slideNo', 'itemCode', 'itemName', 'planQty', 'eom', 'allocatedQty', 'pickedQty', 'sortedQty', 'mergedQty', 'checkedQty', 'finishQty', 'packedQty', 'retrievalSeq', 'onTransferFlag'],
        pageSize: 20,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/retrievalResult/search",
            timeout: 900000,
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

    var seqMap = new Map();
    seqMap.put('1', '先进先出');
    seqMap.put('2', '后进先出');
    var onLineMap = new Map();
    onLineMap.put(true, '投线');
    onLineMap.put(false, '不投线');
    this.grid = Ext.create("Ext.grid.Panel", {
            store: this.store,
            scope: this,
            viewConfig: {
                enableTextSelection: true
            },
            columns: [
                {text: "仓库", dataIndex: "warehouseCode", width: "5%"},
                {text: "波次号", dataIndex: "batachNo", width: "10%"},
                {text: "出库单号", dataIndex: "retrievalOrderNo", width: "10%"},
                {text: "SAP单号", dataIndex: "cnorDocNo", width: "10%"},
                {text: "派车单号", dataIndex: "sendCarOrderNo", width: "10%"},
                {text: "门店号", dataIndex: "outletCode", width: "10%"},
                {text: "滑道", dataIndex: "slideNo", width: "10%"},
                {text: "商品代码", dataIndex: "itemCode", width: "10%"},
                {text: "商品名称", dataIndex: "itemName", width: "10%"},
                {text: "订单数量", dataIndex: "planQty", width: "10%"},
                {text: "单位", dataIndex: "eom", width: "10%"},
                {text: "分配数量", dataIndex: "allocatedQty", width: "10%"},
                {text: "拣选数量", dataIndex: "pickedQty", width: "10%"},
                {text: "分拣数量", dataIndex: "sortedQty", width: "10%"},
                {text: "集货数量", dataIndex: "mergedQty", width: "10%"},
                {text: "复核数量", dataIndex: "checkedQty", width: "10%"},
                {text: "发货数量", dataIndex: "finishQty", width: "10%"},
                {text: "打包数量", dataIndex: "packedQty", width: "10%"},
                {
                    text: "出库策略", dataIndex: "retrievalSeq", width: "10%", scope: this, renderer: function (value) {
                    return seqMap.get(value);
                }
                },
                {
                    text: "是否投线", dataIndex: "onTransferFlag", width: "10%", scope: this, renderer: function (value) {
                    return onLineMap.get(value);
                }
                }
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
                    items: [exportExcelBtn]
                },
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
            tooltip: '出库订单明细查询'
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