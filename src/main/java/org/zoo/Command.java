package org.zoo;

public interface Command<T> {

    void execute(T ziel ) ;
    void undo(T ziel) ;
    String description();
}
