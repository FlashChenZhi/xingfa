package com.util.mongo;

/**
 * @author xiongying
 * @date 2014/11/21
 * @time 21:54
 */
public class MongoFactorySupport {

    public void setMongo(String dbName, Mongo mongo) {
        MongoFactory.getInstance().mongoMap.put(dbName, mongo);
    }
}
