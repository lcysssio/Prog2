package org.zoo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.*;
import java.util.stream.Collectors;

public class Zoo {

    private String name;
    private List<enclosure<? extends Animals>> enclosureList;
Logger LOGGER = Logger.getLogger(Zoo.class.getName());
    public Zoo(String name) {

        LOGGER.info("Creating Zoo: " + name);

        this.name = name;
        this.enclosureList = new ArrayList<>();

        LOGGER.fine("Zoo created successfully with name: " + name);
    }

    // ------------------------------------------------------

    public boolean addenclosure(enclosure<?> enclosure) {

        LOGGER.info("Adding enclosure: " + enclosure.getname());

        boolean added = enclosureList.add(enclosure);

        if (added) {
            LOGGER.fine("Enclosure added. Total: " + enclosureList.size());
        } else {
            LOGGER.warning("Enclosure could not be added: " + enclosure.getname());
        }

        return added;
    }

    // ------------------------------------------------------

    public List<enclosure<? extends Animals>> getenclosure() {

        LOGGER.info("Getting all enclosures.");

        LOGGER.fine("Total enclosures: " + enclosureList.size());

        return enclosureList;
    }

    // ------------------------------------------------------

    public enclosure<?> finenclosurebyname(String name) {

        LOGGER.info("Searching enclosure by name: " + name);

        for (enclosure<?> enclosure : enclosureList) {
            if (enclosure.getname().equals(name)) {

                LOGGER.fine("Enclosure found: " + name);
                return enclosure;
            }
        }

        LOGGER.warning("No enclosure found with name: " + name);
        return null;
    }

    // ------------------------------------------------------

    public List<Animals> getanimals() {

        LOGGER.info("Getting all animals.");

        List<Animals> result = enclosureList.stream()
                .flatMap(enclosure -> enclosure.getinhabitants().stream())
                .map(animal -> (Animals) animal)
                .toList();

        LOGGER.fine("Total animals found: " + result.size());

        return result;
    }

    // ------------------------------------------------------

    public List<Mammal> getallmammals() {

        LOGGER.info("Getting all mammals.");

        List<Mammal> result = enclosureList.stream()
                .flatMap(enclosure -> enclosure.getinhabitants().stream())
                .filter(animal -> animal instanceof Mammal)
                .map(animal -> (Mammal) animal)
                .toList();

        LOGGER.fine("Total mammals found: " + result.size());

        return result;
    }

    // ------------------------------------------------------

    public List<? extends Animals> getAnimalsByPredicate(
            Predicate<Animals> predicate) {

        LOGGER.info("Filtering animals by predicate.");

        List<? extends Animals> result = enclosureList.stream()
                .flatMap(enclosure -> enclosure.getinhabitants().stream())
                .filter(predicate)
                .toList();

        LOGGER.fine("Predicate matched: " + result.size() + " animals.");

        return result;
    }

    // ------------------------------------------------------

    public int countanimalsbytype() {

        LOGGER.info("Counting animals by type.");

        int result = enclosureList.stream()
                .flatMap(enclosure -> enclosure.getinhabitants().stream())
                .map(animal -> animal.getClass().getSimpleName())
                .collect(Collectors.groupingBy(type -> type, Collectors.counting()))
                .size();

        LOGGER.fine("Different animal types: " + result);

        return result;
    }

    // ------------------------------------------------------

    public List<enclosure<? extends Animals>> getovercrowdedenclosures(int a) {

        LOGGER.info("Finding overcrowded enclosures. Limit: " + a);

        List<enclosure<? extends Animals>> result = enclosureList.stream()
                .filter(enclosure -> enclosure.getinhabitants().size() > a)
                .toList();

        LOGGER.fine("Overcrowded enclosures found: " + result.size());

        return result;
    }

    // ------------------------------------------------------

    public String summary() {

        LOGGER.info("Generating zoo summary.");

        String result =
                "Zoo Name: " + name + "\n" +
                        "Number of Enclosures: " + enclosureList.size() + "\n" +
                        "Total Number of Animals: " + getanimals().size() + "\n" +
                        "Number of Mammals: " + getallmammals().size() + "\n" +
                        "Number of Animal Types: " + countanimalsbytype() + "\n";

        LOGGER.fine("Summary generated.");

        return result;
    }
}