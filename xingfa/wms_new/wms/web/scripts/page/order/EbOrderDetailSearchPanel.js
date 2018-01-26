var EbOrderDetailSearchPanel = function (data) {

    this.cmp = new EbOrderSearchSource();

    this.source = new CommonSource(data);

    this.cmp.date_orderDateTo.fieldLabel = "订单时间截止日期";
    this.cmp.date_orderFinishDateTo.fieldLabel = "订单完成截止日期";

    this.orderQcPanel = Ext.create("Ext.form.Panel", {
        title: '订单条件',
        layout: 'column',
        scope: this,
        collapsible: true,
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {anchor: '100%'},
                items: [
                    this.cmp.text_orderNo,
                    this.source.com_logicZoneCode,
                    this.cmp.com_cancelFlag,
                    this.source.com_dsCarrierCode,
                    this.cmp.date_orderDateFrom,
                    this.cmp.date_orderFinishDateFrom,
                    this.cmp.text_expressNo,
                ]
            },
            {
                layout: 'form', columnWidth: .5,
                items: [
                    this.cmp.text_shipperDocNo,
                    this.cmp.text_cnorDocNo,
                    this.cmp.text_retrievalOrderBatchCode,
                    this.cmp.com_shippingFinishFlag,
                    this.cmp.date_orderDateTo,
                    this.cmp.date_orderFinishDateTo

                ]
            }
        ]
    });

    var exportExcelBtn = Ext.create("Ext.button.Button", {
            text: "导出",
            scope: this,
            handler: function () {

                var orderNo = this.cmp.text_orderNo.getSubmitValue();
                if (orderNo == null)
                    orderNo = '';

                var logicZoneCode = this.source.com_logicZoneCode.getSubmitValue();
                if (logicZoneCode == null)
                    logicZoneCode = '';

                var cancelFlag = this.cmp.com_cancelFlag.getSubmitValue();
                if (cancelFlag == null)
                    cancelFlag = '';

                var shipperDocNo = this.cmp.text_shipperDocNo.getSubmitValue();
                if (shipperDocNo == null)
                    shipperDocNo = '';

                var retrievalOrderBatchCode = this.cmp.text_retrievalOrderBatchCode.getSubmitValue();
                if (retrievalOrderBatchCode == null)
                    retrievalOrderBatchCode = '';

                var shippingFinishFlag = this.cmp.com_shippingFinishFlag.getSubmitValue();
                if (shippingFinishFlag == null)
                    shippingFinishFlag = '';

                var carrierCode = this.source.com_dsCarrierCode.getSubmitValue();
                if (carrierCode == null)
                    carrierCode = '';

                var expressNo = this.cmp.text_expressNo.getSubmitValue();
                if (expressNo == null)
                    expressNo = '';

                var cnorDocNo = this.cmp.text_cnorDocNo.getSubmitValue();
                if (cnorDocNo == null)
                    cnorDocNo = '';

                var orderDateFrom = this.cmp.date_orderDateFrom.getSubmitValue();
                if (orderDateFrom == null)
                    orderDateFrom = '';

                var orderFinishDateFrom = this.cmp.date_orderFinishDateFrom.getSubmitValue();
                if (orderFinishDateFrom == null)
                    orderFinishDateFrom = '';

                var limitFinishDate = this.cmp.text_limitDate.getSubmitValue();
                if (limitFinishDate == null)
                    limitFinishDate = '';

                var orderDateTo = this.cmp.date_orderDateTo.getSubmitValue();
                if (orderDateTo == null)
                    orderDateTo = '';

                var orderFinishDateTo = this.cmp.date_orderFinishDateTo.getSubmitValue();
                if (orderFinishDateTo == null)
                    orderFinishDateTo = '';


                var exporturl = 'wms/ebOrderDetailSearch/exportDetail?orderNo=' + orderNo + '&logicZoneCode=' + logicZoneCode + '&cancelFlag=' + cancelFlag + '&shipperDocNo=' + shipperDocNo
                    + '&retrievalOrderBatchCode=' + retrievalOrderBatchCode
                    + '&shippingFinishFlag=' + shippingFinishFlag + '&carrierCode=' + carrierCode + '&expressNo=' + expressNo + '&cnorDocNo=' + cnorDocNo + '&orderDateFrom=' + orderDateFrom
                    + '&orderFinishDateFrom=' + orderFinishDateFrom + '&limitFinishDate=' + limitFinishDate + '&orderDateTo=' + orderDateTo + '&orderFinishDateTo=' + orderFinishDateTo;
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

    var impForm = Ext.create("Ext.form.Panel", {
        layout: 'column',
        scope: this,
        items: [
            exportExcelBtn
        ]
    });
    this.cmp.com_cancelFlag.setValue('0');

    this.tabs = Ext.create('Ext.tab.Panel', {
        columnWidth: 1,
        items: [this.orderQcPanel, this.shippingQcPanel, this.consignorQcPanel, this.orderTimeQcPanel]
    });

    this.form = Ext.create('Ext.form.Panel', {
        title: '订单查询',
        layout: 'column',
        scope: this,
        collapsible: true,
        /* buttonAlign: 'left',*/
        items: this.tabs,
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

    var statusMap = this.cmp.statusMap();

    this.store = Ext.create("Ext.data.JsonStore", {
        fields: ['orderNo', 'cnorDocNo', 'status', 'expressNo', 'skuCode', 'skuName', 'planQty', 'eom'],
        pageSize: 20,
        scope: this,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/ebOrderDetailSearch/searchDetail",
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

    this.orderDetailWin = null;


    this.grid = Ext.create("Ext.grid.Panel", {
        selModel: this.sm,
        store: this.store,
        scope: this,
        columns: [
            {text: "订单号", dataIndex: "orderNo", width: "15%"},
            {text: "上位单号", dataIndex: "cnorDocNo", width: "20%"},
            {
                text: "状态", dataIndex: "status", width: "10%", scope: this, renderer: function (value) {
                return statusMap.get(value);
            }
            },
            {text: "快递单号", dataIndex: "expressNo", width: "10%"},
            {text: "商品代码", dataIndex: "skuCode", width: "10%"},
            {text: "商品名称", dataIndex: "skuName", width: "20%"},
            {text: "数量", dataIndex: "planQty", width: "10%"},
            {text: "单位", dataIndex: "eom", width: "10%"},
        ],
        dockedItems: [
            {
                xtype: "pagingtoolbar",
                store: this.store,
                dock: "bottom",
                displayInfo: true
            }, {
                xtype: "toolbar",
                dock: "top",
                scope: this,
                items: [impForm]
            }
        ],
        viewConfig: {
            enableTextSelection: true
        }
    });

    this.panel = Ext.create("Ext.panel.Panel", {
        tabConfig: {
            tooltip: '电商订单查询'
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