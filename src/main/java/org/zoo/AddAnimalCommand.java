package org.zoo;

public class AddAnimalCommand<A extends Animals> implements Command<enclosure<? super A>, Zooerror, A> {

    private final A animal;
    private boolean executed = false;

    public AddAnimalCommand(A animal) {
        this.animal = animal;
    }


    public Results<Zooerror, A> execute(enclosure<? super A> ziel) {
        boolean added = ((enclosure<A>) ziel).addlist(animal);
        if (!added) {
            return new Results.Error<>(Zooerror.Animal_already_exists);
        } else {
            executed = true;
            return new Results.Result<>(animal);
        }
    }


    public Results<Zooerror, A> undo(enclosure<? super A> ziel) {
        if (!executed) {
            return new Results.Error<>(Zooerror.Commmand_has_not_been_executed_yet);
        }
        boolean removed = ((enclosure<A>) ziel).removeList(animal);
        if (!removed) {
            return new Results.Error<>(Zooerror.couldnt_remove_animal_during_undo);
        } else {
            executed = false;
            return new Results.Result<>(animal);
        }
    }


    public String description() {
        return "Add animal '" + animal + "' to enclosure";
    }
}