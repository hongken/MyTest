package com.example.test.unittestrealm.repository;

import com.example.test.unittestrealm.model.Dog;

import io.realm.Realm;

/**
 * Created by harry on 2016-12-14.
 */

public class DogRepositoryImpl implements DogRepository {
    @Override
    public void createDog(final String name) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Dog dog = realm.createObject(Dog.class);
                dog.setName(name);
            }
        });
        realm.close();
    }
}
