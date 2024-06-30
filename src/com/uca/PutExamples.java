package com.uca;

import com.uca.dao.PokemonDAO;
import com.uca.dao.TradeDAO;
import com.uca.dao.UserDAO;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



public class PutExamples {

    private static final int NUMBER_POKEMONS = 1010;


    public void main() {
    

        System.out.println("-------");
        System.out.println("[Infos] Création d'exemples pour effectuer des tests");
        System.out.println("[Infos] Données des comptes tests:");
        System.out.println("[Infos]          pseudo       /  password");
        System.out.println("[Infos] User1:  LilaJones123  /   Secret123!");
        System.out.println("[Infos] User2:  M.Kumar       /   Passw0rd$");
        System.out.println("[Infos] User3:  Maria         /   Gonza2023!");
        System.out.println("[Infos] User4:  FauxDav       /   W0ngD@vid!");
        System.out.println("[Infos] User5:  BestLeeSoo    /   LeeSoojin2023#");

 



        UserDAO.putUser("Jones", "Lila", "LilaJones123", "lila.jones@example.com", hashPassword("Secret123!" + "$" + "LilaJones123")); // 20 +1 
        UserDAO.putUser("Kumar", "Ravi", "M.Kumar", "ravi.kumar@example.com", hashPassword("Passw0rd$" + "$" + "M.Kumar"));             // 20+2
        UserDAO.putUser("Gonzalez", "Maria", "Maria", "maria.gonzalez@example.com", hashPassword("Gonza2023!" + "$" + "Maria"));        // 20+3
        UserDAO.putUser("Wong", "David", "FauxDav", "david.wong@example.com", hashPassword("W0ngD@vid!" + "$" + "FauxDav"));            // 20+4
        UserDAO.putUser("Lee", "Soojin", "BestLeeSoo", "soojin.lee@example.com", hashPassword("LeeSoojin2023#" + "$" + "BestLeeSoo"));  // 20+5

 
        PokemonDAO.addPokemon(1, 888); // 20 +6
        PokemonDAO.addRdmPokemon(1);    // 27
        PokemonDAO.addRdmPokemon(1);   // 28
        PokemonDAO.addRdmPokemon(1);    //29
        
        PokemonDAO.addPokemon(2, 889);   //30
        PokemonDAO.addRdmPokemon(2);    //31
        PokemonDAO.addRdmPokemon(2);   //32
        PokemonDAO.addRdmPokemon(2);    //33
        PokemonDAO.addRdmPokemon(2);   //34
        PokemonDAO.addRdmPokemon(2);    //35
        
        PokemonDAO.addPokemon(3, 893);   //36
        PokemonDAO.addRdmPokemon(3);    //37
        PokemonDAO.addRdmPokemon(3);   //38
        
        PokemonDAO.addPokemon(4, 807);   //39
        PokemonDAO.addRdmPokemon(4);    //40
        PokemonDAO.addRdmPokemon(4);   //41
        PokemonDAO.addRdmPokemon(4);   //42
        PokemonDAO.addRdmPokemon(4);    //43
        PokemonDAO.addRdmPokemon(4);   //44
        
        PokemonDAO.addPokemon(5, 892);   //45
        PokemonDAO.addRdmPokemon(5);    //46
        PokemonDAO.addRdmPokemon(5);    //47
        PokemonDAO.addRdmPokemon(5);   //48
        PokemonDAO.addRdmPokemon(5);   //49
        PokemonDAO.addRdmPokemon(5);   //50
        


        TradeDAO.newPendingTrade(1, 2, NUMBER_POKEMONS + 6 , NUMBER_POKEMONS + 12);
        TradeDAO.newPendingTrade(3, 4, NUMBER_POKEMONS + 16, NUMBER_POKEMONS + 21);
        TradeDAO.newPendingTrade(1, 3, NUMBER_POKEMONS + 9 , NUMBER_POKEMONS + 18);
        TradeDAO.newPendingTrade(1, 3, NUMBER_POKEMONS + 9 , NUMBER_POKEMONS + 18); // check si l'autre trade se supprime bien après la validation d'un pokemon déjà présent dans un trade
        TradeDAO.newPendingTrade(5, 1, NUMBER_POKEMONS + 25, NUMBER_POKEMONS + 17);


        PokemonDAO.upgradePokemon(1, NUMBER_POKEMONS +6);
        PokemonDAO.upgradePokemon(1, NUMBER_POKEMONS +6);
        PokemonDAO.upgradePokemon(1, NUMBER_POKEMONS +6);






        System.out.println("[Infos] Fin exemples");
        System.out.println("-----");
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
    

}
