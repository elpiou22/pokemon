package com.uca.gui;

import com.uca.core.UserCore;
import com.uca.core.InventoryCore;
import com.uca.core.PokemonCore;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import com.uca.dao.UserDAO;
import com.uca.dao.PokemonDAO;

import com.uca.*;
import com.uca.entity.InventoryEntity;
import com.uca.gui.PokemonGUI;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.Collections;
import java.util.Comparator;



import spark.Request;
import javax.servlet.http.HttpServletRequest;



import static spark.Spark.*;


import com.uca.dao.TradeDAO;


public class InventoryGUI {

    /**
     * Affichage de la page FTL inventaire.ftl
     * @param id user id de l'inventaire à afficher
     * @return la liste des arguments dont le ftl peut avoir accès
     * @throws IOException
     * @throws TemplateException
     */
    public static String getInventory(int id, Request request) throws IOException, TemplateException {
        Configuration configuration = _FreeMarkerInitializer.getContext();
        Map<String, Object> input = new HashMap<>();

        ArrayList<InventoryEntity> pokemons = InventoryCore.getInventory(id);
        List<Integer[]> pokemonList = new ArrayList<>();
                
        for (int i = 0; i < pokemons.size(); i++) {
            int pokemonId = new PokemonDAO().getPokemon(pokemons.get(i).getidInstancePokemon()).getPokemonId();
            Integer pokemonInstanceId = pokemons.get(i).getidInstancePokemon();
            Integer[] pokemon = {pokemonId, pokemonInstanceId};
            pokemonList.add(pokemon);
        }
        
        Collections.sort(pokemonList, new Comparator<Integer[]>() {
            public int compare(Integer[] a, Integer[] b) {
                return a[0].compareTo(b[0]);
            }
        });
        

        ArrayList<InventoryEntity> liste = new ArrayList<>();
        for (Integer[] pokemon : pokemonList) {
            int pokemonId = pokemon[0];
            int pokemonInstanceId = pokemon[1];

            InventoryEntity entity = new InventoryEntity();
            entity.setidInstancePokemon(pokemonInstanceId);
            liste.add(entity);
            //System.out.println("Pokemon ID: " + pokemonId + ", Instance ID: " + pokemonInstanceId);
        }


        input.put("userId", id);
        input.put("pokemons", liste);
        input.put("PokemonCore", new PokemonCore());
        input.put("tradeDAO",  new TradeDAO());
        input.put("userDAO",  new UserDAO());
        input.put("tradeIds",  new TradeDAO().getAllTradesIdOfUser(id));
        input.put("nbPendingTrades", new TradeDAO().getNbPendingTrades(id));
        
        if (request.cookies().isEmpty()) {
            input.put("currentUser", -1);
        } else {
            input.put("currentUser", Integer.parseInt(request.cookies().get("userConnected")));
        }
        

        input.put("pokemonsSize", InventoryCore.getInventory(id).size());


        
        
        if (request.queryParams("tradeid") == null) {
            input.put("currentTradeId", -1);
        } else {
            input.put("currentTradeId", Integer.parseInt(request.queryParams("tradeid")));
        }
        



        Writer output = new StringWriter();
        Template template = configuration.getTemplate("users/inventaire.ftl");
        template.setOutputEncoding("UTF-8");
        template.process(input, output);


        return output.toString();
    }
}
