package base;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EventTest {

    private Event e;
    @Before
    public void setUp() throws Exception {
        e = new Event(Event.eventType.FI, 2,new Component(1, Component.serviceType.INSPECTOR),
                Event.eventLocation.I1);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void geteType() {
        assertEquals(Event.eventType.FI, e.geteType());
    }

    @Test
    public void seteType() {
        e.seteType(Event.eventType.EA);
        assertEquals(Event.eventType.EA, e.geteType());
    }

    @Test
    public void getTime() {
        assertEquals(2, e.getTime());
    }

    @Test
    public void setTime() {
        e.setTime(4);
        assertEquals(4, e.getTime());
    }

    @Test
    public void getC() {
        assertEquals(1, e.getC().getId());
        assertEquals(Component.serviceType.INSPECTOR, e.getC().getWhichService());
    }

    @Test
    public void setC() {
        Component newC = new Component(3, Component.serviceType.BUFFER);
        e.setC(newC);
        assertEquals(3, e.getC().getId());
        assertEquals(Component.serviceType.BUFFER, e.getC().getWhichService());
    }

    @Test
    public void getLocation() {
        assertEquals(Event.eventLocation.I1, e.getLocation());
    }

    @Test
    public void setLocation() {
        e.setLocation(Event.eventLocation.W1);
        assertEquals(Event.eventLocation.W1, e.getLocation());
    }
}