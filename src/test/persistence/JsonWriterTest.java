package persistence;
// Code has been restructured from given material in JsonSerializationDemo to work with this project.

import model.Drug;
import model.MedicationTracker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest {

    static MedicationTracker tracker;
    static Drug exampleDrug;

    @BeforeAll
    static void init() {
        tracker = new MedicationTracker();
        exampleDrug = new Drug("Test Drug",
                "This is a description.",
                LocalTime.of(10,30),
                100,
                1000);

        tracker.addDrug(exampleDrug);
    }

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("invalid\0filename");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void writeGeneral() {
        try {
            JsonWriter writer = new JsonWriter("writing-general-test");
            writer.open();
            writer.write(tracker);
            writer.close();

            JsonReader reader = new JsonReader("writing-general-test");
            tracker = reader.read();
            assertEquals(exampleDrug.getName(), tracker.getMedicationList().get(0).getName());
            assertEquals(1, tracker.getMedicationList().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void writeEmpty() {
        try {
            MedicationTracker tempTracker = new MedicationTracker();
            JsonWriter writer = new JsonWriter("writing-empty-test");
            writer.open();
            writer.write(tempTracker);
            writer.close();

            JsonReader reader = new JsonReader("writing-empty-test");
            tempTracker = reader.read();
            assertEquals(0, tempTracker.getMedicationList().size());
            assertNull(tracker.getNextDrug());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
