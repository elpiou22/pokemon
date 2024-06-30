package com.uca.core;

import com.uca.dao.UserDAO;
import com.uca.entity.UserEntity;

import java.util.ArrayList;

public class UserCore {

    /**
     * Retourne une liste d'user Entity
     * @return la liste de tout les users entity
     */
    public static ArrayList<UserEntity> getAllUsers() {
        return new UserDAO().getAllUsers();
    }

}
