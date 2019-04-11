package com.rifqit.animeList2.Database;

import com.rifqit.animeList2.favorite.FavObj;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmHelper {

    Realm realm;

    public RealmHelper(Realm realm){

        this.realm = realm;
    }
    public void save(final FavObj favObj){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                FavObj model = realm.copyToRealm(favObj);
//                if (realm != null){
//                    Log.e("Created", "Database was created");
//                    Number currentIdNum = realm.where(FavObj.class).max("id");
//                    int nextId;
//                    if (currentIdNum == null){
//                        nextId = 1;
//                    }else {
//                        nextId = currentIdNum.intValue() + 1;
//                    }
//                    FavObj model = realm.copyToRealm(favObj);
//                }else{
//                    Log.e("ppppp", "execute: Database not Exist");
//                }
            }
        });
    }
    public List<FavObj> getFav(){
        RealmResults<FavObj> results = realm.where(FavObj.class).findAll();
        return results;
    }
    public void delete(Integer malid){
        final RealmResults<FavObj> model = realm.where(FavObj.class).equalTo("malId", malid).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                model.deleteFromRealm(0);
            }
        });
    }
    public void deleteAll(){
        final RealmResults<FavObj> model = realm.where(FavObj.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                model.deleteAllFromRealm();

            }
        });
    }

}
