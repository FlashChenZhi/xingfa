package com.util.mongo;

import com.mongodb.*;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author xiongying
 * @date 2015/3/15
 * @time 21:05
 */
public class Mongo {
    private com.mongodb.Mongo mongo;
    private String dbName;
    private String userName;
    private String password;

    public Mongo(com.mongodb.Mongo mongo, String dbName, String userName, String password) {
        this.mongo = mongo;
        this.dbName = dbName;
        this.userName = userName;
        this.password = password;
    }

    private void auth(DB db) {
        if (StringUtils.isNotBlank(this.userName)) {
            boolean flag = db.authenticate(this.userName, this.password.toCharArray());
            if (!flag)
                throw new RuntimeException(String.format("Mongo auth fail, username[%s],password[%s]", this.userName, this.password));
        }
    }

    /**
     * 获取seqence
     *
     * @param coll
     * @return
     */
    public Long sequence(String coll) {
        DB db = this.mongo.getDB(dbName);
        this.auth(db);
        DBCollection collection = db.getCollection("coll_seq");
        DBObject update = new BasicDBObject();
        update.put("$inc", new BasicDBObject("seq", Integer.valueOf(1)));
        try {
            return (Long) collection.findAndModify(new BasicDBObject("coll", coll), update).get("seq");
        } catch (NullPointerException e) {
            DBObject data = new BasicDBObject();
            data.put("coll", coll);
            data.put("seq", Long.valueOf(2L));
            collection.insert(new DBObject[]{data});
        }
        return Long.valueOf(1L);
    }

    /**
     * 数据转码
     *
     * @param data
     * @return
     */
    private BasicDBObject decode(Map<String, Object> data) {
        BasicDBObject odb = new BasicDBObject();
        if (data == null)
            return odb;
        for (String key : data.keySet()) {
            Object value = data.get(key);
            if ((value instanceof BigDecimal)) {
                value = Double.valueOf(((BigDecimal) value).doubleValue());
            } else if (value instanceof Map) {
                BasicDBObject sub = new BasicDBObject();
                Iterator var8 = ((Map) value).keySet().iterator();

                while (var8.hasNext()) {
                    String sub_key = (String) var8.next();
                    Object sub_value = ((Map) value).get(sub_key);
                    if (sub_value instanceof Map) {
                        sub.put(sub_key, this.decode((Map) ((Map) value).get(sub_key)));
                    } else {
                        sub.put(sub_key, sub_value);
                    }
                }
                value = sub;
            }
            odb.put(key, value);
        }
        return odb;
    }

    public boolean insert(String coll, Object obj) {

        JSONObject jsonObject = JSONObject.fromObject(obj);
        Map map = jsonObject;
        return insert(coll, map);
    }

    /**
     * 往列表插入数据
     *
     * @param coll
     * @param data
     */
    public boolean insert(String coll, Map<String, Object> data) {
        DB db = this.mongo.getDB(dbName);
        this.auth(db);
        DBCollection collection = db.getCollection(coll);
        BasicDBObject odb = decode(data);
        odb.put("_id", this.sequence(coll));
        WriteResult writeResult = collection.insert(new DBObject[]{odb});
        if (writeResult != null && writeResult.getN() > 0)
            return true;
        return false;
    }

    /**
     * 更新数据
     *
     * @param coll
     * @param query
     * @param data
     */
    public boolean update(String coll, Map<String, Object> query, Map<String, Object> data) {
        DB db = this.mongo.getDB(dbName);
        this.auth(db);
        DBCollection collection = db.getCollection(coll);
        WriteResult writeResult = collection.update(decode(query), decode(data));
        if (writeResult != null && writeResult.getN() > 0)
            return true;
        return false;
    }


    /**
     * 更新数据
     *
     * @param coll
     * @param id
     * @param data
     */
    public void update(String coll, Map<String, Object> data, Long id) {
        DB db = this.mongo.getDB(dbName);
        this.auth(db);
        DBCollection collection = db.getCollection(coll);
        collection.update(new BasicDBObject("_id", id), this.decode(data));
    }


    /**
     * 删除数据
     *
     * @param coll
     * @param id
     */
    public void delete(String coll, Long id) {
        DB db = this.mongo.getDB(dbName);
        this.auth(db);
        DBCollection collection = db.getCollection(coll);
        collection.remove(new BasicDBObject("_id", id));
    }

    /**
     * 保存
     *
     * @param coll
     * @param query
     * @param data
     */
    public boolean save(String coll, Map<String, Object> query, Map<String, Object> data) {
        DB db = this.mongo.getDB(dbName);
        this.auth(db);
        DBCollection collection = db.getCollection(coll);
        BasicDBObject odb = decode(data);
        WriteResult writeResult = collection.update(decode(query), odb, Boolean.TRUE.booleanValue(), Boolean.TRUE.booleanValue());
        if (writeResult != null && writeResult.getN() > 0)
            return true;
        return false;
    }

    /**
     * 查询数据
     *
     * @param coll
     * @param query
     * @return
     */
    public List<Map> list(String coll, Map<String, Object> query, Map<String, Object> sort) {
        DB db = this.mongo.getDB(dbName);
        this.auth(db);
        DBCollection collection = db.getCollection(coll);
        DBCursor cursor = collection.find(decode(query)).sort(decode(sort));
        List res = new ArrayList();
        while (cursor.hasNext()) {
            res.add((Map) cursor.next());
        }
        return res;
    }

    public List<Map> list(String coll, Map<String, Object> query, Map<String, Object> sort, Pages pages) {
        DB db = this.mongo.getDB(dbName);
        this.auth(db);
        DBCollection collection = db.getCollection(coll);

        DBCursor cursor = collection.find(decode(query)).sort(decode(sort)).skip(pages.firstRow()).limit(pages.getPageSize());

        List res = new ArrayList();
        while (cursor.hasNext()) {
            res.add((Map) cursor.next());
        }
        return res;
    }

    /**
     * 总行数
     *
     * @param coll
     * @param query
     * @return
     */
    public int count(String coll, Map<String, Object> query) {
        DB db = this.mongo.getDB(dbName);
        this.auth(db);
        DBCollection collection = db.getCollection(coll);
        Long count = collection.count(decode(query));
        return count == null ? 0 : count.intValue();
    }


    /**
     * 保存文件
     *
     * @param fileName
     * @param contentType
     * @param in
     * @return
     */
    public long saveFile(String coll, String fileName, String contentType, InputStream in) {
        long id = this.sequence(coll);
        DB db = this.mongo.getDB(dbName);
        this.auth(db);
        GridFSInputFile gridFSInputFile = new GridFS(db).createFile(in);
        gridFSInputFile.setId(id);
        gridFSInputFile.setFilename(fileName);
        gridFSInputFile.setContentType(contentType);
        gridFSInputFile.save();

        return id;
    }


    /**
     * 根据文件名获取文件
     *
     * @param fileName
     * @return
     */
    public GridFSDBFile getFileByName(String fileName) {
        GridFSDBFile gfsFile;
        try {
            DB db = this.mongo.getDB(dbName);
            this.auth(db);
            gfsFile = new GridFS(db).findOne(fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return gfsFile;
    }

    /**
     * 根据ID获取文件
     *
     * @param id
     * @return
     */
    public GridFSDBFile getFileById(Long id) {
        GridFSDBFile gfsFile;
        try {
            Map<String, Object> query = new HashMap<String, Object>();
            query.put("_id", id);
            DB db = this.mongo.getDB(dbName);
            this.auth(db);

            gfsFile = new GridFS(db).findOne(decode(query));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return gfsFile;
    }

    /**
     * 将文件输出到流
     *
     * @param out
     * @param gfsFile
     * @return
     */
    public boolean writeToOutputStream(OutputStream out, GridFSDBFile gfsFile) {
        try {
            gfsFile.writeTo(out);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public Map<String, Object> search(String coll, BasicDBObject odb, int firstRow) {
        DB db = this.mongo.getDB(dbName);
        this.auth(db);
        DBCollection collection = db.getCollection(coll);
        DBCursor cursor = collection.find(odb).skip(firstRow).limit(Pages.DEFAULT_PAGE_SIZE);
        ArrayList res = new ArrayList();

        while (cursor.hasNext()) {
            res.add((Map) cursor.next());
        }
        Long countSour = collection.getCount(odb);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", res);
        map.put("count", countSour.intValue());

        return map;
    }

    public Map<String, Object> search(String coll, BasicDBObject odb, int firstRow, BasicDBObject sortOdb) {
        DB db = this.mongo.getDB(dbName);
        this.auth(db);
        DBCollection collection = db.getCollection(coll);
        DBCursor cursor = collection.find(odb).skip(firstRow).limit(Pages.DEFAULT_PAGE_SIZE).sort(sortOdb);
        ArrayList res = new ArrayList();

        while (cursor.hasNext()) {
            res.add((Map) cursor.next());
        }
        Long countSour = collection.getCount(odb);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", res);
        map.put("count", countSour.intValue());

        return map;
    }

    public BasicDBList group(String coll, String[] keyColumn, DBObject condition,
                             DBObject initial, String reduce, String finalize) {
        DB db = this.mongo.getDB(dbName);
        this.auth(db);
        DBCollection collection = db.getCollection(coll);
        DBObject key = new BasicDBObject();
        for (int i = 0; i < keyColumn.length; i++) {
            key.put(keyColumn[i], true);
        }
        condition = (condition == null) ? new BasicDBObject() : condition;
        if (StringUtils.isEmpty(finalize)) {
            finalize = null;
        }
        if (initial == null) {      //定义一些初始变量
            initial = new BasicDBObject();
            for (int i = 0; i < keyColumn.length; i++) {
                DBObject index = new BasicDBObject();
                index.put("count", 0);
                index.put("sum", 0);
                index.put("max", 0);
                index.put("min", 0);
                index.put("avg", 0);
                index.put("self", "");
                initial.put(keyColumn[i], index);
            }
        }
        BasicDBList resultList = (BasicDBList) collection.group(key, condition,
                initial, reduce, finalize);
        return resultList;
    }

    public List<Map<String, Object>> search(String coll, BasicDBObject odb) {
        DB db = this.mongo.getDB(dbName);
        this.auth(db);
        DBCollection collection = db.getCollection(coll);
        DBCursor cursor = collection.find(odb);
        ArrayList res = new ArrayList();

        while (cursor.hasNext()) {
            res.add((Map) cursor.next());
        }

        return res;
    }

}
