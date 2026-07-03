package org.zoo;

sealed interface Results<E,R> {
    record Error<E,R> (E error) implements Results<E,R> {

    }

    record Result<E,R>(R result) implements Results<E,R> {



    }
}
