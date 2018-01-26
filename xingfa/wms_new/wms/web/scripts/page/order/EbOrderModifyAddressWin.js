var EbOrderModifyAddressWin = function (data) {

    this.source = new CommonSource(data);

    this.orderNo = null;
    this.address = null;

    this.source.txt_retrievalOrderNo.allowBlank = false;
    this.source.txt_address.allowBlank = false;

    this.init = function (orderNo, address) {
        this.source.txt_retrievalOrderNo.setValue(orderNo);
        this.source.txt_address.setValue(address);
    };

    this.form = Ext.create("Ext.form.Panel", {
        defaults: {anchor: "100%", allowBlank: false},
        items: [
            this.source.txt_retrievalOrderNo,//retrievalOrderNo
            this.source.txt_address
        ],
        scope: this,
        buttons: [
            {
                text: '取消',
                scope: this,
                handler: function () {
                    this.form.getForm().reset();
                    this.win.hide();
                }
            },
            {
                text: '修改',
                scope: this,
                formBind: true,
                disabled: true,
                handler: function () {
                    Ext.Msg.show({
                        title: '确认',
                        message: '确认修改地址？',
                        buttons: Ext.Msg.YESNO,
                        icon: Ext.Msg.QUESTION,
                        scope: this,
                        fn: function (btn) {
                            if (btn === 'yes') {
                                var form = this.form.getForm();
                                if (form.isValid()) {
                                    var orderNo = this.source.txt_retrievalOrderNo.getValue();
                                    var address = this.source.txt_address.getValue();

                                    mask.show();
                                    Ext.Ajax.request({
                                        url: 'wms/ebOrderSearch/updateAddress',
                                        method: 'POST',
                                        params: {'orderNo': orderNo, 'address': address},
                                        scope: this,
                                        success: function (response, options) {
                                            mask.hide();
                                            var result = Ext.util.JSON.decode(response.responseText);
                                            if (result.success) {
                                                Ext.success(result.msg);
                                                this.win.hide();
                                                this.parentStore.removeAll();
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
                            }
                        }
                    });
                }
            }
        ]
    });

    this.win = Ext.create("Ext.window.Window", {
        width: 500,
        bodyPadding: 5,
        resizable: false,
        modal: true,
        closeAction: "hide",
        scope: this,
        items: this.form,
    });

    var mask = new Ext.LoadMask({
        msg: '系统处理中...',
        target: this.win
    });

};