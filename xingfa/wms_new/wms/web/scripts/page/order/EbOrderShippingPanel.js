var EbOrderShippingPanel = function () {
    this.cmp = new EbOrderSearchSource();

    this.cmp.txt_palletNo.allowBlank = false;
    this.form = Ext.create("Ext.form.Panel", {
        title: '订单重新称重',
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
        ],
        buttons: [
            {
                text: '重置',
                handler: function () {
                    this.up('form').getForm().reset();
                }
            },
            {
                text: '发货',
                scope: this,
                handler: function () {
                    var form = this.form.getForm();
                    if (form.isValid()) {
                        var palletNo = this.cmp.txt_palletNo.getValue();
                        Ext.Msg.show({
                            title: '确认',
                            message: '确定发货？',
                            buttons: Ext.Msg.YESNO,
                            icon: Ext.Msg.QUESTION,
                            scope: this,
                            fn: function (btn) {
                                if (btn === 'yes') {
                                    mask.show();
                                    Ext.Ajax.request({
                                        url: 'wms/ebShipping/shipping',
                                        method: 'POST',
                                        timeout: 120000,
                                        params: {
                                            'palletNo': palletNo
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
            tooltip: '电商发货'
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
