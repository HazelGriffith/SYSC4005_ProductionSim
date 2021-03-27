package testing;

import base.Component;

import static org.junit.Assert.*;

public class ComponentTest {
    private Component component;

    @org.junit.Before
    public void setUp() throws Exception {
        component = new Component(1, Component.serviceType.INSPECTOR);
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @org.junit.Test
    public void getId() {
        assertEquals(1, component.getId());
    }

    @org.junit.Test
    public void setId() {
        component.setId(2);
        assertEquals(2, component.getId());
    }

    @org.junit.Test
    public void getWhichService() {
        assertEquals(Component.serviceType.INSPECTOR, component.getWhichService());
    }

    @org.junit.Test
    public void setWhichService() {
        component.setWhichService(Component.serviceType.BUFFER);
        assertEquals(Component.serviceType.BUFFER, component.getWhichService());
        component.setWhichService(Component.serviceType.WORKSTATION);
        assertEquals(Component.serviceType.WORKSTATION, component.getWhichService());
        component.setWhichService(Component.serviceType.BLOCKING);
        assertEquals(Component.serviceType.BLOCKING, component.getWhichService());
    }
}