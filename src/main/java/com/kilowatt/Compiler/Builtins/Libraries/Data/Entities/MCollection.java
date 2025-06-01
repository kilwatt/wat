package com.kilowatt.Compiler.Builtins.Libraries.Data.Entities;

import com.kilowatt.Compiler.Builtins.Libraries.Collections.WattList;
import com.kilowatt.Compiler.Builtins.Libraries.Collections.WattMap;
import com.mongodb.client.MongoCollection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;


/*
Коллекция
 */
@AllArgsConstructor
@Getter
public class MCollection {
    // внутренняя коллекция
    private final MongoCollection<Document> collection;

    // функции
    public void insert(MDocument document) {
        collection.insertOne(document.getDocument());
    }

    public void insert_many(WattList list) {
        for (Object o : list.getList()) {
            insert((MDocument) o);
        }
    }

    public void delete(MDocument document) {
        collection.deleteOne(document.getDocument());
    }

    public void delete_many(WattList list) {
        for (Object o : list.getList()) {
            delete((MDocument) o);
        }
    }

    public long size() {
        return collection.countDocuments();
    }

    public void update(MDocument what, MDocument update) {
        collection.updateOne(what.getDocument(), update.getDocument());
    }

    public WattMap find_first(MDocument what) {
        return WattMap.of(collection.find(what.getDocument()).first());
    }

    public WattList find_all(MDocument what) {
        List<Object> results = new ArrayList<>();

        for (Document doc : collection.find(what.getDocument())) {
            results.add(doc);
        }

        return WattList.of(results);
    }

    public WattList all() {
        List<Object> results = new ArrayList<>();

        for (Document doc : collection.find()) {
            results.add(doc);
        }

        return WattList.of(results);
    }

    public long count(MDocument what) {
        return collection.countDocuments(what.getDocument());
    }

    @SuppressWarnings("resource")
    public boolean exists(String collectionName, MDocument filter) {
        return collection.find(filter.getDocument())
                .iterator()
                .hasNext();
    }

    public void drop() {
        collection.drop();
    }
}
