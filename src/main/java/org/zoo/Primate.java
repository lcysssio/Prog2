package org.zoo;

public record Primate(String name) implements Mammal {
    public String getname() {return name;}
}
