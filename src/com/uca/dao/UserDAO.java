package com.uca.dao;

import com.uca.dao.InventoryDAO;
import com.uca.entity.UserEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

public class UserDAO extends _Generic<UserEntity> {

    private static final int INTERVAL = 60 * 60 * 24; // en secondes


    /**
     * Récupère tout les users sous forme d'une liste
     * @return une liste de tout les users
     */
    public ArrayList<UserEntity> getAllUsers(){
        ArrayList<UserEntity> entities = new ArrayList<>();
        int maxId = getMaxId();
        for (int i = 0 + 1; i < maxId + 1; i++) {
            entities.add(getUserById(i));
        }
        return entities;
    }


    /**
     * Récupère l'user id le plus haut
     * @return l'user le plus haut
     */
    public static int getMaxId(){
        Connection connection = _Connector.getInstance();
        int maxId =0;
        try{
            PreparedStatement statement = connection.prepareStatement("SELECT MAX(id) FROM users");
            ResultSet resultSet = statement.executeQuery();
    
            if (resultSet.next()) {
                maxId = resultSet.getInt(1);
                
                //System.out.println("[Debug] Max ID: " + maxId);
            }

        } catch(Exception e){
            System.out.println("[Error] Exception dao.UserDAO.getMaxId()");
        }
        return maxId;
    }


    /**
     * Récupère la derniere connexion dans le sql
     * @param id userid
     * @return date en milliseconds
     */
    public static long getLastConnection(int id){
        Connection connection = _Connector.getInstance();
        long con = 0;
        try{
            PreparedStatement statement = connection.prepareStatement("SELECT lastConnection FROM users where id = " + id + "");
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                con = resultSet.getLong("lastConnection");
            }

            //System.out.println("[Debug] last con: " + con + "   time now: " + System.currentTimeMillis() + "   diff: " + (System.currentTimeMillis() - con) +"");

        } catch(Exception e){
            System.out.println("[Error] Exception dao.UserDAO.getLastConnection()");
        }
        return con;
    }


    /**
     * Récupère la date de création du compte
     * @param userId user id
     * @return Date sous forme d'un string en dd/MM/yyyy HH:mm:ss
     */
    public static String getCreationDate(int userId){
        Connection connection = _Connector.getInstance();
        long dateInMs;
        String response =null;
        try{
            PreparedStatement statement = connection.prepareStatement("SELECT creationDate FROM users where id = " + userId + "");
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                dateInMs = resultSet.getLong("creationDate");
            
                Date date = new Date(dateInMs);

                SimpleDateFormat dateStr = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                response = dateStr.format(date);
            }

        } catch(Exception e){
            System.out.println("[Error] Exception dao.UserDAO.getCreationDate()");
        }

        return response;
    }



    /**
     * Ajoute un user dans le sql et lui affecte un pokemon aléatoire
     * @param firstname prenom
     * @param lastname nom de famille
     * @param pseudo pseudo
     * @param email email
     * @param password pwd
     */
    public static void putUser(String firstname, String lastname, String pseudo, String email, String password){
        try {
            Connection connection = _Connector.getInstance();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO users(firstname, lastname, pseudo, email, password, lastConnection, creationDate) VALUES(?, ?, ?, ?, ?, ?, ?);");
            statement.setString(1, firstname);
            statement.setString(2, lastname);
            statement.setString(3, pseudo);
            statement.setString(4, email);
            statement.setString(5, password);
            statement.setLong  (6, System.currentTimeMillis()); // last connexion
            statement.setLong  (7, System.currentTimeMillis()); // last connexion            
            statement.executeUpdate();

            statement = connection.prepareStatement("SELECT MAX(id) FROM users");
            ResultSet resultSet = statement.executeQuery();

            PokemonDAO.addRdmPokemon(UserDAO.getMaxId());

        } catch (Exception e){
            System.out.println("[Error] Exception dao.UserDAO.putUser()");
        }
    }


    @Override
    public UserEntity create(UserEntity obj) {return null;}

    @Override
    public void delete(UserEntity obj) {}


    /**
     * Créé la table sql "users"
     */
    public void createTable(){
        try {
            Connection connection = _Connector.getInstance();
            PreparedStatement statement;

            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS users (id int primary key auto_increment, firstname varchar(100), lastname varchar(100), pseudo varchar(100), email varchar(100), password varchar(100), lastConnection long, nbUpgrades INT DEFAULT 5, creationDate long); ");
            statement.executeUpdate();

            

        } catch (Exception e){
            System.out.println("[Error] Exception dao.UserDAO.CreateTable()");
        }
    }


    /**
     * Supprime la table sql "users"
     */
    public void deleteTable(){
        try {
            Connection connection = _Connector.getInstance();
            PreparedStatement statement;

            statement = connection.prepareStatement("DROP TABLE users");
            statement.executeUpdate();

            

        } catch (Exception e){
            System.out.println("[Error] Exception dao.UserDAO.deleteTable()");
        }
    }


    /**
     * Vérifie si le password est correct pr le pseudo
     * @param pseudo pseudo de l'user
     * @param password pwd de l'user
     * @return renvoie l'id de l'user si c'est les bons identifiants et renvoie -1 si c'est incorrect
     */
    public static int checkUser(String pseudo, String password) {
        int result = -1;
        Connection connection = _Connector.getInstance();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE pseudo = ? AND password = ?");

            statement.setString(1, pseudo);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                result = resultSet.getInt("id");
            }


        } catch (SQLException e) {
            System.out.println("[Error] Exception dao.UserDAO.checkUser()");
        }
        return result;
    }


    /**
     * Vérifie si un pseudo existe dans la bdd
     * @param pseudo pseudo
     * @return true si il existe et false si il n'existe pas
     */
    public static boolean pseudoExists(String pseudo) {
        boolean result = false;
        Connection connection = _Connector.getInstance();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) AS count FROM users WHERE pseudo = ?");
            statement.setString(1, pseudo);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                if (count > 0) {
                    result = true;
                }
            }
        } catch (SQLException e) {
            System.out.println("[Error] Exception dao.UserDAO.pseudoExists()");
        }
        return result;
    }

    

    /**
     * Check si l'user est éligible a recevoir un nouveau pokemon
     * Si oui, ajoute un nouveau pokemon a son inventaire
     * Si oui, reset aussi le nombre d'upgrades (ça veut dire que ça fait 24h)
     * @param userId userid
     */
    public static void freePokemonEligible(int userId) {


        long actual_timer = System.currentTimeMillis(); 

        if ((actual_timer - UserDAO.getLastConnection(userId)) > (INTERVAL * 1000)){

            //System.out.println("[Debug] Intervalle assez longue, on ajoute un nouveau pokemon");
            PokemonDAO.addRdmPokemon(userId);

            // update sql
            Connection connection = _Connector.getInstance();
            try{
                PreparedStatement statement = connection.prepareStatement("UPDATE users SET lastConnection = ? WHERE id = ?");
                statement.setLong(1, actual_timer);
                statement.setInt(2, userId);
                int rowsAffected = statement.executeUpdate();
    
    
                if (rowsAffected > 0) {
                    //System.out.println("[Debug] lastConnection successfully updated");
                } else {
                    System.out.println("[Error] lastConnection 's update failed for user ID: " + userId);
                } 

                statement = connection.prepareStatement("UPDATE users SET nbUpgrades = () WHERE id = ?");
                statement.setInt(1, userId);
                statement.executeUpdate();
    
            } catch(Exception e){
                System.out.println("[Error] Exception dao.UserDAO.freePokemonEligible()");
            }
        }
    }
    

    /**
     * Récupère l'entité user grace a son id
     * @param userId user id
     * @return UserEntity
     */
    public static UserEntity getUserById(int userId){
        Connection connection = _Connector.getInstance();
        UserEntity entity = new UserEntity();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE id = ?");
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                entity.setId            (resultSet.getInt   ("id"));
                entity.setPseudo        (resultSet.getString("pseudo"));
                entity.setEmail         (resultSet.getString("email"));
                entity.setPassword      (resultSet.getString("password"));
                entity.setFirstName     (resultSet.getString("firstname"));
                entity.setLastName      (resultSet.getString("lastname"));
                entity.setLastConnection(resultSet.getLong  ("lastconnection"));
                entity.setTitre(InventoryDAO.getNbPokemons(userId));
            }


        } catch (SQLException e) {
            System.out.println("[Error] Exception dao.UserDAO.getUserById()");
            e.printStackTrace();
        }
        return entity;
    }


    /**
     * Récupère l'entité user grâce a son pokemon
     * @param pokemonInstanceId pokemon instance id
     * @return UserEntity
     */
    public static UserEntity getOwnerOfPokemonId(int pokemonInstanceId){
        int userId = InventoryDAO.getUserIdByPokemonId(pokemonInstanceId);
        UserEntity user = getUserById(userId);
        return user;
    }


    /**
     * Récupère le nombre d'améliorations disponible pour un user
     * @param userId user id
     * @return le nombre d'améliorations disponibles
     */
    public static int getNbUserUpgrades(int userId){
        Connection connection = _Connector.getInstance();
        int nb = 0;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT nbUpgrades FROM users WHERE id = ?");
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                nb = resultSet.getInt(1);
                
            }


        } catch (SQLException e) {
            System.out.println("[Error] Exception dao.UserDAO.getNbUserUpgrades()");
            e.printStackTrace();
        }
        return nb;

    }


    /**
     * Décrémente de 1 le nombre d'améliorations disponibles
     * @param userId user id
     */
    public static void decreaseUserUpgrade(int userId){
        Connection connection = _Connector.getInstance();
        try{
            PreparedStatement statement = connection.prepareStatement("UPDATE users SET nbUpgrades = ? WHERE id = ?");
            statement.setLong(1, getNbUserUpgrades(userId) - 1 );
            statement.setInt(2, userId);
            int rowsAffected = statement.executeUpdate();


            if (rowsAffected > 0) {
                //System.out.println("[Debug] nbUpgrades successfully updated");
            } else {
                System.out.println("[Error] nbUpgrades 's update failed for user ID: " + userId);
            } 

        } catch(Exception e){
            System.out.println("[Error] Exception dao.UserDAO.decreaseUserUpgrade()");
        }
    }

    /**
     * Actualise la valeur "lastconnection" de la bdd
     * @param userId userid
     */
    public static void refreshTime(int userId){
        Connection connection = _Connector.getInstance();
        try{
            PreparedStatement statement = connection.prepareStatement("UPDATE users SET lastconnection = ? WHERE id = ?");
            statement.setLong(1, System.currentTimeMillis());
            statement.setInt(2, userId);
            statement.executeUpdate();


        } catch(Exception e){
            System.out.println("[Error] Exception dao.UserDAO.refreshTime()");
        }

    }


}
 