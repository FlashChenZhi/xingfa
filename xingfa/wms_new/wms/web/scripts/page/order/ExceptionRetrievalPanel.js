var ExceptionRetrievalPanel = function (data) {
    this.source = new CommonSource(data);
    this.cmp = new InventorySrouce();

    this.source.com_ownerCode.allowBlank = false;

    this.form = Ext.create("Ext.form.Panel", {
        title: '例外出库',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.com_ownerCode,
                    this.source.com_skuCode
                ]
            },
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.cmp.com_qaStatus,
                    this.source.date_productDate
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
                handler: function () {
                    var form = this.form.getForm();
                    if (form.isValid()) {
                        this.store.load();
                    }

                }
            }
        ]
    });

    this.store = Ext.create("Ext.data.JsonStore", {
        scope: this,
        fields: ['inventoryId', 'locationNo', 'containerBarcode', 'providerCode', 'providerName', 'ownerCode', 'ownerName', 'skuCode', 'skuName', 'qty', 'eom', 'productDate', 'qaStatus', 'frozenType', 'frozenFlag', 'reservedType', 'reservedFlag', 'returnSkuFlag', 'serialNumber', 'memo'],
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/exceptionOrderCreate/search",
            actionMethods: {read: "POST"},
            reader: {
                type: "json",
                rootProperty: "res",
                totalProperty: "count"
            },
            listeners: {
                exception: function (thiz, request) {
                    extStoreLoadFail(thiz, request);

                }
            }
        }
    });

    this.finishBtn = Ext.create("Ext.button.Button", {
        text: "例外出库",
        scope: this,
        handler: function () {

            var sels = this.grid.getSelection();
            if (sels.length == 0) {
                Ext.warn('请至少选择一条数据');
                return;
            }
            var results = [];
            for (var res in sels) {
                var rowData = sels[res].data;
                results.push({
                    'inventoryId': rowData.inventoryId
                });
            }

            var win = new ExceptionCreatWin(data);
            win.init(this.store,results);
            win.win.show();
        }
    });


    var qaStatusMap = this.cmp.qaStatusMap();
    var reservedTypeMap = this.cmp.reservedTypeMap();
    var yesOrNoMap = this.cmp.yesOrNoMap();
    var frozenTypeMap = this.cmp.frozenTypeMap();

    this.sm = Ext.create("Ext.selection.CheckboxModel", {
        checkOnly: true
    });

    this.grid = Ext.create("Ext.grid.Panel", {
            selModel: this.sm,
            store: this.store,
            scope: this,
            columns: [
                {text: "ID", dataIndex: "inventoryId", hidden: true},
                {text: "作业区代码", dataIndex: "logicZoneCode"},
                {text: "作业区名称", dataIndex: "logicZoneName"},
                {text: "货位号", dataIndex: "locationNo"},
                {text: "容器号", dataIndex: "containerBarcode"},
                {text: "货主代码", dataIndex: "ownerCode"},
                {text: "货主名称", dataIndex: "ownerName"},
                {text: "商品代码", dataIndex: "skuCode"},
                {text: "商品名称", dataIndex: "skuName"},
                {text: "数量", dataIndex: "qty"},
                {text: "单位", dataIndex: "eom"},
                {text: "生产日期", dataIndex: "productDate"},
                {
                    text: "质检状态", dataIndex: "qaStatus", scope: this, renderer: function (value) {
                    return qaStatusMap.get(value);
                }
                },
                {
                    text: "冻结类型", dataIndex: "frozenType", scope: this, renderer: function (value) {
                    return frozenTypeMap.get(value);
                }
                },
                {
                    text: "是否冻结", dataIndex: "frozenFlag", scope: this, renderer: function (value) {
                    return yesOrNoMap.get(value);
                }
                },
                {
                    text: "预约类型", dataIndex: "reservedType", scope: this, renderer: function (value) {
                    return reservedTypeMap.get(value);
                }
                },
                {
                    text: "是否预约", dataIndex: "reservedFlag", scope: this, renderer: function (value) {
                    return yesOrNoMap.get(value);
                }
                },
                {text: "流水号", dataIndex: "serialNumber"},
                {text: "备注", dataIndex: "memo", flex: 1}
            ],
            listeners: {},
            selType: 'cellmodel',
            viewConfig: {
                enableTextSelection: true
            },
            dockedItems: [
                {
                    xtype: "toolbar",
                    dock: "top",
                    scope: this,
                    items: [
                        this.finishBtn
                    ]
                }
            ]
        }
    );


    this.panel = Ext.create("Ext.panel.Panel", {
        tabConfig: {
            tooltip: '例外出库'
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