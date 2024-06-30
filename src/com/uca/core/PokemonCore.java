package com.uca.core;

import com.uca.dao.PokemonDAO;
import com.uca.entity.PokemonEntity;

 
public class PokemonCore {

    /**
     * Retourne un pokemon Entity grâce a son instance id
     * @param id isntance id
     * @return Pokemon Entity
     */
    public static PokemonEntity getInfos(int id) {
        return new PokemonDAO().getPokemon(id);
    }


}
