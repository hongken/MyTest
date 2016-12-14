package com.example.test.unittestrealm.model;

import io.realm.RealmObject;

/**
 * Created by harry on 2016-12-14.
 */

public class Cat extends RealmObject {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
