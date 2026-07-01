package org.zoo;

public record Forelle(String name) implements Fish {
    public String getname() {return name;}
}
