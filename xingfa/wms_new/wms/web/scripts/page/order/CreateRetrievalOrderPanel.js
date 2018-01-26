var CreateRetrievalOrderPanel = function (data) {
    this.cmp = new CreateRetrievalOrderSource();
    this.source = new CommonSource(data);


    this.source.com_skuCode.allowBlank = false;
    this.cmp.txt_planQty.allowBlank = false;
    this.form = Ext.create("Ext.form.Panel", {
        title: '新建手工出库单',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.com_skuCode
                ]
            },
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.cmp.txt_planQty
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
                text: '添加',
                scope: this,
                handler: function () {
                    var form = this.form.getForm();
                    if (form.isValid()) {
                        var skuCode = this.source.com_skuCode.getValue();
                        var planQty = this.cmp.txt_planQty.getValue();
                        mask.show();
                        Ext.Ajax.request({
                            url: 'wms/createRetrievalOrder/getSkuEom',
                            method: 'POST',
                            params: {skuCode: skuCode},
                            scope: this,
                            success: function (response, options) {
                                mask.hide();
                                var result = Ext.util.JSON.decode(response.responseText);
                                if (result.success) {
                                    this.store.add({
                                        "skuCode": skuCode,
                                        "planQty": planQty,
                                        "skuEom": result.res
                                    });
                                    this.form.reset();
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
        ]
    });

    this.store = Ext.create("Ext.data.JsonStore", {
        fields: ['skuCode', 'skuEom', 'planQty'],
        autoLoad: false,
        scope: this
    });

    var finishBtn = Ext.create("Ext.button.Button", {
        text: "保存",
        scope: this,
        handler: function () {
            var win = new CreateRetrievalOrderWin(data);
            win.init(this.store);
            win.win.show();
        }
    });

    var file_excel = Ext.create("Ext.form.field.File", {
        name: 'file',
        //fieldLabel: '文件',
        //id:'file',
        labelWidth: 50,
        msgTarget: 'side',
        allowBlank: false,
        margin: '0 5 0 0',
        buttonText: '打开'
    });

    var exportUserLocBtn = Ext.create("Ext.button.Button", {
            text: "导出模板",
            scope: this,
            handler: function () {
                var exporturl = 'wms/createRetrievalOrder/exportExcelTemplate';
                window.location.href = exporturl;
            }
        }
    );

    var importLocBtn = Ext.create("Ext.button.Button", {
        text: "订单明细导入",
        formBind: true,
        scope: this,
        handler: function () {
            if (impForm.isValid()) {
                impForm.submit({
                    timeout: 300,
                    headers: [
                        {"Request-By": "Ext"}
                    ],
                    url: 'wms/createRetrievalOrder/importExcel',
                    scope: this,
                    success: function (form, action) {
                        this.store.removeAll();
                        Ext.MessageBox.hide();
                        window.clearInterval(window.intervalId);
                        Ext.success(action.result.msg);

                        for (var r in action.result.res) {
                            var data = action.result.res[r];
                            this.store.add({
                                "skuCode": data.skuCode,
                                "planQty": data.planQty,
                                "skuEom": data.skuEom
                            });
                        }
                    },
                    failure: function (form, action) {
                        Ext.MessageBox.hide();
                        window.clearInterval(window.intervalId);
                        switch (action.failureType) {
                            case Ext.form.action.Action.CLIENT_INVALID:
                                Ext.error('Form fields may not be submitted with invalid values');
                                break;
                            case Ext.form.action.Action.CONNECT_FAILURE:
                                Ext.error('Ajax communication failed');
                                break;
                            case Ext.form.action.Action.SERVER_INVALID:
                                Ext.error(action.result.msg);
                        }
                    }
                });
                var path = file_excel.getValue();
                var index = path.lastIndexOf("\\") + 1;
                var fileName = path.substring(index);
                Ext.MessageBox.show({
                    title: '数据导入',
                    msg: '导入' + fileName,
                    progressText: '正在处理...',
                    progress: true,
                    closable: false
                });
                window.intervalId = setInterval("getProcess()", 1000);
            }
        }
    });

    var impForm = Ext.create("Ext.form.Panel", {
        layout: 'column',
        scope: this,
        items: [
            file_excel,
            importLocBtn,
            exportUserLocBtn,
            finishBtn
        ]
    });

    this.grid = Ext.create("Ext.grid.Panel", {
            store: this.store,
            scope: this,
            columns: [
                {text: "商品代码", dataIndex: "skuCode", width: "50%"},
                {text: "预定数量", dataIndex: "planQty", width: "25%"},
                {text: "商品单位", dataIndex: "skuEom", width: "25%"}
            ],
            viewConfig: {
                enableTextSelection: true
            },
            //listeners: {
            //    scope: this,
            //    rowdblclick: function (thiz, record, tr, rowIndex, e, eOpts) {
            //    },
            //    rowcontextmenu: function (thiz, record, tr, rowIndex, e, eOpts) {
            //        e.preventDefault();
            //        if (rowIndex < 0) {
            //            return;
            //        }
            //        var menu = Ext.create("Ext.menu.Menu", {
            //            items: [
            //                {
            //                    text: "删除", handler: function () {
            //                    console.log(this.store);
            //                    this.store.remove(record);
            //                }
            //                }
            //            ]
            //        });
            //        menu.showAt(e.getPoint());
            //    }
            //},
            selType: 'cellmodel',
            dockedItems: [
                {
                    xtype: "toolbar",
                    dock: "top",
                    scope: this,
                    items: [
                        impForm
                    ]
                }
            ]
        }
    );

    window.getProcess = function () {
        Ext.Ajax.request({
            url: 'wms/createRetrievalOrder/getImpProcess',
            headers: {'Content-Type': "application/json; charset=utf-8"},
            method: 'POST',
            success: function (response, options) {
                var result = Ext.util.JSON.decode(response.responseText).res;
                if (result['total'] == -1) {
                    return;
                }
                var percent = result['done'] / result['total'];
                Ext.MessageBox.updateProgress(percent, "正在处理..." + result['done'] + "/" + result['total']);
                if (percent == 1) {
                    clearInterval(window.intervalId);
                }
            },
            failure: function (response, options) {
                //do nothing
            }
        });
    };


    this.panel = Ext.create("Ext.panel.Panel", {
        tabConfig: {
            tooltip: '新建手工出库单'
        },
        overflowY: 'auto',
        items: [
            this.form, this.grid
        ]
    });

    var mask = new Ext.LoadMask({
        msg: '系统处理中...',
        target: this.panel
    });
};
