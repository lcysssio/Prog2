package org.zoo;

public record Cat(String name) implements Mammal{
    public String getname() {return name;}
}
