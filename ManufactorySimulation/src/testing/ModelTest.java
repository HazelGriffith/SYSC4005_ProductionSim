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
        int[] args = {1234,1234,1234,1234,1234,1234};
        model = new Model(args, 720);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getRandomTime() {
        Component component1 = new Component(1, Component.serviceType.INSPECTOR);
        Component component2 = new Component(2, Component.serviceType.INSPECTOR);
        Component component3 = new Component(3, Component.serviceType.INSPECTOR);
        assertEquals(13.82613, model.getRandomTime(Event.eventLocation.I1, component1),0.05);
        assertEquals(20.73822, model.getRandomTime(Event.eventLocation.I2, component2),0.05);
        assertEquals(27.53986, model.getRandomTime(Event.eventLocation.I2, component3),0.05);
        assertEquals(6.145856, model.getRandomTime(Event.eventLocation.W1, component1),0.05);
        assertEquals(14.80615, model.getRandomTime(Event.eventLocation.W2, component2),0.05);
        assertEquals(11.74011, model.getRandomTime(Event.eventLocation.W3, component3),0.05);
    }

    @Test
    public void getFinalStats() {
        model.setTotalBlockedTimeI1(10.0);
        model.setTotalBlockedTimeI2(720.0);
        model.getFinalStats();
        assertEquals(0.0138, model.getBlockedProportionI1(), 0.005);
        assertEquals(1, model.getBlockedProportionI2(), 0.005);
    }

    @Test
    public void scheduleEvent() {
        assertEquals(0, model.getFEL().size());
        Event event = new Event(Event.eventType.FI, 2,new Component(1, Component.serviceType.INSPECTOR),
                Event.eventLocation.I1);
        Component component = new Component(1, Component.serviceType.INSPECTOR);
        model.scheduleEvent(event.geteType(), component, event.getLocation());
        assertEquals(1, model.getFEL().size());
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
        Event event = new Event(Event.eventType.FI, 2.0,new Component(1, Component.serviceType.INSPECTOR),
                Event.eventLocation.I1);
        model.processFIEvent(event);
        assertEquals(0, model.getBufferC1W1().size());
        //FI and EA event added
        assertEquals(2, model.getFEL().size());
        Event event2 = new Event(Event.eventType.FI, 2.0,new Component(3, Component.serviceType.INSPECTOR),
                Event.eventLocation.I2);
        model.processFIEvent(event);
        assertEquals(0, model.getBufferC3W3().size());
        //FI event added
        assertEquals(3, model.getFEL().size());
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
        //I1 blocked
        model.setIsI1Blocked(true);
        model.setBlockedI1Component(new Component(1, Component.serviceType.BLOCKING));
        model.checkToUnblockInspectors();
        assertFalse(model.isIsI1Blocked());
        assertEquals(null, model.getBlockedI1Component());
        assertEquals(0, model.getStartBlockedTimeI1(), 0.05);
        assertEquals(1, model.getBufferC1W1().size());
        //I2 blocked
        model.setIsI2Blocked(true);
        model.setBlockedI2Component(new Component(2, Component.serviceType.BLOCKING));
        model.checkToUnblockInspectors();
        assertFalse(model.isIsI2Blocked());
        assertEquals(null, model.getBlockedI2Component());
        assertEquals(0, model.getStartBlockedTimeI2(), 0.05);
        assertEquals(1, model.getBufferC2W2().size());
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