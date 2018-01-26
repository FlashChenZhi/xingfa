var InventoryLotChangeWin = function (data) {

    this.source = new CommonSource(data);
    this.cmp = new InventorySrouce();

    this.parentStore = null;

    this.cmp.com_adjustmentType.allowBlank = false;

    this.init = function (rowData, parentStore) {
        this.parentStore = parentStore;

        this.cmp.txt_inventoryId.disabled = true;
        this.cmp.txt_inventoryId.hide();

        this.cmp.txt_inventoryId.setValue(rowData.inventoryId);

    };

    this.form = Ext.create("Ext.form.Panel", {
        defaults: {anchor: "100%", allowBlank: false},
        items: [
            this.cmp.txt_inventoryId,
            this.source.date_productDate
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
                text: '变更',
                scope: this,
                formBind: true,
                disabled: true,
                handler: function () {
                    Ext.Msg.show({
                        title: '确认',
                        message: '确认变更？',
                        buttons: Ext.Msg.YESNO,
                        icon: Ext.Msg.QUESTION,
                        scope: this,
                        fn: function (btn) {
                            if (btn === 'yes') {
                                var form = this.form.getForm();
                                if (form.isValid()) {
                                    var inventoryId = this.cmp.txt_inventoryId.getValue();
                                    var productDate = productDate = Ext.Date.format(this.source.date_productDate.getValue(),'Y-m-d');

                                    mask.show();
                                    Ext.Ajax.request({
                                        url: 'wms/inventoryLotChange/change',
                                        method: 'POST',
                                        params: {inventoryId: inventoryId, productDate : productDate},
                                        scope: this,
                                        success: function (response, options) {
                                            mask.hide();
                                            var result = Ext.util.JSON.decode(response.responseText);
                                            if (result.success) {
                                                Ext.success(result.msg);
                                                this.win.hide();
                                                if(this.parentStore != null)
                                                    this.parentStore.loadPage(1);
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