var EbOrderSetExpressNoPanel = function (data) {
    this.source = new CommonSource(data);

    var audioElementHovertree = document.createElement('audio');
    audioElementHovertree.setAttribute('src', 'sounds/error.wav');


    this.source.txt_expressNo.allowBlank = false;
    this.form = Ext.create("Ext.form.Panel", {
        title: '订单设置快递单号',
        collapsible: true,
        scope: this,
        layout: 'column',
        items: [
            {
                layout: "form", columnWidth: .5, defaults: {layout: "form"},
                items: [
                    this.source.txt_retrievalOrderNo,
                    this.source.com_dsCarrierCode
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

    this.source.txt_expressNo.on('specialKey', function (field, e) {
        if (e.getKey() == e.ENTER) {
            var orderNo = this.source.txt_retrievalOrderNo.getValue();
            var carrierCode = this.source.com_dsCarrierCode.getSubmitValue();
            Ext.Ajax.request({
                url: 'wms/orderWeightAction/changeExpressNo',
                method: 'POST',
                timeout: 120000,
                params: {
                    'orderNo': orderNo,
                    'expressNo': field.getValue(),
                    'carrierCode':carrierCode
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
    }, this);


    this.panel = Ext.create("Ext.panel.Panel", {
        tabConfig: {
            tooltip: '订单设置快递单号'
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
