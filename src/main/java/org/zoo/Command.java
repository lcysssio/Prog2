package org.zoo;

public interface Command<T, E, R> {

    Results<E,R> execute(T ziel ) ;
    Results<E,R> undo(T ziel) ;
    String description();
}
