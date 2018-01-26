var EbOrderChangeWeightPanel = function (data) {
    this.source = new CommonSource(data);
    this.cmp = new EbOrderSearchSource();

    var audioElementHovertree = document.createElement('audio');
    audioElementHovertree.setAttribute('src', 'sounds/error.wav');


    this.source.txt_expressNo.allowBlank = false;
    this.form = Ext.create("Ext.form.Panel", {
        title: '订单重新称重',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.cmp.txt_palletNo,
                    this.source.txt_weight
                ]
            },
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.txt_expressNo
                ]
            }
        ]
    });

    this.source.txt_weight.on('specialKey',function (field, e) {
        if (e.getKey() == e.ENTER) {
            var palletNo = this.cmp.txt_palletNo.getValue();
            var weight = this.source.txt_weight.getValue();
            var expressNo = this.source.txt_expressNo.getValue();
            Ext.Ajax.request({
                url: 'wms/orderWeightAction/changeWeight',
                method: 'POST',
                timeout: 120000,
                params: {
                    'palletNo': palletNo,
                    'expressNo': expressNo,
                    'weight': weight
                },
                scope: this,
                success: function (response, options) {
                    mask.hide();
                    var result = Ext.util.JSON.decode(response.responseText);
                    if (result.success) {
                        Ext.success(result.msg);
                        field.setValue("");
                        field.focus();
                    } else {
                        Ext.error(result.msg);
                        field.setValue("");
                        field.focus();
                        audioElementHovertree.play();

                    }
                },
                failure: function (response, options) {
                    mask.hide();
                    audioElementHovertree.play();

                    extAjaxFail(response, options);
                }
            });
        }
    },this);

    this.source.txt_expressNo.on('specialKey', function (field, e) {
        if (e.getKey() == e.ENTER) {
            var palletNo = this.cmp.txt_palletNo.getValue();
            var result = {success: false, weight: 0};
            Ext.data.JsonP.request({
                url: 'http://localhost:8080/serial',
                success: function (response, options) {
                    if (response.success) {
                        result.weight = response.res.weight;

                        Ext.Ajax.request({
                            url: 'wms/orderWeightAction/changeWeight',
                            method: 'POST',
                            timeout: 120000,
                            params: {
                                'palletNo': palletNo,
                                'expressNo': field.getValue(),
                                'weight': result.weight
                            },
                            scope: this,
                            success: function (response, options) {
                                mask.hide();
                                var result = Ext.util.JSON.decode(response.responseText);
                                if (result.success) {
                                    Ext.success(result.msg);
                                    field.setValue("");
                                    field.focus();
                                } else {
                                    Ext.error(result.msg);
                                    field.setValue("");
                                    field.focus();
                                    audioElementHovertree.play();

                                }
                            },
                            failure: function (response, options) {
                                mask.hide();
                                audioElementHovertree.play();

                                extAjaxFail(response, options);
                            }
                        });
                    }
                },
                failure: function (response, options) {
                    audioElementHovertree.play();

                    Ext.error('称重失败,请检查称重程序是否启动');
                }
            });

        }
    }, this);


    this.panel = Ext.create("Ext.panel.Panel", {
        tabConfig: {
            tooltip: '订单重新称重'
        },
        overflowY: 'auto',
        items: [
            this.form
        ]
    });

    var mask = new Ext.LoadMask({
        msg: '系统处理中...',
        target: this.panel
    });
};
