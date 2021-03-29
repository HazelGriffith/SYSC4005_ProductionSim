package testing;

import base.Component;
import base.Event;
import base.Model;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ModelTest {
    private Model model;
    @Before
    public void setUp() throws Exception {
        model = new Model();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getRandomTime() {
        assertEquals(0, model.getRandomTime(Event.eventLocation.I1));
        assertEquals(0, model.getRandomTime(Event.eventLocation.I2));
        assertEquals(0, model.getRandomTime(Event.eventLocation.W1));
        assertEquals(0, model.getRandomTime(Event.eventLocation.W2));
        assertEquals(0, model.getRandomTime(Event.eventLocation.W3));
    }

    @Test
    public void getBlockedProportions() {
        model.setTotalBlockedTimeI1(10.0);
        model.setTotalBlockedTimeI2(720.0);
        model.getBlockedProportions();
        assertEquals(0.0138, model.getBlockedProportionI1(), 0.005);
        assertEquals(1, model.getBlockedProportionI2(), 0.005);
    }

    @Test
    public void scheduleEvent() {
        assertEquals(2, model.getFEL().size());
        Event event = new Event(Event.eventType.FI, 2,new Component(1, Component.serviceType.INSPECTOR),
                Event.eventLocation.I1);
        Component component = new Component(1, Component.serviceType.INSPECTOR);
        model.scheduleEvent(event.geteType(), component, event.getLocation());
        assertEquals(3, model.getFEL().size());
    }

    @Test
    public void processEAEvent() {
        //Workstation 1
        Event event = new Event(Event.eventType.EA, 2,new Component(1, Component.serviceType.WORKSTATION),
                Event.eventLocation.W1);
        model.processEAEvent(event);
        assertEquals(1, model.getProductCount());
        assertFalse(model.isIsW1Busy());
        //Workstation 2
        Event event2 = new Event(Event.eventType.EA, 2,new Component(1, Component.serviceType.WORKSTATION),
                Event.eventLocation.W2);
        model.processEAEvent(event2);
        assertEquals(2, model.getProductCount());
        assertFalse(model.isIsW2Busy());
        //Workstation 3
        Event event3 = new Event(Event.eventType.EA, 2,new Component(1, Component.serviceType.WORKSTATION),
                Event.eventLocation.W3);
        model.processEAEvent(event3);
        assertEquals(3, model.getProductCount());
        assertFalse(model.isIsW3Busy());
    }

    @Test
    public void processFIEvent() {
    }

    @Test
    public void selectC1Buffer() {
        Component component = new Component(1, Component.serviceType.INSPECTOR);
        Component component2 = new Component(1, Component.serviceType.INSPECTOR);
        Component component3 = new Component(1, Component.serviceType.INSPECTOR);
        model.selectC1Buffer(component);
        assertEquals(1, model.getBufferC1W1().size());
        model.selectC1Buffer(component2);
        assertEquals(1, model.getBufferC1W2().size());
        model.selectC1Buffer(component3);
        assertEquals(1, model.getBufferC1W3().size());
    }

    @Test
    public void checkToUnblockInspectors() {
    }

    @Test
    public void checkToScheduleEAEvent() {
    }

    @Test
    public void addToBuffer() {
        Component component1 = new Component(1, Component.serviceType.INSPECTOR);
        Component component2 = new Component(2, Component.serviceType.INSPECTOR);
        Component component3 = new Component(3, Component.serviceType.INSPECTOR);
        assertTrue(model.addToBuffer(Model.bufferType.BC1W1, component1));
        assertEquals(1,model.getBufferC1W1().size());
        assertTrue(model.addToBuffer(Model.bufferType.BC1W1, component1));
        assertEquals(2,model.getBufferC1W1().size());
        assertFalse(model.addToBuffer(Model.bufferType.BC1W1, component1));
        assertEquals(2,model.getBufferC1W1().size());
        assertTrue(model.addToBuffer(Model.bufferType.BC1W2, component1));
        assertEquals(1,model.getBufferC1W2().size());
        assertTrue(model.addToBuffer(Model.bufferType.BC1W3, component1));
        assertEquals(1,model.getBufferC1W3().size());
        assertTrue(model.addToBuffer(Model.bufferType.BC2W2, component2));
        assertEquals(1,model.getBufferC2W2().size());
        assertTrue(model.addToBuffer(Model.bufferType.BC3W3, component3));
        assertEquals(1,model.getBufferC3W3().size());
    }

}