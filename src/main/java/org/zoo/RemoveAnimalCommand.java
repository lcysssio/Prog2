package org.zoo;

public class RemoveAnimalCommand<A extends Animals> implements Command<enclosure<? super A>, Zooerror, A> {
    private final A animal;
    private boolean executed = false;
    public RemoveAnimalCommand(A animal) {
        this.animal = animal;
    }

    public Results<Zooerror, A> execute(enclosure<? super A> enclosure) {
        executed = ((enclosure<A>) enclosure).removeList(animal);
        if (!executed) {
            return new Results.Error(Zooerror.Animal_is_not_in_enclosure);
        } else  {
            return new Results.Result(animal);
        }

    }

    public Results<Zooerror, A> undo(enclosure<? super A> enclosure) {
        if (!executed) {

            return new Results.Error(Zooerror.Commmand_has_not_been_executed_yet);
        }
        boolean added = ((enclosure<A>) enclosure).addlist(animal);
        if (!added) {
            return new Results.Error<>(Zooerror.couldnt_add_animal_during_undo);
        } else {
            executed = false;
            return new Results.Result(animal);
        }
    }
    public String description() {
        return "Remove animal '" + animal + "' from enclosure";
    }








}
