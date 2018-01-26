var EbDirectOrderAllocatePanel = function (data) {
    this.source = new CommonSource(data);

    this.source.txt_retrievalOrderNo.allowBlank = false;
    this.source.com_labelPrinter.allowBlank = false;
    this.form = Ext.create("Ext.form.Panel", {
        title: '电商订单指定订单分配',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.txt_retrievalOrderNo,
                    this.source.com_logicZoneCode
                ]
            },
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
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
                        var labelPrinterId = this.source.com_labelPrinter.getValue();
                        var orderNo = this.source.txt_retrievalOrderNo.getValue();
                        var logicZoneCode = this.source.com_logicZoneCode.getSubmitValue();
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
                                        url: 'wms/ebOrderAllocatePrint/directAllocate',
                                        method: 'POST',
                                        timeout: 120000,
                                        params: {
                                            'labelPrinterId': labelPrinterId,
                                            'orderNo': orderNo,
                                            'logicZoneCode': logicZoneCode
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
            tooltip: '电商订单指定订单分配'
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
