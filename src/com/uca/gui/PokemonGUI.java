package com.uca.gui;


import com.uca.core.PokemonCore;
import com.uca.core.InventoryCore;
import com.uca.dao.UserDAO;
import com.uca.dao.TradeDAO;
import com.uca.dao.PokemonDAO;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import com.uca.entity.InventoryEntity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;



import spark.Request;


import static spark.Spark.*;






public class PokemonGUI {

    /**
     * Affichage de la page FTL pokemon.ftl
     * @param pokemonInstanceId pokemon instance id
     * @param request la requete coté serveur pour avoir accès à l'url
     * @return la liste des arguments dont le ftl peut avoir accès
     * @throws IOException
     * @throws TemplateException
     */
    public static String getPokemon(int pokemonInstanceId, Request request) throws IOException, TemplateException {
        //System.out.println(request.url() + request.queryString());



        Configuration configuration = _FreeMarkerInitializer.getContext();
        Map<String, Object> input = new HashMap<>();



        input.put("pokemonId", pokemonInstanceId);
        input.put("PokemonCore", new PokemonCore());
        input.put("pokemons", InventoryCore.getInventory(UserDAO.getOwnerOfPokemonId(pokemonInstanceId).getId()));
        input.put("InventoryCore", new InventoryCore());

        input.put("userDAO",  new UserDAO());
        input.put("tradeDAO",  new TradeDAO());

        input.put("currentUrl", request.url() + "?" + request.queryString());


        int currentUser = 1;
        if (request.cookies().isEmpty()) {
            input.put("currentUser", -1);

        } else {
            input.put("currentUser", Integer.parseInt(request.cookies().get("userConnected")));
            currentUser = Integer.parseInt(request.cookies().get("userConnected"));
        }

        ArrayList<InventoryEntity> pokemons = InventoryCore.getInventory(currentUser);
        List<Integer[]> pokemonList = new ArrayList<>();
                
        for (int i = 0; i < pokemons.size(); i++) {
            int pokemonId = new PokemonDAO().getPokemon(pokemons.get(i).getidInstancePokemon()).getPokemonId();
            Integer pokemonInstanceId2 = pokemons.get(i).getidInstancePokemon();
            Integer[] pokemon = {pokemonId, pokemonInstanceId2};
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
            int pokemonInstanceId2 = pokemon[1];

            InventoryEntity entity = new InventoryEntity();
            entity.setidInstancePokemon(pokemonInstanceId2);
            liste.add(entity);
            //System.out.println("Pokemon ID: " + pokemonId + ", Instance ID: " + pokemonInstanceId);
        }

        
        input.put("pokemonsOfCurrentUser", liste);

        






        Writer output = new StringWriter();
        Template template = configuration.getTemplate("pokemons/pokemon.ftl"); 

        template.setOutputEncoding("UTF-8");
        template.process(input, output);


        return output.toString();
    }

    
}
