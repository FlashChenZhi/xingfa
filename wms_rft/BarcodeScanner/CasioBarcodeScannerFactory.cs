//------------------------------------------------------------------------------
/// <copyright from='1997' to='2005' company='Microsoft Corporation'>
///		Copyright (c) Microsoft Corporation. All Rights Reserved.
///
///   This source code is intended only as a supplement to Microsoft
///   Development Tools and/or on-line documentation.  See these other
///   materials for detailed information regarding Microsoft code samples.
/// </copyright>
//------------------------------------------------------------------------------
namespace Barcode
{
    public class CasioBarcodeScannerFactory : BarcodeScannerFactory 
    {
        public override BarcodeScanner GetBarcodeScanner() 
        {
            return new CasioBarcodeScanner();
        }
    }
}