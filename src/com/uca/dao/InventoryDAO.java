package com.uca.dao;

import com.uca.entity.InventoryEntity;

import java.sql.*;
import java.util.ArrayList;

public class InventoryDAO extends _Generic<InventoryEntity> {


    /**
     * renvoie les pokemons dans l'ordre ascendant 
     * @param id id de l'user
     * @return une liste de l'inventaire
     */
    public ArrayList<InventoryEntity> getInventory(int id) {
        ArrayList<InventoryEntity> entities = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = this.connect.prepareStatement("SELECT * FROM inventory WHERE USERID ="+ id +" ORDER BY pokemonInstanceId ASC;");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                //System.out.println(resultSet);
                InventoryEntity entity = new InventoryEntity();
                entity.setUserId(resultSet.getInt("userId"));
                entity.setidInstancePokemon(resultSet.getInt("pokemonInstanceId"));


                entities.add(entity);
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entities;
    }




    @Override
    public InventoryEntity create(InventoryEntity obj) {return null;}

    @Override
    public void delete(InventoryEntity obj) {}



    /**
     * cree la table inventory dans la bese de données si elle n'existe pas 
     */
    public void createTable(){
        try {
            Connection connection = _Connector.getInstance();
            PreparedStatement statement;

            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS inventory (userId int, pokemonInstanceId int);");
            statement.executeUpdate();

            

        } catch (Exception e){
            System.out.println("[Error] Exception dao.InventoryDAO.createTable()");
        }
    }


    /**
     * supprime la table si elle existe 
     */
    public void deleteTable(){
        try {
            Connection connection = _Connector.getInstance();
            PreparedStatement statement;

            statement = connection.prepareStatement("DROP TABLE inventory");
            statement.executeUpdate();

        } catch (Exception e){
            System.out.println("[Error] Exception dao.InventoryDAO.deleteTable()");
        }
        
    }


    /**
     * Retourne l'user id d'un pokmon instance id
     * @param pokemonInstanceId l'id du pokemon
     * @return l'user id
     */
    public static int getUserIdByPokemonId(int pokemonInstanceId){
        int response = -1;
        try {
            Connection connection = _Connector.getInstance();
            PreparedStatement statement;

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT userid FROM inventory WHERE pokemonInstanceId ="+ pokemonInstanceId + ";");
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                response = resultSet.getInt(1);
            }


        } catch (Exception e){
            System.out.println("[Error] Exception dao.InventoryDAO.getUserIdByPokemonId()");
        }
        return response;
    }

    /**
     * Récupère le nombre de pokémon d'un user
     * @param userId userid
     * @return le nombre de pokemons
     */
    public static int getNbPokemons(int userId){
        int response = -1;
        try {
            Connection connection = _Connector.getInstance();
            PreparedStatement statement;

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM inventory WHERE userid =" + userId + ";");
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                response = resultSet.getInt(1);
            }


        } catch (Exception e){
            System.out.println("[Error] Exception dao.InventoryDAO.getNbPokemons()");
        }
        return response;



    }


}
