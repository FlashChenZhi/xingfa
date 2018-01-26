var ExpressOrGoodsBoxPrintPanel = function (data) {
    this.cmp = new ExpressOrGoodsBoxPrintSource();
    this.eos = new EbOrderSearchSource();

    this.source = new CommonSource(data);

    this.cmp.com_printerType.allowBlank = false;
    this.source.com_currentCarrierCode.allowBlank = false;

    var batchPrintBtn = Ext.create("Ext.button.Button", {
            text: "批量打印",
            scope: this,
            handler: function () {
                var form = this.form.getForm();

                var printerType = this.cmp.com_printerType.getSubmitValue();
                var labelPrinterId = this.source.com_labelPrinter.getSubmitValue();
                var lazerPrinter = this.source.com_lazerPrinter.getSubmitValue();

                if (form.isValid()) {
                    Ext.Msg.show({
                        title: '确认',
                        message: '确认打印？',
                        buttons: Ext.Msg.YESNO,
                        icon: Ext.Msg.QUESTION,
                        scope: this,
                        fn: function (btn) {
                            if (btn === 'yes') {
                                var results = [];
                                var sels = this.grid.getSelection();

                                for (var res in sels) {
                                    results.push({
                                        printerType: printerType,
                                        labelPrinterId: labelPrinterId,
                                        lazerPrinter: lazerPrinter,
                                        carrierCode: sels[res].data.carrierCode,
                                        expressNo: sels[res].data.expressNo,
                                    });
                                }
                                if (sels.length == 0) {
                                    Ext.warn('请至少选择一条数据');
                                    return;
                                }

                                mask.show();
                                Ext.Ajax.request({
                                    url: 'wms/expressOrGoodsBoxPrint/batchPrint',
                                    headers: {'Content-Type': "application/json; charset=utf-8"},
                                    method: 'POST',
                                    params: Ext.util.JSON.encode(results),
                                    scope: this,
                                    success: function (response, options) {
                                        mask.hide();
                                        var result = Ext.util.JSON.decode(response.responseText);
                                        if (result.success) {
                                            Ext.success(result.msg);
                                            this.store.loadPage(1);
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
                            else {
                                //todo cancel
                            }
                        }
                    });
                }
            }
        }
    );


    this.form = Ext.create('Ext.form.Panel', {
        title: '面单装箱单打印',
        layout: 'column',
        scope: this,
        items: [
            {
                layout: "form", columnWidth: .33, defaults: {anchor: '100%'},
                items: [
                    this.cmp.com_printerType,
                    this.source.com_currentCarrierCode
                ]
            },
            {
                layout: 'form', columnWidth: .33,
                items: [
                    this.source.com_labelPrinter,
                    this.cmp.text_expressNo
                ]
            },
            {
                layout: 'form', columnWidth: .33,
                items: [
                    this.source.com_lazerPrinter,
                    this.eos.txt_batchNo
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
        fields: ['id', 'orderNo', 'wareHouseCode', 'ownerCode', 'ownerName', 'status', 'type', 'cnorDocNo',
            'platformOrderNo', 'shipperDocNo', 'carrierCode', 'expressNo', 'cneeName', 'cneeMobile', 'cneeTel', 'address',
            'limitFinishDate', 'cancelFlag', 'orderDate', 'mantyFlag', 'printFlag', 'preAllocatedFinishFlag', 'allocatedFinishFlag',
            'pickedFinishFlag', 'packingFinishFlag', 'mergingFinishFlag', 'totalWeight', 'realWeight', 'totalVolume', 'packageCount', 'batchNo', 'logicZoneCode', 'memo', 'shippingFinishFlag'],
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
            url: "wms/expressOrGoodsBoxPrint/search",
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

    var statusMap = this.eos.statusMap();
    var isMantyMap = this.eos.isMantyMap();
    var cancelFlagMap = this.eos.cancelFlagMap();
    var preAllocateFlagMap = this.eos.preAllocateFlagMap();
    var finishFlagMap = this.eos.finishFlagMap();
    var printedFlagMap = this.eos.printedFlagMap();

    this.sm = Ext.create("Ext.selection.CheckboxModel", {
        checkOnly: true
    });
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
                text: "是否已打印", dataIndex: "printFlag", scope: this, renderer: function (value) {
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
            {text: "运单号", dataIndex: "expressNo"},
            {text: "收货人", dataIndex: "cneeName"},
            {text: "收货人手机", dataIndex: "cneeMobile"},
            {text: "收货人固话", dataIndex: "cneeTel"},
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
            {text: "作业区", dataIndex: "logicZoneCode"}
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
                        }
                    ]
                });
                menu.showAt(e.getPoint());
            }
        },
        dockedItems: [
            {
                xtype: "toolbar",
                dock: "top",
                scope: this,
                items: [batchPrintBtn]
            },
            {
                xtype: "pagingtoolbar",
                store: this.store,
                dock: "bottom",
                displayInfo: true
            }
        ],
        viewConfig: {
            enableTextSelection: true
        }
    });


    this.cmp.com_printerType.on('select', function (thiz, value) {
        var selectValue = value.data.value;
        console.log('selectValue :' + selectValue);
        if ('1' == selectValue) {
            this.source.com_labelPrinter.allowBlank = false;
            this.source.com_labelPrinter.enable();
            this.source.com_lazerPrinter.allowBlank = true;
            this.source.com_lazerPrinter.disable();
        } else {
            this.source.com_lazerPrinter.allowBlank = false;
            this.source.com_lazerPrinter.enable();
            this.source.com_labelPrinter.allowBlank = true;
            this.source.com_labelPrinter.disable();
        }
    }, this);

    this.cmp.text_expressNo.on('specialKey', function (field, e) {
        if (e.getKey() == e.ENTER) {
            this.print();
        }
    }, this);


    this.print = function () {
        var form = this.form.getForm();
        if (form.isValid()) {
            var printerType = this.cmp.com_printerType.getValue();
            var labelPrinterId = this.source.com_labelPrinter.getValue();
            var lazerPrinter = this.source.com_lazerPrinter.getValue();
            var carrierCode = this.source.com_currentCarrierCode.getValue();
            var expressNo = this.cmp.text_expressNo.getValue();
            Ext.Ajax.request({
                url: 'wms/expressOrGoodsBoxPrint/print',
                method: 'POST',
                scope: this,
                params: {
                    'printerType': printerType,
                    'labelPrinterId': labelPrinterId,
                    'lazerPrinter': lazerPrinter,
                    'carrierCode': carrierCode,
                    'expressNo': expressNo
                },
                success: function (response, options) {
                    var result = Ext.util.JSON.decode(response.responseText);
                    if (result.success) {
                        this.cmp.text_expressNo.reset();
                        this.cmp.text_expressNo.focus();
                    } else {
                        this.cmp.text_expressNo.focus();
                        Ext.error(result.msg);
                    }
                },
                failure: function (response, options) {
                    this.cmp.text_expressNo.focus();
                    var result = Ext.util.JSON.decode(response.responseText);
                    Ext.error(result.msg);
                }
            });
        }
    };

    this.panel = Ext.create("Ext.panel.Panel", {
        tabConfig: {
            tooltip: '面单装箱单打印'
        },
        overflowY: 'auto',
        items: [
            this.form, this.grid
        ]
    });

    this.source.dsCarrierStore.on('load', function () {
        var count =this.source.dsCarrierStore.getCount();
        console.log('count=' + count);
        if (count > 0) {
            this.source.com_currentCarrierCode.setValue(this.source.dsCarrierStore.getAt(0));
        }
    }, this);


    var mask = new Ext.LoadMask({
        msg: '系统处理中...',
        target: this.panel
    });

};