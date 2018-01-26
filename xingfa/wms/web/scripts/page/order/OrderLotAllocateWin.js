var OrderLotAllocateWin = function (obj, obj2) {
        var sels = obj;
        var keyword = null;
        var orderNo = obj2;

        this.cmp = new OrderSearchSource();
        this.pareStore = null;


        this.orderDetailStore = Ext.create("Ext.data.JsonStore", {
            fields: ['id', 'locationNo', 'skuCode', 'skuName', 'lotNo', 'planQty', 'qty', 'eom', 'orderDtId']
        });

        this.init = function () {

            var results = [];
            for (var res in sels) {
                var data = sels[res].data;
                results.push(data);
            }

            Ext.Ajax.request({
                url: 'wms/orderLotAllocate/allocateResult?orderNo=' + orderNo,
                headers: {'Content-Type': "application/json; charset=utf-8"},
                method: 'POST',
                params: Ext.util.JSON.encode(results),
                scope: this,
                success: function (response, options) {
                    var result = Ext.util.JSON.decode(response.responseText);
                    if (result.success) {
                        for (var r in result.res) {
                            var data = result.res[r];
                            this.orderDetailStore.add({
                                "id": data.id,
                                "locationNo": data.locationNo,
                                "skuCode": data.skuCode,
                                "lotNo": data.lotNo,
                                "skuName": data.skuName,
                                "planQty": data.planQty,
                                "qty": data.qty,
                                "eom": data.eom,
                                "orderDtId": data.orderDtId
                            });
                        }
                    } else {
                        Ext.error(result.msg);
                    }
                },
                failure: function (response, options) {
                    console.log(response.responseText);
                    extAjaxFail(response, options);
                }
            });
        };


        this.grid = Ext.create("Ext.grid.Panel", {
            store: this.orderDetailStore,
            scope: this,
            width: 1024,
            maxHeight: 500,
            tbar: [this.cmp.txt_keyword, {
                text: '搜索',
                scope: this,
                handler: function () {
                    keyword = this.cmp.txt_keyword.getValue();
                    this.orderDetailStore.load();
                }
            }
            ],
            columns: [
                {text: "ID", dataIndex: "id", width: "10%"},
                {text: "订单明细", dataIndex: "orderDtId", width: "5%", hidden: true},
                {text: "货位号", dataIndex: "locationNo", width: "15%"},
                {text: "商品代码", dataIndex: "skuCode", width: "15%"},
                {text: "商品名称", dataIndex: "skuName", width: "15%"},
                {text: "批次", dataIndex: "lotNo", width: "15%"},
                {text: "预订数量", dataIndex: "planQty", width: "10%"},
                {text: "分配数量", dataIndex: "qty", width: "10%"},
                {text: "单位", dataIndex: "eom", width: "10%"}
            ],

            viewConfig: {
                enableTextSelection: true,
                getRowClass: function (record, rowIndex, rowParams, store) {
                    if (record.data.zt == 0) {
                        return 'x-grid-record-red';
                    } else {
                        return '';
                    }
                }
            }
        });

        this.cmp.txt_keyword.on('specialKey', function (field, e) {
            if (e.getKey() == e.ENTER) {
                keyword = this.cmp.txt_keyword.getValue();
                this.orderDetailStore.load();
            }
        }, this);


        this.win = Ext.create("Ext.window.Window", {
            bodyPadding: 5,
            resizable: false,
            modal: true,
            closeAction: "hide",
            scope: this,
            items: this.grid,
            y: 40,
            buttons: [
                {
                    text: '分配',
                    scope: this,
                    handler: function () {

                        Ext.Msg.show({
                            title: '确认',
                            message: '确认分配？',
                            buttons: Ext.Msg.YESNO,
                            icon: Ext.Msg.QUESTION,
                            scope: this,
                            fn: function (btn) {
                                if (btn === 'yes') {

                                    var results = [];
                                    for (var i = 0; i < this.orderDetailStore.getCount(); i++) {
                                        var data = this.orderDetailStore.getAt(i).data;
                                        results.push(data);
                                    }
                                    Ext.Ajax.request({
                                        url: 'wms/orderLotAllocate/allocate?orderNo=' + orderNo,
                                        headers: {'Content-Type': "application/json; charset=utf-8"},
                                        method: 'POST',
                                        params: Ext.util.JSON.encode(results),
                                        scope: this,
                                        success: function (response, options) {
                                            var result = Ext.util.JSON.decode(response.responseText);
                                            if (result.success) {
                                                Ext.success(result.msg);
                                                this.orderDetailStore.removeAll();
                                                this.pareStore.removeAll();
                                                this.win.hide();

                                            } else {
                                                Ext.error(result.msg);
                                            }
                                        },
                                        failure: function (response, options) {
                                            console.log(response.responseText);
                                            extAjaxFail(response, options);
                                        }
                                    });
                                }
                                else {
                                    //todo cancel
                                }
                            }
                        });

                    }
                },
                {
                    text: '关闭',
                    scope: this,
                    handler: function () {
                        this.orderDetailStore.removeAll();
                        this.win.hide();
                    }
                }
            ]
        });
    }
    ;