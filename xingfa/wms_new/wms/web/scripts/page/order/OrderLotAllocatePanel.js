var OrderLotAllocatePanel = function (data) {

    this.source = new CommonSource(data);
    this.source.txt_retrievalOrderNo.allowBlank = false;

    var allocateBtn = Ext.create("Ext.button.Button", {
            text: "分配库存",
            scope: this,
            handler: function () {
                var form = this.form.getForm();
                if (form.isValid()) {
                    Ext.Msg.show({
                        title: '确认',
                        message: '确认分配？',
                        buttons: Ext.Msg.YESNO,
                        icon: Ext.Msg.QUESTION,
                        scope: this,
                        fn: function (btn) {
                            if (btn === 'yes') {

                                var sels = this.grid.getSelection();

                                if (sels.length == 0) {
                                    Ext.warn('请至少选择一条数据');
                                    return;
                                }

                                var orderNo = this.source.txt_retrievalOrderNo.getValue();
                                if (orderNo == null) {
                                    Ext.warn('出库单号不能为空');
                                    return;
                                }

                                var win = new OrderLotAllocateWin(sels, orderNo);
                                win.win.title = "分配明细";
                                win.init();
                                win.pareStore = this.store;
                                win.win.show();

                            }
                            else {
                                //todo cancel
                            }
                        }
                    });
                }
            }
        }
    );

    var impForm = Ext.create("Ext.form.Panel", {
        layout: 'column',
        scope: this,
        items: [
            allocateBtn
        ]
    });

    this.form = Ext.create("Ext.form.Panel", {
        title: '订单指定批次分配',
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
                text: '查询',
                scope: this,
                formBind: true,
                handler: function () {
                    mask.show();
                    this.store.loadPage(1);
                }
            }
        ]
    });

    this.store = Ext.create("Ext.data.JsonStore", {
        scope: this,
        fields: ['id', 'locationNo', 'logicCode', 'containerCode', 'qty', 'eom', 'skuCode', 'skuName', 'lotNo'],
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            },
            load: function (store, obj) {
                mask.hide();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/orderLotAllocate/searchInventory",
            actionMethods: {read: "POST"},
            reader: {
            type: "json",
            rootProperty: "res",
            totalProperty: "count"
        },
        listeners: {
            exception: function (thiz, request) {
                mask.hide();
                extStoreLoadFail(thiz, request);
            }
        }
    }
})
    ;

    this.sm = Ext.create("Ext.selection.CheckboxModel", {
        checkOnly: true
    });

    this.grid = Ext.create("Ext.grid.Panel", {
            store: this.store,
            scope: this,
            selModel: this.sm,
            viewConfig: {
                enableTextSelection: true
            },
            columns: [
                {text: "ID", dataIndex: "id", width: "5%", hidden: true},
                {text: "商品代码", dataIndex: "skuCode", width: "10%"},
                {text: "商品名称", dataIndex: "skuName", width: "10%"},
                {text: "容器号", dataIndex: "containerCode", width: "10%"},
                {text: "货位号", dataIndex: "locationNo", width: "10%"},
                {text: "作业区", dataIndex: "logicCode", width: "10%"},
                {text: "批次", dataIndex: "lotNo", width: "10%"},
                {
                    text: "数量",
                    dataIndex: "qty",
                    width: "10%",
                    scope: this,
                    editor: {
                        xtype: 'numberfield',
                        allowBlank: false, minValue: 0
                    }
                },
                {text: "单位", dataIndex: "eom", width: "10%"}
            ],
            listeners: {},
            selType: 'cellmodel',
            plugins: {
                ptype: 'cellediting',
                clicksToEdit: 2
            },
            viewConfig: {
                enableTextSelection: true
            },
            dockedItems: [
                {
                    xtype: "toolbar",
                    dock: "top",
                    scope: this,
                    items: [impForm]
                }
            ]
        }
    );


    this.panel = Ext.create("Ext.panel.Panel", {
        tabConfig: {
            tooltip: '订单指定批次分配'
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