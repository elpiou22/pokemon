package com.uca;

import com.uca.dao._Initializer;
import com.uca.gui.*;
import com.uca.dao.PokemonDAO;
import com.uca.dao.TradeDAO;
import com.uca.dao.UserDAO;
import com.uca.dao.InventoryDAO;
import com.uca.dao.TradeDAO;
import com.uca.PutExamples;


import static spark.Spark.*;
import org.json.JSONObject;




public class StartServer {

    public static void main(String[] args) {

        

        
        //Configure Spark
        staticFiles.location("/static/");
        port(8081);

        new InventoryDAO().deleteTable();
        new UserDAO()     .deleteTable();
        new TradeDAO()    .deleteTable();
        _Initializer.Init();

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("[Infos] Projet Pokemon: Fauchier Maceo & Loic Lebrat groupe C");
        System.out.println("[Infos] Récupération des 1010 pokemons:");
        System.out.println("[Infos] Estimation: ~5 minutes");
        PokemonDAO.init();
        System.out.println("[Infos] Récupération terminée");
        
        new PutExamples().main();
        

        //Defining our routes
        get("/users", (req, res) -> {
            return UserGUI.getAllUsers(req);
        });


        get("/user/:userId", (req, res) -> {
            int userId = Integer.parseInt(req.params("userId"));
            if (userId <= UserDAO.getMaxId()) {
                return InventoryGUI.getInventory(userId,req);
            } else {
                res.redirect("/404.html");
                return null;
            }
        });

        get("/pokemon/:pokemonId", (req, res) -> {
            int pokemonId = Integer.parseInt(req.params("pokemonId"));
            if (pokemonId <= 1010) {
                res.redirect("/404.html");
                return null;
            } else if (pokemonId <= PokemonDAO.getInstanceIdMax()) {
                return PokemonGUI.getPokemon(pokemonId, req);
            } else {
                res.redirect("/404.html");
                return null;
            }
        });




        get("/pokedex", (req, res) -> {
            res.type("text/html;charset=utf-8");
            return PokedexGUI.getPokedex(req);
        });


        get("/", (req, res) -> {
            res.redirect("/index.html");
            return null;
        });

        post("/signin", (req, res) -> {

            String firstname = req.queryParams("firstname");
            String lastname = req.queryParams("lastname");
            String pseudo = req.queryParams("pseudo");
            String email = req.queryParams("email");
            String password = req.queryParams("password");

            if (UserDAO.pseudoExists(pseudo)){
                res.status(409);
                return 409;
            } else {



                UserDAO.putUser(firstname, lastname, pseudo, email, password);
        
                res.type("application/json");
    
                int id = UserDAO.getMaxId();
                JSONObject json = new JSONObject();
                json.put("id", id);
        
                return json.toString();
            }
        });


        post("/login", (req, res) -> {
            String pseudo = req.queryParams("pseudo");
            String password = req.queryParams("password");
        
            int userId = UserDAO.checkUser(pseudo, password);
            if (userId != -1){


                UserDAO.freePokemonEligible(userId);
                UserDAO.refreshTime(userId);

                JSONObject json = new JSONObject();
                json.put("id", userId);

                res.type("application/json");
                return json.toString();

            } else {
                // user ou mdp incorrect
                res.status(401);
                return 401;
            }        
        });



        post("/tradesubmitted", (req, res) -> {
            int user1Id = Integer.parseInt(req.queryParams("user1"));         
            int user2Id = Integer.parseInt(req.queryParams("user2"));
            int pokemon1Id = Integer.parseInt(req.queryParams("pokemon1"));
            int pokemon2Id = Integer.parseInt(req.queryParams("pokemon2"));

            TradeDAO.newPendingTrade(user1Id, user2Id, pokemon1Id, pokemon2Id);
        
            res.status(200);
            return 200; 
            
        });


        post("/upgrade", (req, res) -> {
            int user = Integer.parseInt(req.queryParams("user"));         
            int pokemon = Integer.parseInt(req.queryParams("pokemon"));

            int response = PokemonDAO.upgradePokemon(user, pokemon);
            int responseHttp;

            
            if (response == 1){

                res.status(200);
                responseHttp = 200;

            } else if (response == 0){
                res.status(409); 
                responseHttp = 409;
            } else {

                res.status(429); 
                responseHttp = 429;


            }

            return responseHttp;
            
        });


        post("/acceptTrade", (req, res) -> {
            int tradeId = Integer.parseInt(req.queryParams("tradeid"));         

            TradeDAO.acceptTrade(tradeId);

            return 200;
        });

        post("/refuseTrade", (req, res) -> {
            int tradeId = Integer.parseInt(req.queryParams("tradeid"));         

            TradeDAO.refuseTrade(tradeId);

            return 200;
        });









        // erreur 404:
        
        notFound((req, res) -> {
            res.redirect("/404.html");
            return null;
        });
        
        
    }
}