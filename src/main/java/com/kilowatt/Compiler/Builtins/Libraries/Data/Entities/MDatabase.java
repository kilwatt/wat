package com.kilowatt.Compiler.Builtins.Libraries.Data.Entities;

import com.mongodb.client.MongoDatabase;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
База данных монго
 */
@AllArgsConstructor
@Getter
public class MDatabase {
    // база данных
    private final MongoDatabase database;

    // функции
    public MCollection get_collection(String name) {
        return new MCollection(database.getCollection(name));
    }

    public void drop_collection(String name) {
        database.getCollection(name).drop();
    }

    // дроп
    public void drop() {
        database.drop();
    }
}
