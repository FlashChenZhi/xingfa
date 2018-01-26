var TransferOrderCreateWin = function (data) {

    this.source = new CommonSource(data);
    this.cmp = new InventorySrouce();

    this.parentStore = null;
    this.parentGrid = null;
    this.fromWareHouseId = null;
    this.skuCode = null;

    this.cmp.com_transferType.allowBlank = false;
    this.init = function (fromWareHouseId, skuCode, parentStore, parentGrid) {
        this.fromWareHouseId = fromWareHouseId;
        this.skuCode = skuCode;
        this.parentStore = parentStore;
        this.parentGrid = parentGrid;

    };

    this.form = Ext.create("Ext.form.Panel", {
        defaults: {anchor: "100%", allowBlank: false},
        items: [
            this.source.com_toWareHouseId
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
                text: '移库下架',
                scope: this,
                formBind: true,
                disabled: true,
                handler: function () {
                    Ext.Msg.show({
                        title: '确认',
                        message: '确认创建移库单并触发下架任务？',
                        buttons: Ext.Msg.YESNO,
                        icon: Ext.Msg.QUESTION,
                        scope: this,
                        fn: function (btn) {
                            if (btn === 'yes') {
                                var form = this.form.getForm();
                                if (form.isValid()) {
                                    var results = [];
                                    var sels = this.parentGrid.getSelection();
                                    for (var res in sels) {
                                        results.push({inventoryId: sels[res].data.inventoryId, qty: sels[res].data.qty});
                                    }
                                    if (sels.length == 0) {
                                        Ext.warn('请至少选择一条数据');
                                        return;
                                    }
                                    var toWareHouseId = this.source.com_toWareHouseId.getValue();
                                    if (toWareHouseId == null) {
                                        Ext.warn('目标仓库不能为空');
                                        return;
                                    }
                                    var fromWareHouseId = this.fromWareHouseId;
                                    if (fromWareHouseId == null) {
                                        Ext.warn('源仓库不能为空');
                                        return;
                                    }
                                    var skuCode = this.skuCode;
                                    if (skuCode == null) {
                                        Ext.warn('商品代码不能为空');
                                        return;
                                    }
                                    mask.show();
                                    Ext.Ajax.request({
                                        url: 'wms/transferOrderCreate/start?fromWareHouseId=' + fromWareHouseId + "&skuCode=" + skuCode + "&toWareHouseId=" + toWareHouseId,
                                        headers: {'Content-Type': "application/json; charset=utf-8"},
                                        method: 'POST',
                                        params: Ext.util.JSON.encode(results),
                                        scope: this,
                                        timeout: 120000,
                                        success: function (response, options) {
                                            mask.hide();
                                            var result = Ext.util.JSON.decode(response.responseText);
                                            if (result.success) {
                                                Ext.Msg.alert('成功', result.msg);
                                                this.win.hide();
                                                if (this.parentStore != null)
                                                    this.parentStore.load();
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
        title: '移库下架开始',
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