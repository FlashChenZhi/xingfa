﻿//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated by a tool.
//     Runtime Version:2.0.50727.5485
//
//     Changes to this file may cause incorrect behavior and will be lost if
//     the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

// 
// This source code was auto-generated by Microsoft.CompactFramework.Design.Data, Version 2.0.50727.5485.
// 
namespace wms_rft.WmsRft {
    using System.Diagnostics;
    using System.Web.Services;
    using System.ComponentModel;
    using System.Web.Services.Protocols;
    using System;
    using System.Xml.Serialization;
    
    
    /// <remarks/>
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [System.Web.Services.WebServiceBindingAttribute(Name="WmsRftPortBinding", Namespace="http://rft.com/")]
    public partial class WmsRftService : System.Web.Services.Protocols.SoapHttpClientProtocol {
        
        /// <remarks/>
        public WmsRftService() {
            this.Url = "http://webservice-server:8080/wms/services/WmsRft";
        }
        
        /// <remarks/>
        [System.Web.Services.Protocols.SoapDocumentMethodAttribute("", RequestNamespace="http://rft.com/", ResponseNamespace="http://rft.com/", Use=System.Web.Services.Description.SoapBindingUse.Literal, ParameterStyle=System.Web.Services.Protocols.SoapParameterStyle.Wrapped)]
        [return: System.Xml.Serialization.XmlElementAttribute("return", Form=System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public fileInfoRFT getFile([System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified)] string arg0, [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified)] long arg1) {
            object[] results = this.Invoke("getFile", new object[] {
                        arg0,
                        arg1});
            return ((fileInfoRFT)(results[0]));
        }
        
        /// <remarks/>
        public System.IAsyncResult BegingetFile(string arg0, long arg1, System.AsyncCallback callback, object asyncState) {
            return this.BeginInvoke("getFile", new object[] {
                        arg0,
                        arg1}, callback, asyncState);
        }
        
        /// <remarks/>
        public fileInfoRFT EndgetFile(System.IAsyncResult asyncResult) {
            object[] results = this.EndInvoke(asyncResult);
            return ((fileInfoRFT)(results[0]));
        }
        
        /// <remarks/>
        [System.Web.Services.Protocols.SoapDocumentMethodAttribute("", RequestNamespace="http://rft.com/", ResponseNamespace="http://rft.com/", Use=System.Web.Services.Description.SoapBindingUse.Literal, ParameterStyle=System.Web.Services.Protocols.SoapParameterStyle.Wrapped)]
        [return: System.Xml.Serialization.XmlElementAttribute("return", Form=System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public skuVo2[] getSkuList() {
            object[] results = this.Invoke("getSkuList", new object[0]);
            return ((skuVo2[])(results[0]));
        }
        
        /// <remarks/>
        public System.IAsyncResult BegingetSkuList(System.AsyncCallback callback, object asyncState) {
            return this.BeginInvoke("getSkuList", new object[0], callback, asyncState);
        }
        
        /// <remarks/>
        public skuVo2[] EndgetSkuList(System.IAsyncResult asyncResult) {
            object[] results = this.EndInvoke(asyncResult);
            return ((skuVo2[])(results[0]));
        }
        
        /// <remarks/>
        [System.Web.Services.Protocols.SoapDocumentMethodAttribute("", RequestNamespace="http://rft.com/", ResponseNamespace="http://rft.com/", Use=System.Web.Services.Description.SoapBindingUse.Literal, ParameterStyle=System.Web.Services.Protocols.SoapParameterStyle.Wrapped)]
        public void putaway([System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified)] string arg0, [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified)] string arg1, [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified)] string arg2, [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified)] string arg3, [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified)] int arg4) {
            this.Invoke("putaway", new object[] {
                        arg0,
                        arg1,
                        arg2,
                        arg3,
                        arg4});
        }
        
        /// <remarks/>
        public System.IAsyncResult Beginputaway(string arg0, string arg1, string arg2, string arg3, int arg4, System.AsyncCallback callback, object asyncState) {
            return this.BeginInvoke("putaway", new object[] {
                        arg0,
                        arg1,
                        arg2,
                        arg3,
                        arg4}, callback, asyncState);
        }
        
        /// <remarks/>
        public void Endputaway(System.IAsyncResult asyncResult) {
            this.EndInvoke(asyncResult);
        }
        
        /// <remarks/>
        [System.Web.Services.Protocols.SoapDocumentMethodAttribute("", RequestNamespace="http://rft.com/", ResponseNamespace="http://rft.com/", Use=System.Web.Services.Description.SoapBindingUse.Literal, ParameterStyle=System.Web.Services.Protocols.SoapParameterStyle.Wrapped)]
        [return: System.Xml.Serialization.XmlElementAttribute("return", Form=System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public string sayHelloWorldFrom([System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified)] string arg0) {
            object[] results = this.Invoke("sayHelloWorldFrom", new object[] {
                        arg0});
            return ((string)(results[0]));
        }
        
        /// <remarks/>
        public System.IAsyncResult BeginsayHelloWorldFrom(string arg0, System.AsyncCallback callback, object asyncState) {
            return this.BeginInvoke("sayHelloWorldFrom", new object[] {
                        arg0}, callback, asyncState);
        }
        
        /// <remarks/>
        public string EndsayHelloWorldFrom(System.IAsyncResult asyncResult) {
            object[] results = this.EndInvoke(asyncResult);
            return ((string)(results[0]));
        }
    }
    
    /// <remarks/>
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [System.Xml.Serialization.XmlTypeAttribute(Namespace="http://rft.com/")]
    public partial class fileInfoRFT {
        
        private byte[] binDataField;
        
        private long fileLengthField;
        
        /// <remarks/>
        [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified, DataType="base64Binary")]
        public byte[] binData {
            get {
                return this.binDataField;
            }
            set {
                this.binDataField = value;
            }
        }
        
        /// <remarks/>
        [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public long fileLength {
            get {
                return this.fileLengthField;
            }
            set {
                this.fileLengthField = value;
            }
        }
    }
    
    /// <remarks/>
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [System.Xml.Serialization.XmlTypeAttribute(Namespace="http://rft.com/")]
    public partial class skuVo2 {
        
        private string idField;
        
        private string nameField;
        
        /// <remarks/>
        [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public string id {
            get {
                return this.idField;
            }
            set {
                this.idField = value;
            }
        }
        
        /// <remarks/>
        [System.Xml.Serialization.XmlElementAttribute(Form=System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public string name {
            get {
                return this.nameField;
            }
            set {
                this.nameField = value;
            }
        }
    }
}
