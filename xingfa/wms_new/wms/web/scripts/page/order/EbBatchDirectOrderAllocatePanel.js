var EbBatchDirectOrderAllocatePanel = function (data) {
    this.source = new CommonSource(data);

    this.source.txt_qty.allowBlank = false;
    this.source.com_logicZoneCode.allowBlank = false;
    this.source.com_labelPrinter.allowBlank = false;
    this.source.date_allocateStopDate.allowBlank = false;
    this.form = Ext.create("Ext.form.Panel", {
        title: '电商订单批量指定订单分配',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.com_logicZoneCode,
                    this.source.txt_qty
                ]
            },
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.com_labelPrinter,
                    this.source.date_allocateStopDate
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
                        var labelPrinterId = this.source.com_labelPrinter.getValue();
                        var qty = this.source.txt_qty.getValue();
                        var logicZoneCode = this.source.com_logicZoneCode.getSubmitValue();
                        var allocateStopDate = this.source.date_allocateStopDate.getSubmitValue();
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
                                        url: 'wms/ebOrderAllocatePrint/batchDirectAllocate',
                                        method: 'POST',
                                        timeout: 120000,
                                        params: {
                                            'labelPrinterId': labelPrinterId,
                                            'qty': qty,
                                            'logicZoneCode': logicZoneCode,
                                            'allocateStopDate': allocateStopDate
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
            tooltip: '电商订单批量指定订单分配'
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
