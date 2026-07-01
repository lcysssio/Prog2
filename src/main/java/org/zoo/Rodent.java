package org.zoo;

public record Rodent(String name) implements Mammal{
    public String getname() {return name;}
}
