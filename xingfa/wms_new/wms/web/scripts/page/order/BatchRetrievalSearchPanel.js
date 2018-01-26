var BatchRetrievalSearchPanel = function (data) {

    this.source = new CommonSource(data);

    this.source.txt_batchNo.allowBlank = false;
    this.form = Ext.create("Ext.form.Panel", {
        title: '波次出库查询',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.txt_batchNo
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
                        this.store.loadPage(1);
                    }
                }
            }
        ]
    });

    this.store = Ext.create("Ext.data.JsonStore", {
        scope: this,
        fields: ['logicZoneCode', 'logicZoneName', 'allocateQty', 'pickedQty', 'eom'],
        listeners: {
            scope: this,
            beforeload: function (store) {
                store.proxy.extraParams = this.form.getValues();
            }
        },
        proxy: {
            type: "ajax",
            url: "wms/batchRetrievalPicking/search",
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
            features: [
                {
                    ftype: 'summary'
                }
            ],
            columns: [
                {text: "作业区代码", dataIndex: "logicZoneCode", width: "30%"},
                {text: "作业区名称", dataIndex: "logicZoneName", width: "30%"},
                {
                    text: "未拣数量", dataIndex: "allocateQty", width: "20%", scope: this, summaryType: 'sum',
                    summaryRenderer: function (value, summaryData, dataIndex) {
                        return Ext.String.format('{0}{1}', '总计：', value);
                    }
                },
                {text: "单位", dataIndex: "eom", width: "10%"},
                {
                    text: "已完成数量", dataIndex: "pickedQty", width: "10%", flex: 1, scope: this, summaryType: 'sum',
                    summaryRenderer: function (value, summaryData, dataIndex) {
                        return Ext.String.format('{0}{1}', '总计：', value);
                    }
                },
            ],
            viewConfig: {
                enableTextSelection: true
            },
            selType: 'cellmodel'
        }
    );

    this.panel = Ext.create("Ext.panel.Panel", {
        tabConfig: {
            tooltip: '波次出库查询'
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