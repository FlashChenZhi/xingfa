var EbOrderPagePickAllocatePanel = function (data) {
    this.source = new CommonSource(data);
    this.cmp = new EbOrderAllocatePrintSource();

    this.source.com_currentCarrierCode.allowBlank = false;
    this.cmp.txt_batchCount.allowBlank = false;
    this.source.com_lazerPrinter.allowBlank = false;
    this.source.com_labelPrinter.allowBlank = false;
    this.cmp.com_logicZone.allowBlank = false;
    this.source.txt_qty.allowBlank = false;
    this.form = Ext.create("Ext.form.Panel", {
        title: '电商订单分配',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.com_currentCarrierCode,
                    this.cmp.txt_batchCount,
                    this.source.com_lazerPrinter
                ]
            },
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.cmp.com_logicZone,
                    this.cmp.txt_qty,
                    this.source.com_labelPrinter
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
                text: '分配',
                scope: this,
                formBind: true,
                handler: function () {
                    var form = this.form.getForm();
                    if (form.isValid()) {
                        var carrierCode = this.source.com_currentCarrierCode.getValue();
                        var batchCount = this.cmp.txt_batchCount.getValue();
                        var lazePrinterId = this.source.com_lazerPrinter.getValue();
                        var logicZoneId = this.cmp.com_logicZone.getValue();
                        var labelPrinterId = this.source.com_labelPrinter.getValue();
                        var qty = this.cmp.txt_qty.getValue();
                        Ext.Msg.show({
                            title: '确认',
                            message: '确定分配？',
                            buttons: Ext.Msg.YESNO,
                            icon: Ext.Msg.QUESTION,
                            scope: this,
                            fn: function (btn) {
                                if (btn === 'yes') {
                                    mask.show();
                                    Ext.Ajax.request({
                                        url: 'wms/ebOrderAllocatePrint/allocateByGoodList',
                                        method: 'POST',
                                        timeout: 120000,
                                        params: {
                                            'carrierCode': carrierCode,
                                            'batchCount': batchCount,
                                            'labelPrinterId': labelPrinterId,
                                            'lazePrinterId': lazePrinterId,
                                            'logicZoneId': logicZoneId,
                                            'qty': qty
                                        },
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
                                } else {
                                    //todo cancel
                                }
                            }
                        });
                    }
                }
            }
        ]
    });

    this.panel = Ext.create("Ext.panel.Panel", {
        tabConfig: {
            tooltip: '纸质单拣选分配'
        },
        overflowY: 'auto',
        items: [
            this.form
        ]
    });

    var mask = new Ext.LoadMask({
        msg: '系统处理中...',
        target: this.panel
    });
};
