package org.zoo;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.*;

public class Zoo {
    private String name;
    private List<enclosure<? extends Animals>> enclosureList;

    public Zoo(String name) {
        this.name = name;
        this.enclosureList = new ArrayList<>();
    }

    /// ////////////////////////////////////////////////////////////////////////////
    public boolean addenclosure(enclosure<?> enclosure) {
        enclosureList.add(enclosure);
        return true;
    }

    public List getenclosure() {
        return enclosureList;
    }

    public enclosure<?> finenclosurebyname(String name) {
        if (enclosureList.contains(name)) {
            for (enclosure<?> enclosure : enclosureList) {
                if (enclosure.getname().equals(name)) {
                    return enclosure;
                }
            }
        }
        return null;
        //stream vergessen
        ////////////////////////////////////////////////////////////////////////////////////////

    }

    public List<Animals> getanimals() {
        return enclosureList.stream()
                .flatMap(enclosure -> enclosure.getinhabitants().stream())
                .map(animal -> (Animals) animal)
                .toList();
    }

    public List<Mammal> getallmammals() {
        return enclosureList.stream()
                .flatMap(enclosure -> enclosure.getinhabitants().stream())
                .filter(animal -> animal instanceof Mammal)
                .map(animal -> (Mammal) animal)
                .toList();
    }
    public List<> getbypredicate(Animals A){
        return enclosureList.stream()
                .flatMap(enclosure -> enclosure.getinhabitants().stream())
                .filter(animal -> animal.equals(A))
                .toList();
    }


    public int countanimalsbytype() {
        return enclosureList.stream()
                .flatMap(enclosure -> enclosure.getinhabitants().stream())
                .map(animal -> animal.getClass().getSimpleName())
                .collect(Collectors.groupingBy(type -> type, Collectors.counting()))
                .size();
    }

}