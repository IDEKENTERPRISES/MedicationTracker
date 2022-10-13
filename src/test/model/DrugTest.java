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
        assertEquals(initDrug.getName(), "New Name");
        initDrug.changeDesc("New Description");
        assertEquals(initDrug.getDesc(), "New Description");
    }

    @Test
    public void properFrequencyChangeTest() {
        initDrug.addDosageFreq(LocalTime.of(16,30));
        assertEquals(initDrug.getDoseTimes().get(1), LocalTime.of(16,30));
        initDrug.removeDosageFreq(LocalTime.of(10,30));
        assertEquals(initDrug.getDoseTimes().size(), 1);
    }

    @Test
    public void improperFrequencyChangeTest() {
        initDrug.addDosageFreq(LocalTime.of(10,30));
        assertEquals(initDrug.getDoseTimes().size(), 1);
        initDrug.removeDosageFreq(LocalTime.of(5,15));
        assertEquals(initDrug.getDoseTimes().size(), 1);
    }

    @Test
    public void amountTest() {
        initDrug.increaseAmountLeft(100);
        assertEquals(initDrug.getAmountLeft(), 1100);
        assertFalse(initDrug.decreaseAmountLeft(200));
        assertEquals(initDrug.getAmountLeft(), 900);
        assertFalse(initDrug.decreaseAmountLeft());
        assertEquals(initDrug.getAmountLeft(), 800);
        assertFalse(initDrug.decreaseAmountLeft(750));
        assertEquals(initDrug.getAmountLeft(), 50);
        assertTrue(initDrug.decreaseAmountLeft());
        assertEquals(initDrug.getAmountLeft(), 0);
        initDrug.increaseAmountLeft(50);
        assertTrue(initDrug.decreaseAmountLeft(60));
        assertEquals(initDrug.getAmountLeft(), 0);
        assertFalse(initDrug.decreaseAmountLeft(-100));
    }

    @Test
    public void dosageTest(){
        assertTrue(initDrug.changeDosage(10));
        assertEquals(initDrug.getDosage(), 10);
        assertFalse(initDrug.changeDosage(-100));
    }

    @Test
    public void ingredientsTest(){
        initDrug.addIngredient("Test Ingredient");
        assertEquals(initDrug.getIngredients().get(0), "Test Ingredient");
    }
}
