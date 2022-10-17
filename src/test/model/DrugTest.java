package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class DrugTest {

    Drug initDrug;

    @BeforeEach
    public void init() {
        initDrug = new Drug("Test Drug",
                "This is a description.",
                LocalTime.of(10,30),
                100,
                1000);
    }

    @Test
    public void simpleChangesTest() {
        initDrug.changeName("New Name");
        assertEquals("New Name", initDrug.getName());
        initDrug.changeDesc("New Description");
        assertEquals("New Description", initDrug.getDesc());
    }

    @Test
    public void properFrequencyChangeTest() {
        initDrug.addDosageFreq(LocalTime.of(16,30));
        assertEquals(LocalTime.of(16,30), initDrug.getDoseTimes().get(1));
        initDrug.removeDosageFreq(LocalTime.of(10,30));
        assertEquals(1, initDrug.getDoseTimes().size());
    }

    @Test
    public void improperFrequencyChangeTest() {
        initDrug.addDosageFreq(LocalTime.of(10,30));
        assertEquals(1, initDrug.getDoseTimes().size());
        initDrug.removeDosageFreq(LocalTime.of(5,15));
        assertEquals(1, initDrug.getDoseTimes().size());
    }

    @Test
    public void amountTest() {
        initDrug.increaseAmountLeft(100);
        assertEquals(1100, initDrug.getAmountLeft());
        assertFalse(initDrug.decreaseAmountLeft(200));
        assertEquals(900, initDrug.getAmountLeft());
        assertFalse(initDrug.decreaseAmountLeft());
        assertEquals(800, initDrug.getAmountLeft());
        assertFalse(initDrug.decreaseAmountLeft(750));
        assertEquals(50, initDrug.getAmountLeft());
        assertTrue(initDrug.decreaseAmountLeft());
        assertEquals(0, initDrug.getAmountLeft());
        initDrug.increaseAmountLeft(50);
        assertTrue(initDrug.decreaseAmountLeft(60));
        assertEquals(0, initDrug.getAmountLeft());
        assertFalse(initDrug.decreaseAmountLeft(-100));
        assertFalse(initDrug.increaseAmountLeft(0));
    }

    @Test
    public void dosageTest(){
        assertTrue(initDrug.changeDosage(10));
        assertEquals(10, initDrug.getDosage());
        assertFalse(initDrug.changeDosage(-100));
    }

    @Test
    public void ingredientsTest(){
        initDrug.addIngredient("Test Ingredient");
        assertEquals("Test Ingredient", initDrug.getIngredients().get(0));
        initDrug.removeIngredient("Test Ingredient");
        assertEquals(0, initDrug.getIngredients().size());
    }
}
