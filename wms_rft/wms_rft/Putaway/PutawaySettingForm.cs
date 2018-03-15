using System;

using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using wms_rft.Helper;
using wms_rft.WmsRft;
using Barcode;

namespace wms_rft.Putaway
{
    public partial class PutawaySettingForm : Form
    {
        private MessageHelper msgHelper;


        public PutawaySettingForm()
        {
            InitializeComponent();
        }

        private void PutawaySettingForm_Load(object sender, EventArgs e)
        {
            msgHelper = new MessageHelper(lblMessage);
            try
            {
                msgHelper.clear();

                skuVo2[] skus = ServiceFactory.getCurrentService().getSkuList();
                pul_SkuCode.ValueMember = "id";
                pul_SkuCode.DisplayMember = "name";
                pul_SkuCode.DataSource = skus;

                skuVo2[] stations = new skuVo2[2];
                skuVo2 station1 = new skuVo2();
                station1.id = "1101";
                station1.name = "1101";
                skuVo2 station2 = new skuVo2();
                station2.id = "1301";
                station2.name = "1301";
                stations[0] = station1;
                stations[1] = station2;
                pul_StationNo.ValueMember = "id";
                pul_StationNo.DisplayMember = "name";
                pul_StationNo.DataSource = stations;

            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }
        }


        private void btn_Clear_Click(object sender, EventArgs e)
        {
            txt_PalletNo.Text = string.Empty;
            msgHelper.clear();
        }

        private void btn_Setting_Click(object sender, EventArgs e)
        {
            try
            {
                msgHelper.clear();

                string palletNo = txt_PalletNo.Text;
                if (string.IsNullOrEmpty(palletNo))
                {
                    msgHelper.showWarning("invalid pallet no");

                    txt_PalletNo.SelectAll();
                    txt_PalletNo.Focus();
                    return;
                }
                string skuCode = pul_SkuCode.SelectedValue.ToString();

                if (string.IsNullOrEmpty(skuCode))
                {
                    msgHelper.showWarning("invalid sku code");

                    pul_SkuCode.Focus();
                    return;
                }

                string stationNo = pul_StationNo.SelectedValue.ToString();

                if (string.IsNullOrEmpty(stationNo))
                {
                    msgHelper.showWarning("invalid station no");

                    pul_StationNo.Focus();
                    return;
                }

                int qty = Convert.ToInt32(txt_Qty.Value);

                if (qty <= 0)
                {
                    msgHelper.showWarning("invalid qty");

                    txt_Qty.Focus();
                    return;
                }

                ServiceFactory.getCurrentService().putaway(palletNo, stationNo, skuCode,txt_LotNo.Text, qty);


                msgHelper.showInfo("success");
                txt_PalletNo.Text = string.Empty;
                txt_PalletNo.Focus();

            }
            catch (Exception ex)
            {
                msgHelper.showError(ex.Message);
            }
        }

        private void btn_Close_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void txt_PalletNo_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar == Convert.ToChar(Keys.Enter))
            {
                btn_Setting_Click(null,null);
            }
        }

    }
}