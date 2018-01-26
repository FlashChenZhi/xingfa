var EbOrderSearchPanel = function (data) {

    this.cmp = new EbOrderSearchSource();

    this.source = new CommonSource(data);

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
                    this.cmp.com_preAllocatedFinishFlag,
                    this.cmp.com_pickedFinishFlag,
                    this.cmp.com_mergingFinishFlag,
                    this.cmp.txt_palletNo
                ]
            },
            {
                layout: 'form', columnWidth: .5,
                items: [
                    this.cmp.text_shipperDocNo,
                    this.cmp.text_retrievalOrderBatchCode,
                    this.cmp.com_mantyFlag,
                    this.cmp.com_allocatedFinishFlag,
                    this.cmp.com_packingFinishFlag,
                    this.cmp.com_shippingFinishFlag,
                    this.cmp.com_weightFlag
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

                var preAllocatedFinishFlag = this.cmp.com_preAllocatedFinishFlag.getSubmitValue();
                if (preAllocatedFinishFlag == null)
                    preAllocatedFinishFlag = '';

                var pickedFinishFlag = this.cmp.com_pickedFinishFlag.getSubmitValue();
                if (pickedFinishFlag == null)
                    pickedFinishFlag = '';

                var mergingFinishFlag = this.cmp.com_mergingFinishFlag.getSubmitValue();
                if (mergingFinishFlag == null)
                    mergingFinishFlag = '';

                var shipperDocNo = this.cmp.text_shipperDocNo.getSubmitValue();
                if (shipperDocNo == null)
                    shipperDocNo = '';

                var retrievalOrderBatchCode = this.cmp.text_retrievalOrderBatchCode.getSubmitValue();
                if (retrievalOrderBatchCode == null)
                    retrievalOrderBatchCode = '';

                var mantyFlag = this.cmp.com_mantyFlag.getSubmitValue();
                if (mantyFlag == null)
                    mantyFlag = '';

                var allocatedFinishFlag = this.cmp.com_allocatedFinishFlag.getSubmitValue();
                if (allocatedFinishFlag == null)
                    allocatedFinishFlag = '';

                var packingFinishFlag = this.cmp.com_packingFinishFlag.getSubmitValue();
                if (packingFinishFlag == null)
                    packingFinishFlag = '';

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
                var palletNo = this.cmp.txt_palletNo.getSubmitValue();
                if (palletNo == null)
                    palletNo = '';

                var weightFlag = this.cmp.com_weightFlag.getSubmitValue();
                if (weightFlag == null)
                    weightFlag = '';

                var pickingFinishDateFrom = this.cmp.date_pickingFinishDateFrom.getSubmitValue();
                if (pickingFinishDateFrom == null)
                    pickingFinishDateFrom = '';
                var pickingFinishDateTo = this.cmp.date_pickingFinishDateTo.getSubmitValue();
                if (pickingFinishDateTo == null)
                    pickingFinishDateTo = '';
                var packingFinishDateFrom = this.cmp.date_packingFinishDateFrom.getSubmitValue();
                if (packingFinishDateFrom == null)
                    packingFinishDateFrom = '';
                var packingFinishDateTo = this.cmp.date_packingFinishDateTo.getSubmitValue();
                if (packingFinishDateTo == null)
                    packingFinishDateTo = '';
                var mergingFinishDateFrom = this.cmp.date_mergingFinishDateFrom.getSubmitValue();
                if (mergingFinishDateFrom == null)
                    mergingFinishDateFrom = '';
                var mergingFinishDateTo = this.cmp.date_mergingFinishDateTo.getSubmitValue();
                if (mergingFinishDateTo == null)
                    mergingFinishDateTo = '';

                var exporturl = 'wms/ebOrderSearch/exportExcel?orderNo=' + orderNo + '&logicZoneCode=' + logicZoneCode + '&cancelFlag=' + cancelFlag + '&preAllocatedFinishFlag=' + preAllocatedFinishFlag + '&pickedFinishFlag=' + pickedFinishFlag
                    + '&mergingFinishFlag=' + mergingFinishFlag + '&shipperDocNo=' + shipperDocNo + '&retrievalOrderBatchCode=' + retrievalOrderBatchCode + '&mantyFlag=' + mantyFlag + '&allocatedFinishFlag=' + allocatedFinishFlag
                    + '&packingFinishFlag=' + packingFinishFlag + '&shippingFinishFlag=' + shippingFinishFlag + '&carrierCode=' + carrierCode + '&expressNo=' + expressNo + '&cnorDocNo=' + cnorDocNo + '&orderDateFrom=' + orderDateFrom
                    + '&orderFinishDateFrom=' + orderFinishDateFrom + '&limitFinishDate=' + limitFinishDate + '&orderDateTo=' + orderDateTo + '&orderFinishDateTo=' + orderFinishDateTo + '&palletNo=' + palletNo + '&weightFlag=' + weightFlag
                    + '&pickingFinishDateFrom=' + pickingFinishDateFrom + '&pickingFinishDateTo=' + pickingFinishDateTo + '&packingFinishDateFrom=' + packingFinishDateFrom + '&packingFinishDateTo=' + packingFinishDateTo
                    + '&mergingFinishDateFrom=' + mergingFinishDateFrom + '&mergingFinishDateTo=' + mergingFinishDateTo;
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

    this.shippingQcPanel = Ext.create("Ext.form.Panel", {
        title: '运单条件',
        layout: 'column',
        scope: this,
        collapsible: true,
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {anchor: '100%'},
                items: [
                    this.source.com_dsCarrierCode,
                    this.source.txt_provice
                ]
            },
            {
                layout: 'form', columnWidth: .5,
                items: [
                    this.cmp.text_expressNo
                ]
            }
        ]
    });

    this.consignorQcPanel = Ext.create("Ext.form.Panel", {
        title: '货主商品条件',
        layout: 'column',
        scope: this,
        collapsible: true,
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {anchor: '100%'},
                items: [
                    this.cmp.text_cnorDocNo,
                    this.source.com_ownerCode,
                    this.cmp.txt_phone
                ]
            },
            {
                layout: 'form', columnWidth: .5,
                items: [
                    this.cmp.text_platformOrderNo,
                    this.source.com_skuCode,
                    this.cmp.txt_name
                ]
            }
        ]
    });
    this.orderTimeQcPanel = Ext.create("Ext.form.Panel", {
        title: '时间范围条件',
        layout: 'column',
        scope: this,
        collapsible: true,
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {anchor: '100%'},
                items: [
                    this.cmp.date_orderDateFrom,
                    this.cmp.date_pickingFinishDateFrom,
                    this.cmp.date_packingFinishDateFrom,
                    this.cmp.date_mergingFinishDateFrom,
                    this.cmp.date_orderFinishDateFrom,
                ]
            },
            {
                layout: 'form', columnWidth: .5,
                items: [
                    this.cmp.date_orderDateTo,
                    this.cmp.date_pickingFinishDateTo,
                    this.cmp.date_packingFinishDateTo,
                    this.cmp.date_mergingFinishDateTo,
                    this.cmp.date_orderFinishDateTo
                ]
            }
        ]
    });

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


    this.store = Ext.create("Ext.data.JsonStore", {
        fields: ['id', 'orderNo', 'wareHouseCode', 'ownerCode', 'goodListPrintFlag', 'ownerName', 'status', 'type', 'cnorDocNo',
            'platformOrderNo', 'shipperDocNo', 'carrierCode', 'expressNo', 'cneeName', 'cneeMobile', 'cneeTel', 'address', 'packageTime', 'merginTime', 'shippingTime',
            'limitFinishDate', 'cancelFlag', 'orderDate', 'mantyFlag', 'printFlag', 'preAllocatedFinishFlag', 'allocatedFinishFlag', 'province', 'city', 'county',
            'pickedFinishFlag','threeCode', 'groupNo', 'groupSerialNo', 'packingFinishFlag', 'mergingFinishFlag', 'totalWeight', 'realWeight', 'totalVolume', 'packageCount', 'batchNo', 'logicZoneCode', 'memo', 'shippingFinishFlag'],
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
            url: "wms/ebOrderSearch/search",
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

    var statusMap = this.cmp.statusMap();
    var typeMap = this.cmp.typeMap();
    var isMantyMap = this.cmp.isMantyMap();
    var cancelFlagMap = this.cmp.cancelFlagMap();
    var preAllocateFlagMap = this.cmp.preAllocateFlagMap();
    var finishFlagMap = this.cmp.finishFlagMap();
    var printedFlagMap = this.cmp.printedFlagMap();

    this.grid = Ext.create("Ext.grid.Panel", {
        selModel: this.sm,
        store: this.store,
        scope: this,
        columns: [
            {text: "ID", dataIndex: "id", hidden: true},
            {text: "订单号", dataIndex: "orderNo"},
            {text: "仓库代码", dataIndex: "wareHouseCode"},
            {text: "货主代码", dataIndex: "ownerCode"},
            {text: "货主名称", dataIndex: "ownerName"},
            {text: "SAP单据号", dataIndex: "cnorDocNo"},
            {text: "外部订单号", dataIndex: "platformOrderNo"},
            {text: "发货方单据号", dataIndex: "shipperDocNo"},
            {
                text: "状态", dataIndex: "status", scope: this, renderer: function (value) {
                return statusMap.get(value);
            }
            },
            {
                text: "取消标志", dataIndex: "cancelFlag", scope: this, renderer: function (value) {
                return cancelFlagMap.get(value);
            }
            },
            {text: "订单日", dataIndex: "orderDate"},
            {
                text: "是否大订单", dataIndex: "mantyFlag", scope: this, renderer: function (value) {
                return isMantyMap.get(value);
            }
            },
            {
                text: "面单是否打印", dataIndex: "printFlag", scope: this, renderer: function (value) {
                return printedFlagMap.get(value);
            }
            }, {
                text: "发货清单是否打印", dataIndex: "goodListPrintFlag", scope: this, renderer: function (value) {
                    return printedFlagMap.get(value);
                }
            },
            {
                text: "是否预分配完成", dataIndex: "preAllocatedFinishFlag", scope: this, renderer: function (value) {
                return preAllocateFlagMap.get(value);
            }
            },
            {
                text: "是否分配完成", dataIndex: "allocatedFinishFlag", scope: this, renderer: function (value) {
                return finishFlagMap.get(value);
            }
            },
            {
                text: "是否拣选完成", dataIndex: "pickedFinishFlag", scope: this, renderer: function (value) {
                return finishFlagMap.get(value);
            }
            },
            {
                text: "是否打包完成", dataIndex: "packingFinishFlag", scope: this, renderer: function (value) {
                return finishFlagMap.get(value);
            }
            },
            {
                text: "是否集货完成", dataIndex: "mergingFinishFlag", scope: this, renderer: function (value) {
                return finishFlagMap.get(value);
            }
            },
            {
                text: "是否发货完成", dataIndex: "shippingFinishFlag", scope: this, renderer: function (value) {
                return finishFlagMap.get(value);
            }
            },
            {text: "承运商", dataIndex: "carrierCode"},
            {text: "打包时间", dataIndex: "packageTime"},
            {text: "集货时间", dataIndex: "merginTime"},
            {text: "发货时间", dataIndex: "shippingTime"},
            {text: "运单号", dataIndex: "expressNo"},
            {text: "三段码", dataIndex: "threeCode"},
            {text: "收货人", dataIndex: "cneeName"},
            {text: "收货人手机", dataIndex: "cneeMobile"},
            {text: "收货人固话", dataIndex: "cneeTel"},
            {text: "省", dataIndex: "province"},
            {text: "市", dataIndex: "city"},
            {text: "区县", dataIndex: "county"},
            {text: "收货人详细地址", dataIndex: "address"},
            {text: "限制发货时间", dataIndex: "limitFinishDate"},
            {text: "订单重量", dataIndex: "totalWeight"},
            {text: "实际重量", dataIndex: "realWeight"},
            {text: "订单体积", dataIndex: "totalVolume"},
            {text: "订单件数", dataIndex: "packageCount"},
            {text: "预分配失败次数", dataIndex: "preAllocationFail"},
            {text: "分配失败次数", dataIndex: "allocationFail"},
            {text: "备注", dataIndex: "memo"},
            {text: "批次号", dataIndex: "batchNo"},
            {text: "作业区", dataIndex: "logicZoneCode"},
            {text: "组号", dataIndex: "groupNo"},
            {text: "组序号", dataIndex: "groupSerialNo"}
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
                            var orderId = record.data.id;
                            var orderNo = record.data.orderNo;
                            if (this.orderDetailWin == null) {
                                this.orderDetailWin = new EbOrderDetailWin(orderId);
                            } else {
                                this.orderDetailWin.init(orderId);
                            }
                            this.orderDetailWin.win.title = "订单:" + orderNo + "明细一览";
                            this.orderDetailWin.win.show();
                        }
                        }, {
                            text: "修改地址", handler: function () {
                                var orderNo = record.data.orderNo;
                                var address = record.data.address;
                                this.orderDetailWin = new EbOrderModifyAddressWin(data);
                                this.orderDetailWin.init(orderNo, address);
                                this.orderDetailWin.win.title = "订单:" + orderNo + "修改地址";
                                this.orderDetailWin.win.show();
                            }
                        }, {
                            text: "重获三段码",
                            handler: function () {
                                Ext.Msg.show({
                                    title: '确认',
                                    message: '确认重获三段码？',
                                    buttons: Ext.Msg.YESNO,
                                    icon: Ext.Msg.QUESTION,
                                    scope: this,
                                    fn: function (btn) {
                                        if (btn === 'yes') {
                                            var orderNo = record.data.orderNo;
                                            mask.show();
                                            Ext.Ajax.request({
                                                url: 'wms/ebOrderSearch/updateThreeCode',
                                                method: 'POST',
                                                params: {'orderNo': orderNo},
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
                });
                menu.showAt(e.getPoint());
            }
        },
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