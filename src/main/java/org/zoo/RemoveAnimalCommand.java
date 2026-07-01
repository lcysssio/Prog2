package org.zoo;

public class RemoveAnimalCommand<A extends Animals> implements Command<enclosure<? super A>> {
    private final A animal;
    private boolean executed = false;
    public RemoveAnimalCommand(A animal) {
        this.animal = animal;
    }

    public void execute(enclosure<? super A> enclosure) {
        executed = ((enclosure<A>) enclosure).removeList(animal);
        if (!executed) {
            System.out.println("Animal '" + animal + "' has not been removed");
        } else  {
            System.out.println("Animal '" + animal + "' has been removed");
        }
    }

    public void undo(enclosure<? super A> enclosure) {
        if (!executed) {
            System.out.println("Undo not possible: command has not been executed yet.");
            return;
        }
        boolean added = ((enclosure<A>) enclosure).addlist(animal);
        if (!added) {
            System.out.println("Could not add animal '" + animal + "' during undo.");
        } else {
            executed = false;
        }
    }
    public String description() {
        return "Remove animal '" + animal + "' from enclosure";
    }








}
