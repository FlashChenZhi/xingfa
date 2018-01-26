/**
 * Created by Van on 2015/8/12.
 * 创建电商活动
 */

var CreateOrderPromotionPlan = function (data) {

    this.source = new CommonSource(data);

    this.source.com_skuCode.allowBlank = false;
    this.source.txt_qty.allowBlank = false;

    this.form = Ext.create("Ext.form.Panel", {
        title: '创建电商活动',
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
                    this.source.txt_qty
                ]
            }
        ],
        buttons: [
            {
                text: '重置',
                scope: this,
                handler: function () {
                    Ext.Msg.show({
                        title: '确认',
                        message: '确认重置？',
                        buttons: Ext.Msg.YESNO,
                        icon: Ext.Msg.QUESTION,
                        scope: this,
                        fn: function (btn) {
                            if (btn === 'yes') {
                                this.form.getForm().reset();
                                this.store.removeAll();
                            }
                        }
                    })
                }
            },
            {
                text: '添加',
                scope: this,
                handler: function () {

                    var form = this.form.getForm();
                    if (form.isValid()) {

                        var sels = this.store.data.items;
                        for (var res in sels) {
                            var skuCode = sels[res].data['skuCode'];
                            if (skuCode == this.source.com_skuCode.getValue()) {
                                Ext.error("列表中已经存在该商品");
                                return;
                            }
                        }

                        this.store.add({
                            "skuCode": this.source.com_skuCode.getValue(),
                            "skuName": this.source.com_skuCode.getDisplayValue(),
                            "qty": this.source.txt_qty.getValue()
                        });
                    }
                }
            }
        ]
    });

    var createBtn = Ext.create("Ext.button.Button", {
        text: "创建",
        scope: this,
        handler: function () {
            Ext.Msg.show({
                title: '确认',
                message: '确认创建？',
                buttons: Ext.Msg.YESNO,
                icon: Ext.Msg.QUESTION,
                scope: this,
                fn: function (btn) {
                    if (btn === 'yes') {

                        if (this.store.getCount() == 0) {
                            Ext.error("列表必须要存在");
                            return;
                        }

                        var sels = this.store.data.items;
                        var results = [];
                        for (var res in sels) {
                            results.push(sels[res].data);
                        }
                        var logicCode = this.source.com_logicZoneCode.getValue();
                        var marchType = this.source.com_promotionMatchType.getValue();
                        var name = this.source.txt_promotionName.getValue();
                        if(logicCode == null){
                            Ext.error("作业区不能为空");
                            return;
                        }
                        if(marchType == null){
                            Ext.error("匹配模式不能为空");
                            return;
                        }
                        if(name == null){
                            Ext.error("名称不能为空");
                            return;
                        }
                        mask.show();
                        Ext.Ajax.request({
                            url: 'wms/promotion/create?logicZoneCode='+logicCode+'&matchFlag='+marchType+'&name='+name,
                            headers: {'Content-Type': "application/json; charset=utf-8"},
                            method: 'POST',
                            scope: this,
                            params: Ext.util.JSON.encode(results),
                            success: function (response, options) {
                                mask.hide();
                                var result = Ext.util.JSON.decode(response.responseText);
                                if (result.success) {
                                    Ext.success(result.msg);
                                    this.form.getForm().reset();
                                    this.store.removeAll();
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
            });
        }
    });


    this.store = Ext.create("Ext.data.Store", {
        fields: [ 'skuCode', 'skuName', 'qty'],
        scope: this
    });


    this.getDockedItems = function () {
        var dockedItems = [];

        var toolbar = {
            xtype: "toolbar",
            dock: "top",
            scope: this,
            items: [createBtn,this.source.txt_promotionName,this.source.com_logicZoneCode,this.source.com_promotionMatchType]
        };

        dockedItems.push(toolbar);

        return dockedItems;
    };

    this.grid = Ext.create("Ext.grid.Panel", {
        store: this.store,
        scope: this,
        viewConfig: {
            enableTextSelection: true
        },
        columns: [
            {text: "商品代码", dataIndex: "skuCode"},
            {text: "商品名称", dataIndex: "skuName"},
            {text: "数量", dataIndex: "qty"},
        ],
        listeners: {
            scope: this,
            rowcontextmenu: function (thiz, record, tr, rowIndex, e, eOpts) {
                e.preventDefault();
                if (rowIndex < 0) {
                    return;
                }
                var menu = Ext.create("Ext.menu.Menu", {
                    items: [
                        {
                            text: '删除',
                            scope: this,
                            handler: function () {
                                this.store.remove(record);
                            }
                        }
                    ]
                });
                menu.showAt(e.getPoint());
            }
        },
        dockedItems: this.getDockedItems()
    });

    this.panel = Ext.create("Ext.panel.Panel", {
        tabConfig: {
            tooltip: '创建电商活动'
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