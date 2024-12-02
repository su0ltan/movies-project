package com.example.mainactivity.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Person {
    private boolean adult;
    private int gender;
    private int id;
    private String knownForDepartment;
    private String name;
    private String originalName;
    private double popularity;
    private String profilePath;
    private int castId;
    private String character;
    private String creditId;
    private int order;

    // Constructor
    public Person(boolean adult, int gender, int id, String knownForDepartment, String name,
                  String originalName, double popularity, String profilePath, int castId,
                  String character, String creditId, int order) {
        this.adult = adult;
        this.gender = gender;
        this.id = id;
        this.knownForDepartment = knownForDepartment;
        this.name = name;
        this.originalName = originalName;
        this.popularity = popularity;
        this.profilePath = profilePath;
        this.castId = castId;
        this.character = character;
        this.creditId = creditId;
        this.order = order;
    }

    // Getters and Setters
    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKnownForDepartment() {
        return knownForDepartment;
    }

    public void setKnownForDepartment(String knownForDepartment) {
        this.knownForDepartment = knownForDepartment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public int getCastId() {
        return castId;
    }

    public void setCastId(int castId) {
        this.castId = castId;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    // toString method for easy representation
    @Override
    public String toString() {
        return "Person{" +
                "adult=" + adult +
                ", gender=" + gender +
                ", id=" + id +
                ", knownForDepartment='" + knownForDepartment + '\'' +
                ", name='" + name + '\'' +
                ", originalName='" + originalName + '\'' +
                ", popularity=" + popularity +
                ", profilePath='" + profilePath + '\'' +
                ", castId=" + castId +
                ", character='" + character + '\'' +
                ", creditId='" + creditId + '\'' +
                ", order=" + order +
                '}';
    }
    public static List<Person> parsePerson(String jsonResponse) {
        List<Person> persons = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray castArray = jsonObject.getJSONArray("cast");

            for (int i = 0; i < castArray.length(); i++) {
                JSONObject personObject = castArray.getJSONObject(i);

                // Check if the person is in the Acting department
                if ("Acting".equalsIgnoreCase(personObject.optString("known_for_department", ""))) {
                    Person person = new Person(
                            personObject.optBoolean("adult", false),
                            personObject.optInt("gender", 0),
                            personObject.optInt("id", 0),
                            personObject.optString("known_for_department", ""),
                            personObject.optString("name", ""),
                            personObject.optString("original_name", ""),
                            personObject.optDouble("popularity", 0.0),
                            personObject.optString("profile_path", null),
                            personObject.optInt("cast_id", 0),
                            personObject.optString("character", ""),
                            personObject.optString("credit_id", ""),
                            personObject.optInt("order", 0)
                    );
                    persons.add(person);
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return persons;
    }

}