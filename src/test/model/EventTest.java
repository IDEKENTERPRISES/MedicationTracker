package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Event class
 */
public class EventTest {
	private Event testEvent;
	private Date d;
	
	//NOTE: these tests might fail if time at which line (2) below is executed
	//is different from time that line (1) is executed.  Lines (1) and (2) must
	//run in same millisecond for this test to make sense and pass.
	
	@BeforeEach
	public void runBefore() {
		testEvent = new Event("Event was added.");   // (1)
		d = Calendar.getInstance().getTime();   // (2)
	}

    @Test
    public void testEquals() {
        Event newEvent = new Event("Event was added 2.");
        assertNotEquals(newEvent.hashCode(), testEvent.hashCode());
        assertNotEquals(null, testEvent);
        assertNotEquals("Event was added.", testEvent);
        assertNotEquals(testEvent, newEvent);
        assertEquals(testEvent, testEvent);
    }
	@Test
	public void testEvent() {
		assertEquals("Event was added.", testEvent.getDescription());
		assertEquals(d.toString(), testEvent.getDate().toString());
	}

	@Test
	public void testToString() {
		assertEquals(d.toString() + "\n" + "Event was added.", testEvent.toString());
	}
}
