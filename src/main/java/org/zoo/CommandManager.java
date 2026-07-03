package org.zoo;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Logger;

public class CommandManager<T extends enclosure, E , R> {
    private boolean executed = false;
    Deque<Command<T,Zooerror,R>> Undostack = new ArrayDeque<>();
    Deque<Command<T,Zooerror,R>> Redostack = new ArrayDeque<>();
    Logger LOGGER = Logger.getLogger(CommandManager.class.getName());


    Results<Zooerror,R> executeCommand(Command<T,Zooerror, R> command, T ziel) {
        if (!executed) {
            Results<Zooerror,R> f = command.execute(ziel);
            Undostack.push(command);
            executed = true;

            Redostack.clear();
            switch (f) {
                case Results.Result<Zooerror,R> result -> LOGGER.info("Command executed successfully: " + command.description());
                case Results.Error<Zooerror,R > error -> LOGGER.warning("Command execution failed: " + error.error());
            }

            return f;

        } else {
            return new Results.Error<>(Zooerror.Commmand_has_already_been_executed);

        }


    }
    Results<Zooerror, R> undoCommand(Command<T,Zooerror,R> command, T ziel) {
        if (!Undostack.isEmpty()) {
            Command<T,Zooerror,R> letzte = Undostack.pop();
            Results<Zooerror,R> f = letzte.undo(ziel);
            Redostack.push(letzte);
            executed = false;
            switch (f) {
                case Results.Result<Zooerror,R> result -> LOGGER.info("Command undone successfully: " + command.description());
                case Results.Error<Zooerror,R > error -> LOGGER.warning("Command undo failed: " + error.error());
            }
            return f;
        } else {

            return new Results.Error<>(Zooerror.Commmand_has_not_been_executed_yet);

        }

    }
    Results<Zooerror, R> redoCommand(Command<T,Zooerror,R> command, T ziel) {
        if (!Redostack.isEmpty()) {
            Command<T,Zooerror,R> letzte = Redostack.pop();
            Results<Zooerror,R> f = letzte.execute(ziel);
            Undostack.push(letzte);
            executed = true;
            switch (f) {
                case Results.Result<Zooerror,R> result -> LOGGER.info("Command redone successfully: " + command.description());
                case Results.Error<Zooerror,R > error -> LOGGER.warning("Command redo failed: " + error.error());
            }
            return f;
        } else {
            return new Results.Error<>(Zooerror.Commmand_has_not_been_executed_yet);

    }
}}

