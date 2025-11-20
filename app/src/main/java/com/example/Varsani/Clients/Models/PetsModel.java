package com.example.Varsani.Clients.Models;

public class PetsModel {

    private String petName;
    private String species;
    private String breed;
    private String gender;
    private String dob;
    private String weight;
    private String petID;

    public PetsModel(String petName, String species, String breed, String gender, String dob, String weight, String petID) {
        this.petName = petName;
        this.species = species;
        this.breed = breed;
        this.gender = gender;
        this.dob = dob;
        this.weight = weight;
        this.petID = petID;
    }

    public String getPetName() {
        return petName;
    }

    public String getSpecies() {
        return species;
    }

    public String getBreed() {
        return breed;
    }

    public String getGender() {
        return gender;
    }

    public String getDob() {
        return dob;
    }

    public String getWeight() {
        return weight;
    }

    public String getPetID() {
        return petID;
    }
}
