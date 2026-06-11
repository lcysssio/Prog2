import org.junit.jupiter.api.Test;
import cyclechronicles.*;
import org.mockito.internal.matchers.Or;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class tests {

//Wüsste nicht wie ich es in einen test packe ohne das pendingorder verfälscht
    // Ich Mocke die klasse Order da dort keine rückgabewerte existieren
        @Test
    public void Singlespeed() {
            Order Singlespeed = mock(Order.class);
            when(Singlespeed.getBicycleType()).thenReturn(Type.SINGLE_SPEED);
            Shop Test = new Shop();
            assertTrue(Test.accept(Singlespeed));
    }
    @Test
    public void Fixie(){
        Order Fixie = mock(Order.class);
        when(Fixie.getBicycleType()).thenReturn(Type.FIXIE);
        Shop Test = new Shop();
        assertTrue(Test.accept(Fixie));
    }
    @Test
    public void Race(){
        Order Race = mock(Order.class);
        when(Race.getBicycleType()).thenReturn(Type.RACE);
        Shop Test = new Shop();
        assertTrue(Test.accept(Race));
    }
    @Test
    public void Gravel(){
        Order Gravel = mock(Order.class);
        when(Gravel.getBicycleType()).thenReturn(Type.GRAVEL);
        Shop Test = new Shop();
        assertFalse(Test.accept(Gravel));
    }
    @Test
    public void Ebike(){
        Order Ebike = mock(Order.class);
        when(Ebike.getBicycleType()).thenReturn(Type.EBIKE);
        Shop Test = new Shop();
        assertFalse(Test.accept(Ebike));
    }
   ////////////////////////////////////////////////////////////////////////////////
    @Test
    public void Samecustomermultipleorders(){
            Order Customer = mock(Order.class);
            when(Customer.getCustomer()).thenReturn("Max Mustermann");
        Shop Test = new Shop();
        Test.accept(Customer);
        assertFalse(Test.accept(Customer));
    }
    @Test
    public void alreadyhas4orders(){
            Shop Test = new Shop();
            Order Customer1 = mock(Order.class);
            when(Customer1.getCustomer()).thenReturn("Max Mustermann");
            Order Customer2 = mock(Order.class);
            when(Customer2.getCustomer()).thenReturn("Max Mustermann2");
            Order Customer3 = mock(Order.class);
            when(Customer3.getCustomer()).thenReturn("Max Mustermann3");
            Order Customer4 = mock(Order.class);
            when(Customer4.getCustomer()).thenReturn("Max Mustermann4");
            Order Customer5 = mock(Order.class);
            when(Customer5.getCustomer()).thenReturn("Max Mustermann5");
            Order Customer6 = mock(Order.class);
            when(Customer6.getCustomer()).thenReturn("Max Mustermann6");
            Test.accept(Customer1);
            Test.accept(Customer2);
            Test.accept(Customer3);
            Test.accept(Customer4);
            Test.accept(Customer5);
        assertFalse(Test.accept(Customer6));
    }




}
