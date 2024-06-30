package com.uca.dao;

public class _Initializer {

    public static void Init(){
        // create tables
        new InventoryDAO().createTable();
        new UserDAO().createTable();
        new TradeDAO().createTable();

    }
}
