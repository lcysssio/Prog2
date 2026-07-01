package org.zoo;

public record Taube(String name) implements Bird{
    public String getname() {return name;}
}
