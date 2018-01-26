var TransferOrderSettingPanel = function (data) {
    this.source = new CommonSource(data);

    this.source.txt_retrievalOrderNo.allowBlank = false;
    this.source.txt_retrievalOrderNo.fieldLabel = '<font style="color: red;">*</font>' + this.source.txt_retrievalOrderNo.fieldLabel;

    this.form = Ext.create("Ext.form.Panel", {
        title: '转仓发货(311-WMS)设定',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.txt_retrievalOrderNo
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
                text: '开始转移库存',
                scope: this,
                formBind: true,
                handler: function () {
                    var form = this.form.getForm();
                    if (form.isValid()) {
                        var retrievalOrderNo = this.source.txt_retrievalOrderNo.getValue();
                        Ext.Msg.show({
                            title: '确认',
                            message: '确定结束该张出库单,并发起库存转移请求？',
                            buttons: Ext.Msg.YESNO,
                            icon: Ext.Msg.QUESTION,
                            scope: this,
                            fn: function (btn) {
                                if (btn === 'yes') {
                                    mask.show();
                                    Ext.Ajax.request({
                                        url: 'wms/transferOrderSetting/confirm',
                                        method: 'POST',
                                        params: {'retrievalOrderNo': retrievalOrderNo},
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
            tooltip: '转仓发货(311-WMS)设定'
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