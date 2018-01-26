var SpecialOrderTransferPanel = function (data) {
    this.source = new CommonSource(data);
    this.cmp = new SpecialOrderTransferSource();

    this.source.txt_retrievalOrderNo.allowBlank = false;
    this.cmp.com_transferType.allowBlank = false;

    this.form = Ext.create("Ext.form.Panel", {
        title: '特渠出库单转储',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.txt_retrievalOrderNo
                ]
            },
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.cmp.com_transferType
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
                text: '转储',
                scope: this,
                formBind: true,
                handler: function () {
                    var form = this.form.getForm();
                    if (form.isValid()) {
                        var retrievalOrderNo = this.source.txt_retrievalOrderNo.getValue();
                        var transferType = this.cmp.com_transferType.getValue();
                        Ext.Msg.show({
                            title: '确认',
                            message: '确定转储？',
                            buttons: Ext.Msg.YESNO,
                            icon: Ext.Msg.QUESTION,
                            scope: this,
                            fn: function (btn) {
                                if (btn === 'yes') {
                                    mask.show();
                                    Ext.Ajax.request({
                                        url: 'wms/specialOrderTransfer/transfer',
                                        method: 'POST',
                                        params: {'retrievalOrderNo': retrievalOrderNo, 'transferType': transferType},
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
            tooltip: '特渠出库单转储'
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