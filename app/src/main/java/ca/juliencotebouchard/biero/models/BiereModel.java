package ca.juliencotebouchard.biero.models;

/**
 * Created by e1595070 on 2017-04-13.
 */

public class BiereModel {
    private String id_biere;
    private String description;
    private String nom;
    private String brasserie;
    private String image;
    private String date_ajout;
    private String moyenne;
    private String nombre;

    public String getId_biere() {
        return id_biere;
    }

    public void setId_biere(String id_biere) {
        this.id_biere = id_biere;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getBrasserie() {
        return brasserie;
    }

    public void setBrasserie(String brasserie) {
        this.brasserie = brasserie;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate_ajout() {
        return date_ajout;
    }

    public void setDate_ajout(String date_ajout) {
        this.date_ajout = date_ajout;
    }

    public String getMoyenne() {
        return moyenne;
    }

    public void setMoyenne(String moyenne) {
        this.moyenne = moyenne;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}




