var InventoryBindWin = function (data) {

    this.source = new CommonSource(data);
    this.cmp = new InventorySrouce();

    this.cmp.com_containerType.allowBlank = false;
    this.source.txt_bindingContainerBarCode.allowBlank = false;
    this.source.txt_bindingQty.allowBlank = false;

    this.init = function (rowData) {

        this.cmp.txt_inventoryId.readOnly = true;
        this.source.txt_skuCode.readOnly = true;
        this.source.txt_skuName.readOnly = true;
        this.source.txt_eom.readOnly = true;
        this.cmp.txt_fromLocationNo.readOnly = true;


        this.cmp.txt_inventoryId.hide();

        this.cmp.txt_inventoryId.setValue(rowData.inventoryId);
        this.source.txt_skuCode.setValue(rowData.skuCode);
        this.source.txt_skuName.setValue(rowData.skuName);
        this.source.txt_eom.setValue(rowData.eom);
        this.cmp.txt_fromLocationNo.setValue(rowData.locationNo);

    };

    this.form = Ext.create("Ext.form.Panel", {
        defaults: {anchor: "100%", allowBlank: false},
        items: [
            this.cmp.txt_inventoryId,
            this.source.txt_skuCode,
            this.source.txt_skuName,
            this.source.txt_eom,
            this.cmp.txt_fromLocationNo,
            this.source.txt_bindingContainerBarCode,
            this.cmp.com_containerType,
            this.source.txt_bindingQty
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
                text: '绑定',
                scope: this,
                formBind: true,
                disabled: true,
                handler: function () {
                    Ext.Msg.show({
                        title: '确认',
                        message: '确认绑定？',
                        buttons: Ext.Msg.YESNO,
                        icon: Ext.Msg.QUESTION,
                        scope: this,
                        fn: function (btn) {
                            if (btn === 'yes') {
                                var form = this.form.getForm();
                                if (form.isValid()) {
                                    var inventoryId = this.cmp.txt_inventoryId.getValue();
                                    var bindingContainerBarCode = this.source.txt_bindingContainerBarCode.getValue();
                                    var containerType = this.cmp.com_containerType.getValue();
                                    var bindingQty = this.source.txt_bindingQty.getValue();

                                    mask.show();
                                    Ext.Ajax.request({
                                        url: 'wms/bindContainer/bind',
                                        method: 'POST',
                                        params: {
                                            inventoryId: inventoryId,
                                            bindingContainerBarCode: bindingContainerBarCode,
                                            containerType: containerType,
                                            bindingQty: bindingQty
                                        },
                                        scope: this,
                                        success: function (response, options) {
                                            mask.hide();
                                            var result = Ext.util.JSON.decode(response.responseText);
                                            if (result.success) {
                                                Ext.success(result.msg);
                                                this.win.hide();
                                                this.store.reload();
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
        listeners: {
            scope: this,
            'beforehide': function () {
                this.key = null;
                this.form.getForm().reset();
            }
        }
    });

    var mask = new Ext.LoadMask({
        msg: '系统处理中...',
        target: this.win
    });
};