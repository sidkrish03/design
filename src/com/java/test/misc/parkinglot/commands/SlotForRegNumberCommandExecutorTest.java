package com.java.test.misc.parkinglot.commands;

import com.java.design.misc.parkinglot.OutputPrinter;
import com.java.design.misc.parkinglot.commands.SlotForRegNumberCommandExecutor;
import com.java.design.misc.parkinglot.model.Car;
import com.java.design.misc.parkinglot.model.Command;
import com.java.design.misc.parkinglot.model.Slot;
import com.java.design.misc.parkinglot.service.ParkingLotService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class SlotForRegNumberCommandExecutorTest {

    private ParkingLotService parkingLotService;
    private OutputPrinter outputPrinter;
    private SlotForRegNumberCommandExecutor slotForRegNumberCommandExecutor;

    @Before
    public void setUp() throws Exception {
        parkingLotService = mock(ParkingLotService.class);
        outputPrinter = mock(OutputPrinter.class);
        slotForRegNumberCommandExecutor =
                new SlotForRegNumberCommandExecutor(parkingLotService, outputPrinter);
    }

    @Test
    public void testValidCommand() {
        assertTrue(
                slotForRegNumberCommandExecutor.validate(
                        new Command("slot_number_for_registration_number AB-01-CP-1230")));
    }

    @Test
    public void testInvalidCommand() {
        assertFalse(
                slotForRegNumberCommandExecutor.validate(
                        new Command("slot_number_for_registration_number")));
        assertFalse(
                slotForRegNumberCommandExecutor.validate(
                        new Command("slot_number_for_registration_number AB-01-CP-1230 b")));
    }

    @Test
    public void testCorrectSlotNumberForValidRegistrationNumber() {
        final Slot slot1 = new Slot(1);
        slot1.assignCar(new Car("reg-1", "white"));

        final Slot slot2 = new Slot(2);
        slot2.assignCar(new Car("AB-01-CP-1230", "blue"));

        final Slot slot3 = new Slot(3);
        slot3.assignCar(new Car("reg-2", "blue"));

        when(parkingLotService.getOccupiedSlots()).thenReturn(Arrays.asList(slot1, slot2, slot3));

        slotForRegNumberCommandExecutor.execute(
                new Command("slot_number_for_registration_number AB-01-CP-1230"));
        verify(outputPrinter).printWithNewLine("2");
    }

    @Test
    public void testCorrectSlotNumberForNonExistingRegistrationNumber() {
        List<Slot> occupiedSlots = new ArrayList<>();
        when(parkingLotService.getOccupiedSlots()).thenReturn(occupiedSlots);

        slotForRegNumberCommandExecutor.execute(
                new Command("slot_number_for_registration_number AB-01-CP-1230"));
        verify(outputPrinter).notFound();
    }
}
