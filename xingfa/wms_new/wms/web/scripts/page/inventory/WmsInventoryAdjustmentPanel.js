var WmsInventoryAdjustmentPanel = function (data) {
    this.source = new CommonSource(data);
    this.cmp = new InventorySrouce();

    this.invQcPanel = Ext.create("Ext.form.Panel", {
        title: '库存属性条件',
        layout: 'column',
        scope: this,
        collapsible: true,
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {anchor: '100%'},
                items: [
                    this.cmp.com_frozenFlag,
                    this.cmp.com_returnSkuFlag,
                    this.source.date_productDate,
                    this.source.txt_receivingOrderNo
                ]
            },
            {
                layout: 'form', columnWidth: .5,
                items: [
                    this.cmp.com_reservedFlag,
                    this.cmp.com_qaStatus,
                    this.source.txt_batchNo,
                    this.source.txt_retrievalOrderNo
                ]
            }
        ]
    });


    this.locQcPanel = Ext.create("Ext.form.Panel", {
        title: '库存地点条件',
        layout: 'column',
        scope: this,
        collapsible: true,
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {anchor: '100%'},
                items: [
                    this.source.com_logicZoneCode,
                    this.source.txt_locationNo,
                    this.source.com_blockFlag,
                    this.source.com_restrictedFlag,
                    this.source.com_abnormalFlag,
                    this.cmp.com_printLogisticsBarcodeFlag

                ]
            },
            {
                layout: 'form', columnWidth: .5,
                items: [
                    this.source.com_tempLocationNo,
                    this.source.txt_containerBarcode,
                    this.source.txt_dpsAlias,
                    this.source.txt_bayNo,
                    this.source.txt_bankNo,
                    this.source.txt_levelNo
                ]
            }
        ]
    });

    this.skuQcPanel = Ext.create("Ext.form.Panel", {
        title: '货主商品条件',
        layout: 'column',
        scope: this,
        collapsible: true,
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {anchor: '100%'},
                items: [
                    this.source.com_ownerCode,
                    this.cmp.com_onTransferLineFlag
                ]
            },
            {
                layout: 'form', columnWidth: .5,
                items: [
                    this.source.com_skuCode
                ]
            }
        ]
    });

    this.source.com_blockFlag.fieldLabel = '货位是否冻结';
    this.source.com_restrictedFlag.fieldLabel = '货位是否限制';
    this.source.com_abnormalFlag.fieldLabel = '货位是否故障';

    this.tabs = Ext.create('Ext.tab.Panel', {
        columnWidth: 1,
        items: [this.locQcPanel, this.skuQcPanel, this.invQcPanel]
    });

    this.form = Ext.create("Ext.form.Panel", {
        title: 'WMS内部库存维护',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: this.tabs,
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
                    this.store.loadPage(1);
                }
            }
        ]
    });

    this.store = Ext.create("Ext.data.JsonStore", {
        scope: this,
        fields: ['inventoryId', 'wareHouseSapCode', 'zoneCode', 'zoneName', 'logicZoneCode', 'logicZoneName', 'locationNo', 'containerBarcode', 'providerCode', 'providerName', 'ownerCode', 'ownerName', 'skuCode', 'skuName', 'qty', 'eom', 'productDate', 'qaStatus', 'frozenType', 'frozenFlag', 'reservedType', 'reservedFlag',
            'returnSkuFlag', 'returnSkuType', 'serialNumber', 'memo', 'retrievalOrderBatchNo', 'retrievalOrderNo', 'logisticsBarcode', 'containerType', 'receivingOrderNo', 'dpsAlias', 'containerReservedFlag', 'containerReservedType'],
        pageSize: 20,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/wmsInventoryAdjustment/search",
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

    var containerTypeMap = this.cmp.containerTypeMap();
    var returnSkuTypeMap = this.cmp.returnSkuTypeMap();
    var qaStatusMap = this.cmp.qaStatusMap();
    var reservedTypeMap = this.cmp.reservedTypeMap();
    var containerReservedTypeMap = this.cmp.containerReservedTypeMap();
    var yesOrNoMap = this.cmp.yesOrNoMap();
    var frozenTypeMap = this.cmp.frozenTypeMap();

    this.grid = Ext.create("Ext.grid.Panel", {
            store: this.store,
            scope: this,
            viewConfig: {
                enableTextSelection: true
            },
            columns: [
                {text: "ID", dataIndex: "inventoryId", hidden: true},
                {text: "货位号", dataIndex: "locationNo"},
                {text: "容器号", dataIndex: "containerBarcode"},
                {
                    text: "容器类型", dataIndex: "containerType", scope: this, renderer: function (value) {
                    return containerTypeMap.get(value);
                }
                },
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
                {text: "DPS地址", dataIndex: "dpsAlias"},
                {text: "物流码", dataIndex: "logisticsBarcode"},
                {text: "仓库代码", dataIndex: "wareHouseSapCode"},
                {text: "区域代码", dataIndex: "zoneCode"},
                {text: "区域名称", dataIndex: "zoneName"},
                {text: "作业区代码", dataIndex: "logicZoneCode"},
                {text: "作业区名称", dataIndex: "logicZoneName"},
                {text: "供应商代码", dataIndex: "providerCode"},
                {text: "供应商名称", dataIndex: "providerName"},
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
                {
                    text: "容器预约类型", dataIndex: "containerReservedType", scope: this, renderer: function (value) {
                    return containerReservedTypeMap.get(value);
                }
                },
                {
                    text: "容器是否预约", dataIndex: "containerReservedFlag", scope: this, renderer: function (value) {
                    return yesOrNoMap.get(value);
                }
                },
                {text: "流水号", dataIndex: "serialNumber"},
                {text: "收货单号", dataIndex: "receivingOrderNo"},
                {text: "波次号", dataIndex: "retrievalOrderBatchNo"},
                {text: "订单号", dataIndex: "retrievalOrderNo"},
                {text: "备注", dataIndex: "memo", flex: 1}
            ],
            listeners: {
                scope: this,
                rowdblclick: function (thiz, record, tr, rowIndex, e, eOpts) {
                },
                rowcontextmenu: function (thiz, record, tr, rowIndex, e, eOpts) {
                    e.preventDefault();
                    if (rowIndex < 0) {
                        return;
                    }
                    var menu = Ext.create("Ext.menu.Menu", {
                        items: [
                            {
                                text: "调整",
                                scope: this,
                                handler: function () {
                                    var win = new WmsInventoryAdjustmentWin(data);
                                    var rowData = record.data;
                                    win.init(rowData, this.store);
                                    win.win.show();
                                }
                            }
                        ]
                    });
                    menu.showAt(e.getPoint());
                }
            },
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
                        {
                            xtype: "button",
                            text: "空货位维护库存",
                            scope: this,
                            handler: function () {
                                var win = new WmsInventoryAddWin(data, this.store);
                                win.win.title = "空货位维护库存";
                                win.win.show();
                            }
                        }
                    ]
                },
                {
                    xtype: "pagingtoolbar",
                    store: this.store,
                    dock: "bottom",
                    displayInfo: true
                }
            ]
        }
    );


    this.panel = Ext.create("Ext.panel.Panel", {
        tabConfig: {
            tooltip: 'WMS内部库存维护'
        },
        overflowY: 'auto',
        items: [
            this.form, this.grid
        ]
    });

};