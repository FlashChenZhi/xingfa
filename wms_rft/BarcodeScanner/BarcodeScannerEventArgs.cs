//------------------------------------------------------------------------------
/// <copyright from='1997' to='2005' company='Microsoft Corporation'>
///		Copyright (c) Microsoft Corporation. All Rights Reserved.
///
///   This source code is intended only as a supplement to Microsoft
///   Development Tools and/or on-line documentation.  See these other
///   materials for detailed information regarding Microsoft code samples.
/// </copyright>
//------------------------------------------------------------------------------
using System;

namespace Barcode
{
    /// <summary>
    /// This is the event arguments for the Barcode Scanner class event BarcodeScan.
    /// </summary>
    public class BarcodeScannerEventArgs : EventArgs
    {
        private readonly string data;
        private readonly string type;

        public BarcodeScannerEventArgs(string data, string type)
        {
            this.data = data;
            this.type = type;
        }        

        public string Data
        {
            get { return data; }
        }

        public string Type
        {
            get { return type; }
        }
    }
}