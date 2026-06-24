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



    }




        }


