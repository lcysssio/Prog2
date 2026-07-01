package org.zoo;

public record Echse(String name) implements Reptille {
    public String getname() {return name;}
}
