package com.kilowatt.Compiler.Builtins.Libraries.Data;

import com.kilowatt.Compiler.Builtins.Libraries.Collections.WattMap;
import com.kilowatt.Compiler.Builtins.Libraries.Data.Entities.MDatabase;
import com.kilowatt.Compiler.Builtins.Libraries.Data.Entities.MDocument;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.Map;
import java.util.stream.Collectors;

/*
Data -> Монго БД
 */
public class DataMongo {
    // клиент
    private final MongoClient client;

    // конструктор
    public DataMongo(String url) {
        this.client = MongoClients.create(url);
    }

    // функции
    public MDatabase get_database(String name) {
        return new MDatabase(client.getDatabase(name));
    }

    public void delete_database(String name) {
        client.getDatabase(name).drop();
    }

    public MDocument document(WattMap map) {
        Map<String, Object> converted = map.getMap().entrySet().stream().collect(
            Collectors.toMap(e -> e.getKey().toString(),Map.Entry::getValue)
        );
        return new MDocument(new Document(converted));
    }

    public MDocument filter_eq(String key, Object to) {
        return new MDocument(Document.parse(Filters.eq(key, to).toBsonDocument().toJson()));
    }
    public MDocument filter_lte(String key, Object to) {
        return new MDocument(Document.parse(Filters.lte(key, to).toBsonDocument().toJson()));
    }
    public MDocument filter_gte(String key, Object to) {
        return new MDocument(Document.parse(Filters.gte(key, to).toBsonDocument().toJson()));
    }
    public MDocument filter_lt(String key, Object to) {
        return new MDocument(Document.parse(Filters.lt(key, to).toBsonDocument().toJson()));
    }
    public MDocument filter_gt(String key, Object to) {
        return new MDocument(Document.parse(Filters.gt(key, to).toBsonDocument().toJson()));
    }
    public MDocument filter_neq(String key, Object to) {
        return new MDocument(Document.parse(Filters.ne(key, to).toBsonDocument().toJson()));
    }
}
