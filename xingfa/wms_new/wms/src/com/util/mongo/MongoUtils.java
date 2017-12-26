package com.util.mongo;

/**
 * @author xiongying
 * @date 2015/3/15
 * @time 21:27
 */
public class MongoUtils {

    public static Mongo getMongo(String dbName) {
        return MongoFactory.getInstance().getMongo(dbName);
    }
}
