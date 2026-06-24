package org.zoo;

import java.util.ArrayList;
import java.util.List;

public class enclosure <T extends Animals> {

    private String name;
    private List<T> AnimalsList;


    public enclosure(String name) {
        this.name = name;
        this.AnimalsList = new ArrayList<>();

    }

public String getname() {
        return name;
}


    ////////////////////////////////////////////////////////////////////////////////////////////////////
    public boolean addlist(T Animal) {
        if (AnimalsList.contains(Animal)) {
            return false;
        } else {
            AnimalsList.add(Animal);
            return true;
        }
    }
    public boolean removeList (T animals){
        if (AnimalsList.contains(animals)) {
            AnimalsList.remove(animals);
            return true;
        } else {
            return false;
        }
    }
    public List<T> getinhabitants() {
        return AnimalsList.stream().toList();
    }



}
