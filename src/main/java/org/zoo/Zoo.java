package org.zoo;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class Zoo {
    private String name;
    private List<enclosure<? extends Animals>> enclosureList;

    public Zoo(String name) {
        this.name = name;
        this.enclosureList = new ArrayList<>();
    }
///////////////////////////////////////////////////////////////////////////////
    public boolean addenclosure(enclosure<?> enclosure) {
        enclosureList.add(enclosure);
        return true;
    }

    public List getenclosure() {
        return enclosureList;
    }

    public enclosure<?> finenclosurebyname(String name){
        if (enclosureList.contains(name)){
            for (enclosure<?> enclosure : enclosureList) {
                if (enclosure.getname().equals(name)) {
                    return enclosure;
                }
            }
        }
            return null;
    }
    ////////////////////////////////////////////////////////////////////////////////////////


    public List getanimals(){
        List<Animals> animalsList = new ArrayList<>();
        for (enclosure<?> enclosure : enclosureList) {
            animalsList.addAll(enclosure.getinhabitants());
        }
        return animalsList;
    }






}
