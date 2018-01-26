var InventoryChangeWin = function (data) {

    this.source = new CommonSource(data);
    this.cmp = new InventorySrouce();

    this.parentStore = null;
    this.checkQuality;

    this.init = function (rowData, parentStore,checkQuality) {
        this.parentStore = parentStore;
        this.checkQuality = checkQuality;

        this.cmp.txt_inventoryId.disabled = true;
        this.cmp.txt_inventoryId.hide();

        this.cmp.txt_inventoryId.setValue(rowData.inventoryId);

    };

    this.form = Ext.create("Ext.form.Panel", {
        defaults: {anchor: "100%", allowBlank: false},
        items: [
            this.cmp.txt_inventoryId,
            this.cmp.com_changeQaStatus
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
                                    var qaStatus = this.cmp.com_changeQaStatus.getValue();
                                    mask.show();
                                    Ext.Ajax.request({
                                        url: 'wms/inventoryLotChange/changeQaStatus',
                                        method: 'POST',
                                        params: {inventoryId: inventoryId, qaStatus : qaStatus,checkQuality:this.checkQuality},
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