package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class MedicationTrackerTest {
    private MedicationTracker initTracker;
    private Drug exampleDrug1;
    private Drug exampleDrug2;

    @BeforeEach
    public void init(){
        initTracker = new MedicationTracker(true);

        exampleDrug1 = new Drug("Frank's cough syrup",
                "Cough syrup devised to ...",
                LocalTime.now().plusMinutes(20),
                100,
                1000);
        exampleDrug1.addIngredient("Paracetamol");
        exampleDrug2 = new Drug("Steve's constipation syrup",
                "Constipation syrup devised to ...",
                LocalTime.now().plusMinutes(10),
                75,
                600);
        exampleDrug2.addIngredient("Ibuprofen");
        initTracker.addDrug(exampleDrug1);
        initTracker.addDrug(exampleDrug2);
    }

    @Test
    public void drugsAddedTest() {
        assertEquals(exampleDrug1, initTracker.getMedicationList().get(0));
        assertEquals(exampleDrug2, initTracker.getMedicationList().get(1));
    }

    @Test
    public void systemChangeTest() {
        assertTrue(initTracker.getSystem());
        assertFalse(initTracker.toggleSystem());
        assertFalse(initTracker.getSystem());
    }

    @Test
    public void conflictingIngredientsTest() {
        Drug newDrugParacetamol = new Drug("test",
                "test",
                LocalTime.of(9,20),
                10, 100);
        newDrugParacetamol.addIngredient("Paracetamol");
        Drug newDrugRandom = new Drug("test",
                "test",
                LocalTime.of(9,20),
                10, 100);
        newDrugRandom.addIngredient("Random");
        assertTrue(initTracker.addDrug(newDrugParacetamol));
        assertFalse(initTracker.addDrug(newDrugRandom));
    }

    @Test
    public void drugRemovalTest() {
        initTracker.removeDrug(2);
        assertEquals(exampleDrug1, initTracker.getMedicationList().get(0));
        assertEquals(exampleDrug2, initTracker.getMedicationList().get(1));

        initTracker.removeDrug(-1);
        assertEquals(exampleDrug1, initTracker.getMedicationList().get(0));
        assertEquals(exampleDrug2, initTracker.getMedicationList().get(1));

        initTracker.removeDrug(1);
        assertEquals(exampleDrug1, initTracker.getMedicationList().get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> initTracker.getMedicationList().get(1));
    }

    @Test
    public void nextScheduledDrugTest() {
        assertEquals(exampleDrug2, initTracker.getNextDrug());

        Drug newDrugRandom = new Drug("test",
                "test",
                LocalTime.now().plusMinutes(1),
                10, 100);

        initTracker.addDrug(newDrugRandom);

        assertEquals(newDrugRandom, initTracker.getNextDrug());
    }


}