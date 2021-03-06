var EbMerginPrintPanel = function (data) {

    this.source = new CommonSource(data);
    this.cmp = new EbOrderSearchSource();

    this.cmp.txt_palletNo.allowBlank = false;
    this.source.com_lazerPrinter.allowBlank = false;
    this.form = Ext.create("Ext.form.Panel", {
        title: '订单打印',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.cmp.txt_palletNo
                ]
            },
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.com_lazerPrinter
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
                text: '打印',
                scope: this,
                formBind: true,
                handler: function () {
                    var form = this.form.getForm();
                    if (form.isValid()) {
                        Ext.Msg.show({
                            title: '确认',
                            message: '确认开始打印订单?',
                            buttons: Ext.Msg.YESNO,
                            icon: Ext.Msg.QUESTION,
                            scope: this,
                            fn: function (btn) {
                                if (btn === 'yes') {
                                    var printerId = this.source.com_lazerPrinter.getValue();
                                    var palletNo = this.cmp.txt_palletNo.getValue();

                                    mask.show();
                                    Ext.Ajax.request({
                                        url: 'wms/merginStatus/ebMerginPrint',
                                        method: 'POST',
                                        params: {printerId: printerId, palletNo: palletNo},
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
                                else {
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
            tooltip: '订单打印'
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