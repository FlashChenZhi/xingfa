var EbOrderAllocatePanel = function (data) {
    this.source = new CommonSource(data);
    this.cmp = new EbOrderAllocatePrintSource();

    this.source.com_currentCarrierCode.allowBlank = false;
    this.cmp.txt_batchCount.allowBlank = false;
    this.cmp.com_labelPrinter.allowBlank = false;
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
                    this.cmp.com_labelPrinter
                ]
            },
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.cmp.com_logicZone,
                    this.cmp.txt_qty
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
                        var labelPrinterId = this.cmp.com_labelPrinter.getValue();
                        var logicZoneId = this.cmp.com_logicZone.getValue();
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
                                        url: 'wms/ebOrderAllocatePrint/allocate',
                                        method: 'POST',
                                        timeout: 120000,
                                        params: {
                                            'carrierCode': carrierCode,
                                            'batchCount': batchCount,
                                            'labelPrinterId': labelPrinterId,
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
            tooltip: '电商订单分配'
        },
        overflowY: 'auto',
        items: [
            this.form
        ]
    });

    this.source.com_currentCarrierCode.on('select', function (thiz, value) {
        var selectValue = value.data.value;
        console.log("select value : " + selectValue);
        this.cmp.com_labelPrinter.clearValue();
        this.cmp.init(selectValue);
    }, this);

    this.source.dsCarrierStore.on('load', function () {
        var count =this.source.dsCarrierStore.getCount();
        console.log('count=' + count);
        if (count > 0) {
            this.source.com_currentCarrierCode.setValue(this.source.dsCarrierStore.getAt(0));
        }
        this.cmp.com_labelPrinter.clearValue();
        this.cmp.init(this.source.com_currentCarrierCode.getSubmitValue());
    }, this);

    var mask = new Ext.LoadMask({
        msg: '系统处理中...',
        target: this.panel
    });
};
