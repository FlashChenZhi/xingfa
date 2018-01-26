var InventoryHistoryAbcPanel = function (data) {

    this.source = new CommonSource(data);

    var valueStore = Ext.create("Ext.data.JsonStore", {
        fields: ["value", "displayValue"],
        autoLoad: true,
        scope: this,
        proxy: {
            type: 'memory',
            reader: {
                type: 'json',
                rootProperty: 'res'
            }
        }
    });

    this.com_date = Ext.create('Ext.form.ComboBox', {
        fieldLabel: '月份',
        name: 'month',
        emptyText: '请选择',
        store: valueStore,
        queryMode: 'local',
        displayField: 'displayValue',
        selectOnFocus: true,
        valueField: 'value'
    });


    this.valueStroeChange = function () {
        valueStore.removeAll();
        var myDate = new Date();
        var year = myDate.getFullYear(); ///获取当期那年份
        var month = myDate.getMonth() + 1; //获取当前月份
        for (var i = 2015; i < year; i++) {
            var bg;
            if(i == 2015)
                bg = 10;
            else
                bg = 1;
            for (var j = bg; j <= 12; j++) {
                valueStore.add({'value': i + "-" + j, 'displayValue': i + "-" + j})
            }
        }
        for (var i = 1; i < month; i++) {
            valueStore.add({'value': year + "-" + i, 'displayValue': year + "-" + i})
        }

    };

    valueStore.on('load', function () {
        this.valueStroeChange()
    }, this);


    var exportExcelBtn = Ext.create("Ext.button.Button", {
            text: "导出",
            scope: this,
            formBind: true,
            handler: function () {

                var month = this.com_date.getValue();
                if (month == null) {
                    Ext.warn("月份必须选择");
                    return;
                }
                var skuCode = this.source.com_skuCode.getValue();
                if (skuCode == null) {
                    skuCode = '';
                }
                var type = this.source.com_skuAbcType.getValue();
                if (type == null) {
                    type = '';
                }
                var exporturl = 'wms/inventoryAbc/exportExcel?month=' + month + "&skuCode=" + skuCode + "&skuType=" + type;
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


    this.com_date.allowBlank = false;
    this.form = Ext.create("Ext.form.Panel", {
        title: '库存ABC历史分析',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.com_date,
                    this.source.com_skuCode

                ]
            },
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.com_skuAbcType
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
                    this.store.loadPage(1);
                }
            }
        ]
    });

    this.store = Ext.create("Ext.data.JsonStore", {
        scope: this,
        fields: ['skuCode', 'skuName', 'wareHouseCode', 'shipQty', 'eom', 'radio', 'avgShipQty', 'type', 'expireDate'],
        pageSize: 20,
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/inventoryAbc/searchHistory",
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

    this.grid = Ext.create("Ext.grid.Panel", {
            store: this.store,
            scope: this,
            viewConfig: {
                enableTextSelection: true
            },
            columns: [
                {text: "仓库代码", dataIndex: "wareHouseCode", width: "10%"},
                {text: "商品代码", dataIndex: "skuCode", width: "15%"},
                {text: "商品名称", dataIndex: "skuName", width: "15%"},
                {text: "出库数量", dataIndex: "shipQty", width: "10%"},
                {text: "单位", dataIndex: "eom", width: "10%"},
                {text: "出库比率", dataIndex: "radio", width: "10%"},
                {text: "平均出库量", dataIndex: "avgShipQty", width: "10%"},
                {text: "类型", dataIndex: "type", width: "7%"},
                {text: "过期日", dataIndex: "expireDate", flex: 1}
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
            tooltip: '库存ABC历史分析'
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