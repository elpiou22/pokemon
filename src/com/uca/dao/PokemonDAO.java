package com.uca.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

import org.json.JSONObject;

import com.uca.entity.PokemonEntity;

public class PokemonDAO extends _Generic<PokemonEntity> {

    private static final int NUMBER_POKEMONS = 1010;
    private static final int POKEMON_MAX_LEVEL = 100;

    


    private static ArrayList<PokemonEntity> pokemons = new ArrayList<>();
    private static int instanceId = 0;
    private static Random random = new Random();



    /**
     * créé les 1008 premiers pokemons pour accéder au pokédex
     */
    public static void init(){
        PokemonDAO pokemonDAO = new PokemonDAO();
        for (int i = 1; i < NUMBER_POKEMONS +1; i++) {
            pokemonDAO.addPokemonEntity(i);

            int progress = i * 100 / NUMBER_POKEMONS;
            // Affichage de la barre de progression
            System.out.print("\r[");
            for (int j = 0; j < 50; j++) {
                if (j < progress / 2) {
                    System.out.print("=");
                } else if (j == progress / 2) {
                    System.out.print(">");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.print("] " + progress + "%");

        }
        System.out.println();
    }


    /**
     * ajoute un pokemon aléatoire
     * @param userId l'user id
     */
    public static void addRdmPokemon(int userId){
        
        int rdm = random.nextInt(NUMBER_POKEMONS) + 1; // génère une valeur aléatoire entre 1 et 1010
        PokemonDAO.addPokemon(userId, rdm);
    }



    /**
     * add pokemon for an user
     * @param userId id user
     * @param pokemonIdnumero du pokemon (entre 1 et 1010)
     */
    public static void addPokemon(int userId, int pokemonId){
        Connection connection = _Connector.getInstance();
        try {
                      
            PreparedStatement statement = connection.prepareStatement("INSERT INTO inventory(userId, pokemonInstanceId) VALUES(?, ?);");
            //System.out.println("[Debug] userid " + userId + " / Pokemon id: " + pokemonId +" / instanceid:" + (getInstanceIdMax()+1));
            statement.setInt(1, userId); //userId
            statement.setInt(2, getInstanceIdMax()+1); //pokemonInstanceId
            addPokemonEntity(pokemonId); 
            statement.executeUpdate();
          
        } catch (Exception e){
            System.out.println("[Error] Exception dao.addPokemon.trade()");
        }
    }



    /**
     * crée une nouvelle entité en fonction de l'id du pokemon 
     * @param id l'id du pokemon (entre 1 et 1008)
     */
    public static void addPokemonEntity(int id) {

        instanceId ++;
        //id = numero pokemon
        // instanceId = id pokemon
        
        try {

            // URL de l'API Pokémon
            String url = "https://pokeapi.co/api/v2/pokemon/"+id+"/"; // valeurs
            String url2 = "https://pokeapi.co/api/v2/pokemon-species/"+id+"/"; // img

            // Connexion à l'API Pokémon
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("GET");

            HttpURLConnection con2 = (HttpURLConnection) new URL(url2).openConnection();
            con2.setRequestMethod("GET");

            
            // Récupération de la réponse JSON
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
              content.append(inputLine);
            }
            in.close();

            BufferedReader in2 = new BufferedReader(new InputStreamReader(con2.getInputStream()));
            String inputLine2;
            StringBuffer content2 = new StringBuffer();
            while ((inputLine2 = in2.readLine()) != null) {
              content2.append(inputLine2);
            }
            in2.close();


            // Création d'un objet JSON à partir de la réponse
            JSONObject response = new JSONObject(content.toString());
            JSONObject response2 = new JSONObject(content2.toString());


            // récupération infos pokemon
        
            String name = response2.getJSONArray("names").getJSONObject(4).getString("name");
            boolean leg = response2.getBoolean("is_legendary");

            boolean mythic = response2.getBoolean("is_mythical");


            ArrayList<String> types = new ArrayList<String>();
            for(int i=0; i<response.getJSONArray("types").length(); i++) {
                types.add(response.getJSONArray("types").getJSONObject(i).getJSONObject("type").getString("name"));
            }
            


            String urlImg = response.getJSONObject("sprites").getJSONObject("other").getJSONObject("official-artwork").getString("front_default");


            PokemonEntity pokemon = new PokemonEntity(name, id, instanceId, leg, mythic, types, urlImg, 1);
            

            pokemons.add(pokemon);
            } catch (Exception e) {
                System.out.println("[Error] Exception dao.addPokemonEntity.trade()");
            }
        
            //System.out.println("[Debug] new pokemon " + id + "  "  + instanceId);
    }




    /**
     * retourne un PokemonEntity en fonction de son instanceid
     * @param pokemonInstanceId l'instance id du pokemon
     * @return PokemonEntity
     */
    public static PokemonEntity getPokemon(int pokemonInstanceId) {
        return pokemons.get(pokemonInstanceId-1);
    }


    /**
     * retourne l'instance id le plus haut
     * @return l'instance id le plus haut
     */
    public static int getInstanceIdMax(){
        return pokemons.size();
    }



    /**
     * améliore le level d'un pokemon en fonction de son instanceid
     * @param userId user id
     * @param pokemonInstanceId pokemon instance id
     * @return 1 si correct, 0 si pokemon deja level max et -1 si user n'a pas d'améliorations disponibles
     */
    public static int upgradePokemon(int userId, int pokemonInstanceId){
        if (UserDAO.getNbUserUpgrades(userId) > 0) {
            
            PokemonEntity pokemon = getPokemon(pokemonInstanceId);
            if (pokemon.getLevel() == POKEMON_MAX_LEVEL) {
                return 0;
            }
            pokemon.setLevel(pokemon.getLevel()+1);
            UserDAO.decreaseUserUpgrade(userId);

            return 1;
            
        } else {
            //System.out.println("[Error] not enough upgrapes for the user: " + userId);
            return -1;
            
        }

        
    }




    @Override
    public PokemonEntity create(PokemonEntity obj) {return null;}

    @Override
    public void delete(PokemonEntity obj) {}


}
