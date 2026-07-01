package org.zoo;

import java.util.ArrayDeque;
import java.util.Deque;

public class CommandManager<T extends enclosure> {
    private boolean executed = false;
    Deque<Command<T>> Undostack = new ArrayDeque<>();
    Deque<Command<T>> Redostack = new ArrayDeque<>();


    void executeCommand(Command<T> command, T ziel) {
        if (!executed) {
            command.execute(ziel);
            Undostack.push(command);
            executed = true;

            Redostack.clear();
        } else {
            System.out.println("Command has already been executed. Please undo before executing again.");
        }

    }
    void undoCommand(Command<T> command, T ziel) {
        if (!Undostack.isEmpty()) {
            Command<T> letzte = Undostack.pop();
            letzte.undo(ziel);
            Redostack.push(letzte);
            executed = false;
        } else {
            System.out.println("No command to undo.");

        }

    }
    void redoCommand(Command<T> command, T ziel) {
        if (!Redostack.isEmpty()) {
            Command<T> letzte = Redostack.pop();
            command.execute(ziel);
            Undostack.push(letzte);
            executed = true;
        } else {
            System.out.println("No command to redo.");

    }
}}

