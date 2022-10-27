package persistence;
// Code has been restructured from given material in JsonSerializationDemo to work with this project.

import model.MedicationTracker;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest {

    @Test
    void fakeFileTest() {
        JsonReader jsonReader = new JsonReader("non-existent");
        try {
            MedicationTracker tracker = jsonReader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void emptyListTest() {
        JsonReader reader = new JsonReader("empty");
        try {
            MedicationTracker tracker = reader.read();
            assertEquals(0, tracker.getMedicationList().size());
            assertNull(tracker.getNextDrug());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void generalTest() {
        JsonReader reader = new JsonReader("test");
        try {
            MedicationTracker tracker = reader.read();
            assertEquals(3, tracker.getMedicationList().size());
            assertEquals("Jamie's cough medicine", tracker.getMedicationList().get(0).getName());
            assertEquals(0, tracker.getMedicationList().get(2).getIngredients().size());
            assertEquals(3, tracker.getMedicationList().get(0).getIngredients().size());
            assertEquals("Paracetamol", tracker.getMedicationList().get(0).getIngredients().get(0));

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }


}
