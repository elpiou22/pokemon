package com.uca.entity;

import java.sql.Timestamp;
import java.util.ArrayList;

public class PokemonEntity {
    private String name;
    private int idPokemon; // id pokemon entre 1 et 1010
    private int pokemonInstanceId; // id total du pokemon
    private boolean legendary = false;
    private boolean mythical = false;
    private ArrayList<String> types;
    private String imageUrl;
    private int level;
    
    

    public PokemonEntity(String name, int idPokemon, int pokemonInstanceId, boolean legendary, boolean mythical, ArrayList<String> types, String imageUrl, int level) {
        this.name = name;
        this.idPokemon = idPokemon;
        this.pokemonInstanceId = pokemonInstanceId;
        this.legendary = legendary;
        this.mythical = mythical;
        this.types = types;
        this.imageUrl = imageUrl;
        this.level = level;
    }

    /**
     * Remplace certains caractères spéciaux par leur valeurs HTML
     * Sur certains ordinateurs il suffit juste de rajouter "res.type("text/html;charset=utf-8");" coté serveur mais sur d'autres ça ne fonctionne pas
     * @return le nom avec les caractères compatibles HTML
     */
    public String getName() {
        
        if(name.contains("♀")) {
            return name.replace("♀", "&female;");
        } else if(name.contains("é")) {
            return name.replace("é", "&eacute;");
        } else if(name.contains("ê")) {
            return name.replace("ê", "&ecirc;");
        } else if(name.contains("É")) {
            return name.replace("É", "&Eacute;");
        } else if(name.contains("è")) {
            return name.replace("è", "&egrave;");
        } else if(name.contains("à")) {
            return name.replace("à", "&agrave;");
        } else if(name.contains("ù")) {
            return name.replace("ù", "&ugrave;");
        } else if(name.contains("ï")) {
            return name.replace("ï", "&iuml;");
        } else if(name.contains("ç")) {
            return name.replace("ç", "&ccedil;");
        } else {
            return name;
        }

    }

    /**
     * Spécifie le nom de cette entité
     * @param name le nom
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Récupère le pokemon id de cette entité
     * @return le pokemon id
     */
    public int getPokemonId() {
        return idPokemon;
    }

    /**
     * Spécifie le pokemon id de cette entité
     * @param id pokemon id
     */
    public void setPokemonId(int id) {
        this.idPokemon = id;
    }

    /**
     * Récupère le boolean légendaire de cette entité
     * @return boolean
     */
    public boolean getLegendary() {
        return legendary;
    }

    /**
     * Spécifie le boolean légendaire de cette entité
     * @param bool boolean
     */
    public void setLegendary(boolean bool) {
        this.legendary = bool;
    }

    /**
     * Récupère le boolean mythic de cette entité
     * @return boolean
     */
    public boolean getMythical() {
        return mythical;
    }

    /**
     * Spécifie le boolean mythic de cette entité
     * @param bool boolean
     */
    public void setMythical(boolean bool) {
        this.mythical = bool;
    }

    /**
     * Récupère les types de cette entité
     * @return liste de types
     */
    public ArrayList<String> getTypes() {
        return types;
    }

    /**
     * Spécifie les types de cette entité
     * @param types liste de Strings
     */
    public void setTypes(ArrayList<String> types) {
        this.types = types;
    }

    /**
     * Récupère le lien de l'image de cette entité
     * @return l'url en String
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Spécifie le lien de l'image de cette entité
     * @param imageUrl l'url en String
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    /**
     * Récupère la rareté de cette entité 
     * @return String
     */
    public String getRarity(){

        if (legendary) {
            return "legendary";
            
        } else if (mythical) {
            return "mythical";
        } else {
            return "common";
        }
    }

    /**
     * Récupère le pokemon instance id de cette entité
     * @return pokemon instance id
     */
    public int getPokemonInstanceId() {
        return pokemonInstanceId;
    }

    /**
     * Spécifie le pokemon isntance id de cette entité
     * @param pokemonInstanceId pokemon instance id
     */
    public void setPokemonInstanceId(int pokemonInstanceId) {
        this.pokemonInstanceId = pokemonInstanceId;
    }

    /**
     * Récupère le level de cette entité
     * @return le level
     */
    public int getLevel() {
        return level;
    }
    
    /**
     * Spécifie le level de cette entité
     * @param level level
     */
    public void setLevel(int level) {
        this.level = level;
    }


}
