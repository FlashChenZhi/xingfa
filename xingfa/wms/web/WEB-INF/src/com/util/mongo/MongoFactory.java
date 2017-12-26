package com.util.mongo;

import com.mongodb.MongoOptions;
import com.mongodb.ServerAddress;
import com.util.common.FileHelper;
import org.apache.commons.lang3.BooleanUtils;

import java.util.*;

/**
 * @author xiongying
 * @date 2014/11/21
 * @time 21:52
 */
public class MongoFactory {
    Map<String, Mongo> mongoMap = new HashMap<String, Mongo>();
    private com.mongodb.Mongo mongo;

    //数据库名
    private String[] dbNames;

    private static final MongoFactory MONGO_FACTORY = new MongoFactory();

    private MongoFactory() {
        Properties properties = FileHelper.getInstance().loadPropFile("/datasource-mongo");
        try {
            String hostStr = properties.getProperty("mongo.host");
            String[] hosts = hostStr.split(",");
            Integer port = Integer.parseInt(properties.getProperty("mongo.port"));
            List<ServerAddress> serverAddressList = new ArrayList<>();
            for (String host : hosts) {
                ServerAddress server = new ServerAddress(host, port);
                serverAddressList.add(server);
            }

            MongoOptions options = new MongoOptions();
//            options.autoConnectRetry = BooleanUtils.toBoolean(properties.getProperty("mongo.autoConnectRetry"));
            options.connectionsPerHost = Integer.parseInt(properties.getProperty("mongo.connectionsPerHost"));
            options.threadsAllowedToBlockForConnectionMultiplier = Integer.parseInt(properties.getProperty("mongo.threadsAllowedToBlockForConnectionMultiplier"));
            options.fsync = BooleanUtils.toBoolean(properties.getProperty("mongo.fsync"));
            options.connectTimeout = Integer.parseInt(properties.getProperty("mongo.connectTimeout"));
            options.maxWaitTime = Integer.parseInt(properties.getProperty("mongo.maxWaitTime"));
            String userName = properties.getProperty("mongo.userName");
            String password = properties.getProperty("mongo.password");
            this.mongo = new com.mongodb.Mongo(serverAddressList, options);

            this.dbNames = properties.getProperty("mongo.dbNames").split(",");

            for (String dbName : dbNames) {
                Mongo m = new Mongo(this.mongo, dbName,userName,password);
                mongoMap.put(dbName, m);

                System.out.println(String.format("======================mongo db [%s] is init======================", dbName));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static MongoFactory getInstance() {
        return MONGO_FACTORY;
    }

    public Mongo getMongo(String dbName) {
        return mongoMap.get(dbName);
    }
}
