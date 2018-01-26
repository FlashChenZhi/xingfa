var InventoryPreWarningSearchPanel = function (data) {
    this.source = new CommonSource(data);
    this.cmp = new InventorySrouce();

    var exportExcelBtn = Ext.create("Ext.button.Button", {
            text: "导出",
            scope: this,
            handler: function () {
                var exceedLastRetrievalDate = this.cmp.com_exceedLastRetrievalDate.getValue();
                if (exceedLastRetrievalDate == null) {
                    exceedLastRetrievalDate = '';
                }
                var exceedBestSellDate = this.cmp.com_exceedBestSellDate.getValue();
                if (exceedBestSellDate == null) {
                    exceedBestSellDate = '';
                }
                var overLotNo = this.cmp.com_overLotNoType.getValue();
                if (overLotNo == null) {
                    overLotNo = '';
                }
                var overShipDate = this.cmp.num_overShipDate.getValue();
                if (overShipDate == null) {
                    overShipDate = '';
                }
                var overBestSellDate = this.cmp.num_overBestSellDate.getValue();
                if (overBestSellDate == null) {
                    overBestSellDate = '';
                }
                var skuCode = this.source.com_skuCode.getValue();
                if(skuCode == null){
                    skuCode = '';
                }
                var exporturl = 'wms/inventoryPreWarningSearch/exportExcel?exceedLastRetrievalDate=' + exceedLastRetrievalDate + '&exceedBestSellDate=' + exceedBestSellDate +
                    '&overLotNo=' + overLotNo + '&overShipDate=' + overShipDate + '&overBestSellDate=' + overBestSellDate + '&skuCode='+skuCode;
                window.location.href = exporturl;
            }
        }
    );

    var impForm = Ext.create("Ext.form.Panel", {
        layout: 'column',
        scope: this,
        items: [
            exportExcelBtn
        ]
    });


    this.form = Ext.create("Ext.form.Panel", {
        title: '库存预警查询',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.cmp.com_exceedLastRetrievalDate,
                    this.cmp.com_exceedBestSellDate,
                    this.cmp.com_overLotNoType
                ]
            },
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.cmp.num_overShipDate,
                    this.cmp.num_overBestSellDate,
                    this.source.com_skuCode
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
                    var form = this.form.getForm();
                    if (form.isValid()) {
                        var exceedLastRetrievalDate = this.cmp.com_exceedLastRetrievalDate.getValue();
                        var exceedBestSellDate = this.cmp.com_exceedBestSellDate.getValue();
                        var lotNoDate = this.cmp.com_overLotNoType.getValue();
                        if (exceedLastRetrievalDate == null && exceedBestSellDate == null && lotNoDate == null) {
                            Ext.warn('请至少输入一个查询条件');
                            return;
                        }
                        this.store.load({
                            callback: function (record, options, success) {
                                if (record.length == 0) {
                                    Ext.success('无预警库存数据');
                                }
                            }
                        });
                    }
                }
            }
        ]
    });

    this.store = Ext.create("Ext.data.JsonStore", {
        scope: this,
        fields: ['locationNo', 'ownerCode', 'ownerName', 'skuCode', 'bestSellDate', 'lastRetrievalDate', 'expireDate', 'skuName', 'eom', 'totalQty', 'qaStatus', 'productDate'],
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/inventoryPreWarningSearch/search",
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

    var qaStatusMap = this.cmp.qaStatusMap();

    this.grid = Ext.create("Ext.grid.Panel", {
            store: this.store,
            scope: this,
            viewConfig: {
                enableTextSelection: true
            },
            columns: [
                {text: "货位号", dataIndex: "locationNo", width: "15%"},
                {text: "货主代码", dataIndex: "ownerCode", width: "10%"},
                {text: "货主名称", dataIndex: "ownerName", width: "15%"},
                {text: "商品代码", dataIndex: "skuCode", width: "10%"},
                {text: "商品名称", dataIndex: "skuName", width: "20%"},
                {text: "生产日期", dataIndex: "productDate", width: "10%"},
                {text: "最佳销售期", dataIndex: "bestSellDate", width: "10%"},
                {text: "最晚出库日", dataIndex: "lastRetrievalDate", width: "10%"},
                {text: "过期日", dataIndex: "expireDate", width: "10%"},

                {
                    text: "库存总数", dataIndex: "totalQty", width: "10%", scope: this, summaryType: 'sum',
                    summaryRenderer: function (value, summaryData, dataIndex) {
                        return Ext.String.format('{0}{1}', '总数：', value);
                    }
                },
                {
                    text: "库存状态", dataIndex: "qaStatus", width: "10%", scope: this, renderer: function (value) {
                    return qaStatusMap.get(value);
                }
                },
                {text: "库存单位", dataIndex: "eom", width: "10%"}
            ],
            listeners: {},
            selType: 'cellmodel',
            viewConfig: {
                enableTextSelection: true
            },
            features: [
                {
                    ftype: 'summary'
                }
            ],
            dockedItems: [
                {
                    xtype: "toolbar",
                    dock: "top",
                    scope: this,
                    items: [impForm]
                },
                {
                    xtype: "toolbar",
                    dock: "top",
                    scope: this,
                    items: []
                }
            ]
        }
    );


    this.panel = Ext.create("Ext.panel.Panel", {
        tabConfig: {
            tooltip: '库存预警查询'
        },
        overflowY: 'auto',
        items: [
            this.form, this.grid
        ]
    });

};