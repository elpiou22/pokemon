package com.uca.entity;

import java.sql.Timestamp;
import java.util.Date;
import java.text.SimpleDateFormat;


public class UserEntity {
    private int id;
    private String firstName;
    private String lastName;
    private String pseudo;
    private String email;
    private String password;
    private long lastConnection;
    private String titre;

    public UserEntity() {
        
    }


    /**
     * Récèpre l'id de cette entité
     * @return user id
     */
    public int getId() {
        return id;
    }

    /**
     * Spécifie l'user id de cette entité
     * @param id user id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Récupère le firstname de cette entité
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Spécifie le first name de cette entité
     * @param firstName first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Récupère le lastname de cette entité
     * @return lastname
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Spécifie le last name de cette entité
     * @param lastName lastname
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Récupère le pseudo de cette entité
     * @return pseudo
     */
    public String getPseudo() {
        return pseudo;
    }

    /**
     * Spécifie le pseudo de cette entité
     * @param pseudo pseudo
     */
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    /**
     * Récupère l'email  de cette entité
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Spécifie l'email  de cette entité
     * @param email email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Récupère le password de cette entité
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Spécifie le password de cette entité
     * @param password password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Récupère la derniere connexion de cette entité
     * @return la derniere connexion
     */
    public long getLastConnection() {
        return lastConnection;
    }

    /**
     * Récupère la derniere connexion de cette entité en format string
     * @return la derniere connexion
     */
    public String getLastConnectionStr() {
        Date date = new Date(lastConnection);

        SimpleDateFormat dateStr = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return dateStr.format(date);
    }

    /**
     * Spécifie la derniere connexion de cette entité
     * @param lastConnection derniere connexion
     */
    public void setLastConnection(long lastConnection) {
        this.lastConnection = lastConnection;
    }

    /**
     * Récupère le titre de cette entité.
     * @return le titre
     */
    public String getTitre() {
        return this.titre;
    }

    /**
     * Spécifie le titre de cette entité.
     * @param titre le titre
     */
    public void setTitre(int nbPok) {

        if (nbPok < 10) {
            this.titre = "Dresseur d&eacute;butant";
        } else if (nbPok < 50) {
            this.titre = "Dresseur novice";
        } else if (nbPok < 100) {
            this.titre = "Dresseur expert";
        } else {
            this.titre = "Dresseur l&eacute;gendaire";
        }
    }


}
