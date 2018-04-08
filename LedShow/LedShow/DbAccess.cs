using PersistenceLayer;
using BusinessEntity;

namespace LedShow
{
    public static class DbAccess
    {
        public static LedMessage getLedMessageByLedNo(string ledNo, bool justStart)
        {
            RetrieveCriteria rc = new RetrieveCriteria(typeof(LedMessage));
            Condition c = rc.GetNewCondition();
            c.AddEqualTo(LedMessage.__LEDNO, ledNo);
            if (!justStart)
            {
                c.AddEqualTo(LedMessage.__PROCESSED, '0');
            }
            return rc.AsEntity() as LedMessage;
        }
    }
}
