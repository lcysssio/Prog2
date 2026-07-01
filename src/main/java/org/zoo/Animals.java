package org.zoo;

sealed interface Animals
    permits Mammal, Bird, Reptille, Fish {
String getname();
    }

