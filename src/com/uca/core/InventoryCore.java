package com.uca.core;

import com.uca.dao.InventoryDAO;
import com.uca.entity.InventoryEntity;

import java.util.ArrayList;

public class InventoryCore {

    /**
     * Retourne l'inventaire d'un user
     * @param id user id
     * @return inventaire
     */
    public static ArrayList<InventoryEntity> getInventory(int id) {
        return new InventoryDAO().getInventory(id);
    }



}
