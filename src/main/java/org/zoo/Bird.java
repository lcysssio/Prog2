package org.zoo;

sealed interface Bird extends Animals
permits Adler, Taube {
}


