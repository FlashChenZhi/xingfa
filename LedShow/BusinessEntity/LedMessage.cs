using System;
using PersistenceLayer;

namespace BusinessEntity
{
    [Serializable]
    public class LedMessage : EntityObject
    {
        public const string __LEDNO = "LedNo";
        public const string __PROCESSED = "Processed";

        private string ledNo;
        private string message1;
        private string message2;
        private string message3;
        private string message4;
        private string processed;

        public string LedNo
        {
            get { return ledNo; }
            set { ledNo = value; }
        }

        public string Message1
        {
            get { return message1; }
            set { message1 = value; }
        }

        public string Message2
        {
            get { return message2; }
            set { message2 = value; }
        }

        public string Message3
        {
            get { return message3; }
            set { message3 = value; }
        }

        public string Message4
        {
            get { return message4; }
            set { message4 = value; }
        }

        public string Processed
        {
            get { return processed; }
            set { processed = value; }
        }
    }
}
