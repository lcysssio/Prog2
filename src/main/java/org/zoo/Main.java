package org.zoo;


import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    static void main() {
        Logger logger = Logger.getLogger(Zoo.class.getName());
        logger.setLevel(Level.FINE);

        ConsoleHandler ch = new ConsoleHandler();
        ch.setLevel(Level.FINE);

        logger.setUseParentHandlers(false);
        logger.addHandler(ch);

        Zoo zoo = new Zoo("Petersburg");




        // Gehege
        enclosure<Mammal> mammalHouse = new Mammalhouse("Säugetier-Haus");
        Cathouse catHouse = new Cathouse("Katzen-Haus");

// Kommando-Manager für die Gehege
        CommandManager<enclosure<Mammal>> mammalManager = new CommandManager<>();
        CommandManager<enclosure<Cat>> catManager = new CommandManager<>();

// Typ-sichere Commands: hier nur Mammal bzw. Lion erlaubt
        AddAnimalCommand<Cat> mieze = new AddAnimalCommand<>(new Cat("Mieze"));
        AddAnimalCommand<Echse> kiki = new AddAnimalCommand<>(new Echse("Kiki"));
        RemoveAnimalCommand<Cat> leon = new RemoveAnimalCommand<>(new Cat("Leon"));

        mammalManager.executeCommand(mieze, mammalHouse);
        mammalManager.executeCommand(kiki, mammalHouse);
        mammalManager.executeCommand(leon, mammalHouse);

// Spezielles Katzengelände
        AddAnimalCommand<Cat> felix = new AddAnimalCommand<>(new Cat("Felix"));
        catManager.executeCommand(felix, catHouse);

// Undo/Redo
        mammalManager.undo(mammalHouse);
        mammalManager.redo(mammalHouse);

// Das folgende executeCommand() sollte vom Compiler zurückgewiesen werden!
        AddAnimalCommand<Forelle> nemo = new AddAnimalCommand<>(new Forelle("Nemo"));
        mammalManager.executeCommand(nemo, mammalHouse);  // NOPE!!!

    }




        }


