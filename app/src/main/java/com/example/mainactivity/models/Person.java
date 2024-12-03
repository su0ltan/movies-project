// In com.example.mainactivity.models.Person.java

package com.example.mainactivity.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import java.util.List;

public class Person {
    private String name;
    // You can add other fields as needed

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static List<Person> parsePerson(String jsonResponse) {
        List<Person> personList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray castArray = jsonObject.getJSONArray("cast");
            for (int i = 0; i < castArray.length(); i++) {
                JSONObject personObject = castArray.getJSONObject(i);
                String name = personObject.getString("name");
                Person person = new Person(name);
                personList.add(person);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return personList;
    }
}
