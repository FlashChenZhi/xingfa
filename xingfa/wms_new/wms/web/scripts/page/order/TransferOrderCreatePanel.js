var TransferOrderCreatePanel = function (data) {

    this.source = new CommonSource(data);
    this.cmp = new InventorySrouce();

    this.source.com_fromWareHouseId.allowBlank = false;
    this.source.com_skuCode.allowBlank = false;
    this.source.com_fromWareHouseId.fieldLabel = '<font style="color: red;">*</font>' + this.source.com_fromWareHouseId.fieldLabel;
    this.source.com_skuCode.fieldLabel = '<font style="color: red;">*</font>' + this.source.com_skuCode.fieldLabel;

    this.form = Ext.create("Ext.form.Panel", {
        title: '移库单下架开始',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.com_fromWareHouseId,
                    this.source.com_logicZoneCode,
                    this.source.txt_containerBarcode,
                    this.cmp.com_qaStatus
                ]
            },
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.com_skuCode,
                    this.source.txt_locationNo,
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
        fields: ['inventoryId', 'wareHouseSapCode', 'zoneCode', 'zoneName', 'logicZoneCode', 'logicZoneName', 'locationNo', 'containerBarcode', 'providerCode', 'providerName', 'ownerCode', 'ownerName', 'skuCode', 'skuName', 'qty', 'eom', 'productDate', 'qaStatus', 'frozenType', 'frozenFlag', 'reservedType', 'reservedFlag', 'returnSkuFlag', 'returnSkuType', 'serialNumber', 'memo'],
        pageSize: 20,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/transferOrderCreate/search",
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

    var win = null;
    this.finishBtn = Ext.create("Ext.button.Button", {
        text: "移库下架",
        scope: this,
        handler: function () {
            if (win == null) {
                win = new TransferOrderCreateWin(data);
            }
            var fromWareHouseId = this.source.com_fromWareHouseId.getValue();
            var skuCode = this.source.com_skuCode.getValue();
            win.init(fromWareHouseId, skuCode, this.store, this.grid);
            win.win.show();
        }
    });


    var returnSkuTypeMap = this.cmp.returnSkuTypeMap();
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
                {text: "仓库代码", dataIndex: "wareHouseSapCode"},
                {text: "区域代码", dataIndex: "zoneCode"},
                {text: "区域名称", dataIndex: "zoneName"},
                {text: "作业区代码", dataIndex: "logicZoneCode"},
                {text: "作业区名称", dataIndex: "logicZoneName"},
                {text: "货位号", dataIndex: "locationNo"},
                {text: "容器号", dataIndex: "containerBarcode"},
                {text: "供应商代码", dataIndex: "providerCode"},
                {text: "供应商名称", dataIndex: "providerName"},
                {text: "货主代码", dataIndex: "ownerCode"},
                {text: "货主名称", dataIndex: "ownerName"},
                {text: "商品代码", dataIndex: "skuCode"},
                {text: "商品名称", dataIndex: "skuName"},
                {
                    text: "数量", dataIndex: "qty", scope: this,
                    editor: {
                        xtype: 'numberfield',
                        allowBlank: false, minValue: 0
                    }
                },
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
                {
                    text: "是否退货品", dataIndex: "returnSkuFlag", scope: this, renderer: function (value) {
                    return yesOrNoMap.get(value);
                }
                },
                {
                    text: "退品类型", dataIndex: "returnSkuType", scope: this, renderer: function (value) {
                    return returnSkuTypeMap.get(value);
                }
                },
                {text: "流水号", dataIndex: "serialNumber"},
                {text: "备注", dataIndex: "memo", flex: 1}
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
                    items: [
                        this.finishBtn
                    ]
                }
            ]
        }
    );


    this.panel = Ext.create("Ext.panel.Panel", {
        tabConfig: {
            tooltip: '移库单下架开始'
        },
        overflowY: 'auto',
        items: [
            this.form, this.grid
        ]
    });

};