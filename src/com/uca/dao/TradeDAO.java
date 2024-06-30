package com.uca.dao;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;


import com.uca.entity.PokemonEntity;

public class TradeDAO {
    
    /**
     * initialise la table "trades" dans le sql
     */
    public void createTable(){
        try {
            Connection connection = _Connector.getInstance();
            PreparedStatement statement;

            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS trades (tradeId INT NOT NULL AUTO_INCREMENT PRIMARY KEY, userId1 int, userId2 int, pokemonInstanceId1 int, pokemonInstanceId2 int, date long);");
            statement.executeUpdate();

        } catch (Exception e){
            System.out.println("-----");
            System.out.println("[Error] Exception dao.TradeDAO.createTable()");
            System.out.println("-----");
        }
    }


    /**
     * Supprime la table "trades"
     */
    public void deleteTable(){
        try {
            Connection connection = _Connector.getInstance();
            PreparedStatement statement;

            statement = connection.prepareStatement("DROP TABLE trades");
            statement.executeUpdate();

            

        } catch (Exception e){
            System.out.println("-----");
            System.out.println("[Error] Exception dao.TradeDAO.deleteTable()");
            System.out.println("-----");
        }
    }


    

    /**
     * Créé une nouvelle demande de trade dans le sql
     * @param user1 l'user 1 dans le sql
     * @param user2 l'user 2 dans le sql 
     * @param pokemon1Id le pokemon 1 dans le sql
     * @param pokemon2Id le pokemon 1 dans le sql
     */
    public static void newPendingTrade(int user1, int user2, int pokemon1Id, int pokemon2Id){
        /*
         * user 1 envoie le pokemon1Id au user 2
         * user 2 envoie le pokemon2Id au user 1
         * 
         * ex:
         * newPendingTrade(5,2,15,30)
         * l'user 5 envoie son pokemon 15 à l'user 2 en échange de son pokemon 30
         */
        try {
            Connection connection = _Connector.getInstance();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO trades(userId1, userId2, pokemonInstanceId1, pokemonInstanceId2, date) VALUES(?, ?, ?, ?, ?);");
            statement.setInt(1, user1);
            statement.setInt(2, user2);
            statement.setInt(3, pokemon1Id);
            statement.setInt(4, pokemon2Id);
            statement.setLong(5, System.currentTimeMillis()); // current time
            statement.executeUpdate();
            //System.out.println("[Debug] new pending trade, " + user1 + ", "  + user2 + ", " + pokemon1Id + ", " + pokemon2Id + " ");
            

        } catch (Exception e) {
            System.out.println("-----");
            System.out.println("[Error] Exception dao.TradeDAO.newPendingTrade()");
            e.printStackTrace(); // imprime l'exception avec sa trace
            System.out.println("-----");
        }
    }




    /**
     * Retourne le nombre de demandes de trades pour un user
     * @param userId l'id de l'user recherché
     * @return le nombre de trades d'un user
     */
    public int getNbPendingTrades(int userId){
        int retour = 0;
        try {
            Connection connection = _Connector.getInstance();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM trades WHERE userId2 = " + userId + ";"); 
            /*
            * on choisir userid2 car:
            * userid1 et la personne qui propose un trade
            * on ne peux pas se faire un trade a soi meme
            */ 
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                retour = resultSet.getInt(1);
                //System.out.println("[Debug] User: " + userId +" possède: " + retour + " demandes de trade");
            } else {
                System.out.println("[Error] requete sql nulle");
            }

        } catch (Exception e) {
            System.out.println("-----");
            System.out.println("[Error] Exception dao.TradeDAO.getNbPendingTrades()");
            e.printStackTrace(); // imprime l'exception avec sa trace
            System.out.println("-----");
        }

        return retour;
    }


    /**
     * Retourne tout les trades ids d'un user
     * @param userId l'user id
     * @return une liste de tout les trade ids
     */
    public List<Integer> getAllTradesIdOfUser(int userId) {
        List<Integer> tradeIds = new ArrayList<>();
        int tradeId;
        try {
            Connection connection = _Connector.getInstance();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM trades WHERE userId2 = ?");
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            while (resultSet.next()) {
                tradeId = resultSet.getInt("tradeId");
                tradeIds.add(tradeId);
            }
    
        } catch (Exception e) {
            System.out.println("-----");
            System.out.println("[Error] Exception dao.TradeDAO.getAllTradesIdOfUser()");
            e.printStackTrace();
            System.out.println("-----");
        }
    
        return tradeIds;
    }
    



    
    /**
     * Accepte un trade puis le supprime de la bdd
     * @param tradeId le trade id
     */
    public static void acceptTrade(int tradeId) {
        int user1 = getUserId1(tradeId);
        int user2 = getUserId2(tradeId);
        int pokemon1Id = getPokemonInstanceId1(tradeId);
        int pokemon2Id = getPokemonInstanceId2(tradeId);

        Connection connection = _Connector.getInstance();

        try {
            PreparedStatement statement;

            // on ajoute dans l'user2 le pokemon de l'user 1 puis on remove de l'user2 le pokeon 2
            // remove
            statement = connection.prepareStatement("DELETE FROM inventory WHERE pokemonInstanceId = " + pokemon2Id + "and userId =" + user2 + ";");
            statement.executeUpdate();

            //add
            statement = connection.prepareStatement("INSERT INTO inventory(userId, pokemonInstanceId) VALUES(?, ?);");
            statement.setInt(1, user2);
            statement.setInt(2, pokemon1Id);
            statement.executeUpdate();


            // l'inverse pr user1
            //remove
            statement = connection.prepareStatement("DELETE FROM inventory WHERE pokemonInstanceId = " + pokemon1Id + " and userId =" + user1 + ";");
            statement.executeUpdate();
            
            //add
            statement = connection.prepareStatement("INSERT INTO inventory(userId, pokemonInstanceId) VALUES(?, ?);");
            statement.setInt(1, user1);
            statement.setInt(2, pokemon2Id);
            statement.executeUpdate();
            
            // on supprime le trade
            refuseTrade(tradeId);
            
            
            // on retire les autres trades
            statement = connection.prepareStatement("DELETE FROM trades WHERE (pokemonInstanceId1 = " + pokemon1Id + "or pokemonInstanceId1 = " + pokemon2Id + ") or (pokemonInstanceId2 = " + pokemon1Id + "or pokemonInstanceId2 = " + pokemon2Id + ") ;");
            statement.executeUpdate();

            
            
        } catch (Exception e) {
            System.out.println("-----");
            System.out.println("[Error] Exception dao.TradeDAO.acceptTrade()");
            e.printStackTrace();
            System.out.println("-----");
        }
    }


    /**
     * Refuse un trade, le supprime de la bdd
     * @param tradeId le trade id
     */
    public static void refuseTrade(int tradeId) {
        
        Connection connection = _Connector.getInstance();

        try {
            PreparedStatement statement;

            
            statement = connection.prepareStatement("DELETE FROM trades WHERE tradeId = " + tradeId + ";");
            statement.executeUpdate();

            
        } catch (Exception e) {
            System.out.println("-----");
            System.out.println("[Error] Exception dao.TradeDAO.refuseTrade()");
            e.printStackTrace();
            System.out.println("-----");
        }
    }

    /**
     * Récupère l'user1 du sql
     * @param tradeId l'id du trade
     * @return l'id de l'user 1
     */
    public static int getUserId1(int tradeId) {
        int userId1 = -1;
        try {
            Connection connection = _Connector.getInstance();
            PreparedStatement statement = connection.prepareStatement("SELECT userId1 FROM trades WHERE tradeId = ?");
            statement.setInt(1, tradeId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                userId1 = resultSet.getInt("userId1");
            }
        } catch (Exception e) {
            System.out.println("[Error] Exception dao.TradeDAO.getUserId1()");
            e.printStackTrace();
        }
        return userId1;
    }
    

    /**
     * Récupère l'user2 du sql
     * @param tradeId l'id du trade
     * @return l'id de l'user 2
     */
    public static int getUserId2(int tradeId) {
        int userId2 = -1;
        try {
            Connection connection = _Connector.getInstance();
            PreparedStatement statement = connection.prepareStatement("SELECT userId2 FROM trades WHERE tradeId = ?");
            statement.setInt(1, tradeId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                userId2 = resultSet.getInt("userId2");
            }
        } catch (Exception e) {
            System.out.println("[Error] Exception dao.TradeDAO.getUserId2()");
            e.printStackTrace();
        }
        return userId2;
    }
    
    /**
     * Récupère le pokemon1 du sql
     * @param tradeId l'id du trade
     * @return l'id du pokemon1
     */
    public static int getPokemonInstanceId1(int tradeId) {
        int pokemonInstanceId1 = -1;
        try {
            Connection connection = _Connector.getInstance();
            PreparedStatement statement = connection.prepareStatement("SELECT pokemonInstanceId1 FROM trades WHERE tradeId = ?");
            statement.setInt(1, tradeId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                pokemonInstanceId1 = resultSet.getInt("pokemonInstanceId1");
            }
        } catch (Exception e) {
            System.out.println("[Error] Exception dao.TradeDAO.getPokemonInstanceId1()");
            e.printStackTrace();
        }
        return pokemonInstanceId1;
    }
    
    /**
     * Récupère le pokemon2 du sql
     * @param tradeId l'id du trade
     * @return l'id du pokemon2
     */
    public static int getPokemonInstanceId2(int tradeId) {
        int pokemonInstanceId2 = -1;
        try {
            Connection connection = _Connector.getInstance();
            PreparedStatement statement = connection.prepareStatement("SELECT pokemonInstanceId2 FROM trades WHERE tradeId = ?");
            statement.setInt(1, tradeId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                pokemonInstanceId2 = resultSet.getInt("pokemonInstanceId2");
            }
        } catch (Exception e) {
            System.out.println("[Error] Exception dao.TradeDAO.getPokemonInstanceId2()");
            e.printStackTrace();
        }
        return pokemonInstanceId2;
    }
    

    /**
     * Récupère la date d'un trade
     * @param tradeId
     * @return la date sous forme de milliseconds
     */
    public static long getDate(int tradeId) {
        long date = -1;
        try {
            Connection connection = _Connector.getInstance();
            PreparedStatement statement = connection.prepareStatement("SELECT date FROM trades WHERE tradeId = ?");
            statement.setInt(1, tradeId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                date = resultSet.getLong("date");
            }
        } catch (Exception e) {
            System.out.println("[Error] Exception dao.TradeDAO.getDate()");
            e.printStackTrace();
        }
        return date;
    }

    /**
     * Convertit des millisecondes en Texte dd/MM/yyyy HH:mm:ss
     * @param ms nombre de ms
     * @return String dd/MM/yyyy HH:mm:ss
     */
    public static String getDateStr(long ms){
        Date date = new Date(ms);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(date);
    }
    

}
