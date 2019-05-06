package com.youngpower.a996icu.model;


public class DataManager {

    private static final DataManager sInstance = new DataManager();

    private DataManager() {

    }

    public static DataManager getInstance() {
        return sInstance;
    }


}
