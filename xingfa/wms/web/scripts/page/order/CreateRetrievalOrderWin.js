var CreateRetrievalOrderWin = function (data) {

    this.source = new CommonSource(data);
    this.cmp = new CreateRetrievalOrderSource();

    this.parentStore = null;

    this.source.com_outletId.allowBlank = false;
    this.source.com_wareHouseId.allowBlank = false;
    this.cmp.com_orderType.allowBlank = false;

    this.init = function (parentStore) {
        this.parentStore = parentStore;
    };

    this.form = Ext.create("Ext.form.Panel", {
        defaults: {anchor: "100%", allowBlank: false},
        items: [
            this.source.com_outletId,
            this.source.com_wareHouseId,
            this.cmp.com_orderType
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
                text: '创建',
                scope: this,
                formBind: true,
                disabled: true,
                handler: function () {
                    Ext.Msg.show({
                        title: '确认',
                        message: '确认创建出库单？',
                        buttons: Ext.Msg.YESNO,
                        icon: Ext.Msg.QUESTION,
                        scope: this,
                        fn: function (btn) {
                            if (btn === 'yes') {
                                var form = this.form.getForm();
                                if (form.isValid()) {
                                    if (this.parentStore.getCount() == 0) {
                                        Ext.warn("列表数据不能为空");
                                        return;
                                    }
                                    var results = [];
                                    for (var i = 0; i < this.parentStore.getCount(); i++) {
                                        var rowStore = this.parentStore.getAt(i);
                                        var rowData = rowStore.data;
                                        results.push({
                                            'skuCode': rowData.skuCode,
                                            'skuEom': rowData.skuEom,
                                            'planQty': rowData.planQty
                                        });
                                    }

                                    var outletId = this.source.com_outletId.getValue();
                                    var wareHouseId = this.source.com_wareHouseId.getValue();
                                    var orderType = this.cmp.com_orderType.getValue();

                                    mask.show();
                                    Ext.Ajax.request({
                                        url: 'wms/createRetrievalOrder/create?outletId='+outletId+'&wareHouseId='+wareHouseId+'&orderType='+orderType,
                                        headers: {'Content-Type': "application/json; charset=utf-8"},
                                        method: 'POST',
                                        params: Ext.util.JSON.encode(results),
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