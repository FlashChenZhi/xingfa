var InventoryTurnoverDaysPanel = function (data) {

    this.source = new CommonSource(data);

    this.form = Ext.create("Ext.form.Panel", {
        title: '物流商品周转天数',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.com_skuCode,
                    this.source.date_beginDate
                ]
            },
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.com_wareHouseId,
                    this.source.date_endDate
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
                    this.store.loadPage(1);
                }
            }
        ]
    });

    this.store = Ext.create("Ext.data.JsonStore", {
        scope: this,
        fields: ['ownerCode','storeArea', 'skuCode', 'skuName','turnRadio', 'shippedQty', 'shippedEom', 'receivedQty', 'receivedEom', 'currQty', 'avgQty', 'avgShippedQty', 'turnOverDays', 'allDays'],
        pageSize: 20,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/inventoryTurnOver/search",
            timeout: 960000,
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

    this.exportBtn = Ext.create("Ext.button.Button", {
        text: "导出",
        scope: this,
        handler: function () {
            var skuCode = this.source.com_skuCode.getValue();
            if (skuCode == null) {
                skuCode = '';
            }
            var wareHouseId = this.source.com_wareHouseId.getValue();
            if (wareHouseId == null) {
                wareHouseId = '';
            }
            var beginDate = this.source.date_beginDate.getSubmitValue();
            if (beginDate == null) {
                beginDate = '';
            }
            var endDate = this.source.date_endDate.getSubmitValue();
            if (endDate == null) {
                endDate = '';
            }

            var exporturl = 'wms/inventoryTurnOver/exportExcel?skuCode=' + skuCode + '&wareHouseId=' + wareHouseId + '&beginDate=' + beginDate + '&endDate=' + endDate;
            window.location.href = exporturl;
        }
    });

    this.grid = Ext.create("Ext.grid.Panel", {
            selModel: this.sm,
            store: this.store,
            scope: this,
            columns: [
                {text: "货主", dataIndex: "ownerCode"},
                {text: "存储地点", dataIndex: "storeArea"},
                {text: "商品代码", dataIndex: "skuCode"},
                {text: "商品名称", dataIndex: "skuName"},
                {text: "发货量", dataIndex: "shippedQty"},
                {text: "发货单位", dataIndex: "shippedEom"},
                {text: "收货量", dataIndex: "receivedQty"},
                {text: "收货单位", dataIndex: "receivedEom"},
                {text: "当前库存", dataIndex: "currQty"},
                {text: "平均库存", dataIndex: "avgQty"},
                {text: "平均发货量", dataIndex: "avgShippedQty"},
                {text: "周转率", dataIndex: "turnRadio"},
                {text: "周转天数", dataIndex: "turnOverDays"},
                {text: "天数", dataIndex: "allDays"}
            ],
            selType: 'cellmodel',
            viewConfig: {
                enableTextSelection: true
            },
            dockedItems: [
                {
                    xtype: "toolbar",
                    dock: "top",
                    scope: this,
                    items: [this.exportBtn]
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
            tooltip: '物流商品周转天数'
        },
        overflowY: 'auto',
        items: [
            this.form, this.grid
        ]
    });

};