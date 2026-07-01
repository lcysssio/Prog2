package org.zoo;

public class AddAnimalCommand<A extends Animals> implements Command<enclosure<? super A>> {

    private final A animal;
    private boolean executed = false;

    public AddAnimalCommand(A animal) {
        this.animal = animal;
    }


    public void execute(enclosure<? super A> ziel) {
        boolean added = ((enclosure<A>) ziel).addlist(animal);
        if (!added) {
            System.out.println("Could not add animal '" + animal + "' to enclosure – already present.");
        } else {
            executed = true;
        }
    }


    public void undo(enclosure<? super A> ziel) {
        if (!executed) {
            System.out.println("Undo not possible: command has not been executed yet.");
            return;
        }
        boolean removed = ((enclosure<A>) ziel).removeList(animal);
        if (!removed) {
            System.out.println("Could not remove animal '" + animal + "' during undo.");
        } else {
            executed = false;
        }
    }


    public String description() {
        return "Add animal '" + animal + "' to enclosure";
    }
}