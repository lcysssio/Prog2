package org.zoo;

public record Kroko(String name) implements Reptille {
    public String getname() {return name;}
}
