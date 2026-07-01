package org.zoo;

public record Adler(String name) implements Bird {
    public String getname() {return name;}
}
