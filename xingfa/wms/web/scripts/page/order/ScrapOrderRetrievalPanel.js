var ScrapOrderRetrievalPanel = function (data) {
    this.source = new CommonSource(data);

    this.source.txt_retrievalOrderNo.allowBlank = false;

    this.form = Ext.create("Ext.form.Panel", {
        title: '出库确认',
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
                text: '出库确认',
                scope: this,
                handler: function () {
                    var form = this.form.getForm();
                    if (form.isValid()) {
                        Ext.Msg.show({
                            title: '确认',
                            message: '确认出库？',
                            buttons: Ext.Msg.YESNO,
                            icon: Ext.Msg.QUESTION,
                            scope: this,
                            fn: function (btn) {
                                if (btn === 'yes') {
                                    var retrievalOrderNo = this.source.txt_retrievalOrderNo.getValue();
                                    if (retrievalOrderNo == null) {
                                        Ext.warn('出库单号不能为空');
                                        return;
                                    }

                                    mask.show();
                                    Ext.Ajax.request({
                                        url: 'wms/scrapOrderRetrieval/confirm',
                                        method: 'POST',
                                        params: {retrievalOrderNo: retrievalOrderNo},
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
            tooltip: '出库确认'
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