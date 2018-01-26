/**
 * Created by Van on 2015/8/11.
 */
this.WmsInventoryAddWin = function (data) {

    this.source = new CommonSource(data);
    this.isSource = new InventorySrouce();
    this.parentStore = null;

    this.form = Ext.create("Ext.form.Panel", {
        defaults: {anchor: "100%"},
        items: [
            this.source.com_wareHouseId,
            this.source.com_skuCode,
            this.source.txt_qty,
            this.source.com_skuEom,
            this.source.date_productDate,
            this.source.txt_locationNo,
            this.source.txt_containerBarcode,
            this.isSource.com_containerType
        ],
        scope: this,
        buttons: [
            {
                text: '取消',
                scope: this,
                handler: function () {
                    this.form.getForm().reset();
                    this.win.hide();
                }
            },
            {
                text: '确定',
                scope: this,
                handler: function () {
                    var wareHouseId = this.source.com_wareHouseId.getValue();
                    if (wareHouseId == null) {
                        Ext.error("仓库必须选择");
                        return;
                    }
                    var skuCode = this.source.com_skuCode.getValue();
                    if (skuCode == null) {
                        Ext.error("商品必须输入");
                        return;
                    }
                    var qty = this.source.txt_qty.getValue();
                    if (qty == null) {
                        Ext.error("数量必须输入");
                        return;
                    }
                    var eom = this.source.com_skuEom.getValue();
                    if (eom == null) {
                        Ext.error("单位必须选择");
                        return;
                    }
                    var locationNo = this.source.txt_locationNo.getValue();
                    if (locationNo == null) {
                        locationNo = "";
                    }
                    var containerBarcode = this.source.txt_containerBarcode.getValue();
                    if (containerBarcode == null) {
                        containerBarcode = "";
                    }
                    var containerType = this.isSource.com_containerType.getValue();
                    if(containerType == null){
                        containerType = "";
                    }
                    var productDate = this.source.date_productDate.getSubmitValue();
                    mask.show();
                    Ext.Ajax.request({
                        url: "wms/wmsInventoryAdjustment/save",
                        method: "POST",
                        params: {
                            'wareHouseId': wareHouseId,
                            'skuCode': skuCode,
                            'qty': qty,
                            'eom': eom,
                            'locationNo': locationNo,
                            'containerBarcode':containerBarcode,
                            'productDate': productDate,
                            'containerType':containerType
                        },
                        scope: this,
                        success: function (response, options) {
                            mask.hide();
                            var result = Ext.util.JSON.decode(response.responseText);
                            if (result.success) {
                                Ext.success(result.msg);
                                this.win.hide();
                                this.store.reload();
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
        ]
    });

    this.source.com_skuCode.on('select', function () {
        this.source.com_skuEom.clearValue();
        this.source.init(this.source.com_skuCode.getValue());
    }, this);

    this.init = function (data, parentStore) {
        this.parentStore = parentStore;

        this.cmp.txt_id.setValue(data.id);
        this.cmp.txt_printName.setValue(data.name);
        this.cmp.com_printerType.setValue(data.type);
        this.cmp.com_printerStatus.setValue(data.status);
        this.source.com_carrier.setValue(data.carrierCode);
    };

    this.win = Ext.create("Ext.window.Window", {
        bodyPadding: 5,
        width: 500,
        resizable: false,
        modal: true,
        closeAction: "hide",
        scope: this,
        items: this.form
    });

    var mask = new Ext.LoadMask({
        msg: '系统处理中...',
        target: this.win
    });

};