package org.zoo;

sealed interface Mammal extends Animals permits Primate, Rodent, Cat {
}
