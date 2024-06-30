package com.uca.entity;

import java.sql.Timestamp;

public class InventoryEntity {
    private int idUser;
    private int idInstancePokemon;  

    public InventoryEntity() {
        
    }

    /**
     * Récupère l'user id de cette entité
     * @return l'user id
     */
    public int getUserId() {
        return idUser;
    }

    /**
     * Spécifie l'user id de cette entité
     * @param id user id
     */
    public void setUserId(int id) {
        this.idUser = id;
    }

    /**
     * Récupère le pokemon instance id de cette entité
     * @return pokemon instance id
     */
    public int getidInstancePokemon() {
        return idInstancePokemon;
    }

    /**
     * Spécifie un pokemon instance id de cette entité
     * @param id pokemon instance id
     */
    public void setidInstancePokemon(int id) {
        this.idInstancePokemon = id;
    }

}
