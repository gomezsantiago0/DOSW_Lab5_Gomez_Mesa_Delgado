package edu.eci.dosw.tdd.skyrescue.center;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.eci.dosw.tdd.skyrescue.drone.Drone;
import edu.eci.dosw.tdd.skyrescue.mission.Mission;
import edu.eci.dosw.tdd.skyrescue.mission.MissionStatus;
import edu.eci.dosw.tdd.skyrescue.operator.RescueOperator;

import java.time.LocalDateTime;

/**
 * TDD tests for RescueCenter.
 */
class RescueCenterTest {

    private RescueCenter center;
    private RescueOperator operator;
    private Drone drone;
    private Mission activeMission;

    /**
     * Initializes a fresh RescueCenter and a valid RescueOperator
     * before each test, and registers the operator in the center
     * so every test starts from a clean, consistent state.
     */
    @BeforeEach
    void setUp() {
        center = new RescueCenter();
        operator = new RescueOperator("OP-1", "Alice");
        center.addOperator(operator);

        drone = new Drone("D-1", "ModelX", 100);
        drone.setAvailable(false);

        activeMission = new Mission(
                "M-1",
                "Zona Norte",
                50,
                drone,
                operator,
                LocalDateTime.now(),
                MissionStatus.ACTIVE
        );

        center.addMission(activeMission);
    }

    // -------------------------------------------------------------------------
    // completeMission - Gómez
    // -------------------------------------------------------------------------

    @Test
    void shouldCompleteMissionAndMakeDroneAvailable() {
        Mission result = center.completeMission("M-1");

        assertEquals(MissionStatus.COMPLETED, result.getStatus());
        assertNotNull(result.getEndDate());
        assertTrue(drone.isAvailable());
    }

    @Test
    void shouldThrowExceptionWhenMissionIdDoesNotExist() {
        assertThrows(IllegalArgumentException.class,
                () -> center.completeMission("INEXISTENTE"));
    }

    @Test
    void shouldThrowExceptionWhenMissionIsAlreadyCompleted() {
        center.completeMission("M-1");

        assertThrows(IllegalStateException.class,
                () -> center.completeMission("M-1"));
    }

    @Test
    void shouldNotAffectOtherActiveMissionWhenOneIsCompleted() {
        Drone drone2 = new Drone("D-2", "ModelY", 80);
        drone2.setAvailable(false);
        RescueOperator operator2 = new RescueOperator("OP-2", "Bob");
        center.addOperator(operator2);

        Mission otherMission = new Mission(
                "M-2",
                "Zona Sur",
                30,
                drone2,
                operator2,
                LocalDateTime.now(),
                MissionStatus.ACTIVE
        );
        center.addMission(otherMission);

        center.completeMission("M-1");

        assertEquals(MissionStatus.ACTIVE, otherMission.getStatus());
        assertTrue(!drone2.isAvailable());
    }

    // -------------------------------------------------------------------------
    // assignMission - Delgado
    // -------------------------------------------------------------------------

    /**
     * Creates a drone and registers it in the center using addDrone.
     *
     * @return a registered, available drone with a 50 km max range.
     */
    private Drone registerAvailableDrone() {
        Drone d = new Drone("DR-1", "Falcon", 50);
        center.addDrone(d);
        return d;
    }

    /**
     * Case: Valid operator and drone, allowed distance.
     * Expected result: the mission is created with ACTIVE status
     * and the drone becomes unavailable.
     */
    @Test
    void shouldAssignMissionWhenDataIsValid() {
        Drone drone = registerAvailableDrone();

        Mission mission = center.assignMission(operator.getId(), drone.getId(), "Downtown", 20);

        assertEquals(MissionStatus.ACTIVE, mission.getStatus());
        assertFalse(drone.isAvailable());
    }

    /**
     * Case: Nonexistent drone.
     * Expected result: IllegalArgumentException.
     */
    @Test
    void shouldThrowIllegalArgumentExceptionWhenDroneDoesNotExist() {
        try {
            center.assignMission(operator.getId(), "DR-NON-EXISTENT", "Downtown", 20);
            fail("An IllegalArgumentException was expected");
        } catch (IllegalArgumentException e) {
            // Expected exception, the test passes.
        }
    }

    /**
     * Case: Drone already busy.
     * Expected result: IllegalStateException.
     */
    @Test
    void shouldThrowIllegalStateExceptionWhenDroneIsAlreadyBusy() {
        Drone drone = registerAvailableDrone();
        drone.setAvailable(false);

        try {
            center.assignMission(operator.getId(), drone.getId(), "Downtown", 20);
            fail("An IllegalStateException was expected");
        } catch (IllegalStateException e) {
            // Expected exception, the test passes.
        }
    }

    /**
     * Case: Distance greater than the drone's max range.
     * Expected result: IllegalArgumentException.
     */
    @Test
    void shouldThrowIllegalArgumentExceptionWhenDistanceExceedsMaxRange() {
        Drone drone = registerAvailableDrone();

        try {
            center.assignMission(operator.getId(), drone.getId(), "Downtown", 100);
            fail("An IllegalArgumentException was expected");
        } catch (IllegalArgumentException e) {
            // Expected exception, the test passes.
        }
    }

    // -------------------------------------------------------------------------
    // addDrone - Mesa
    // -------------------------------------------------------------------------

    @Test
    void shouldRegisterDroneWhenDataIsValid() {
        Drone drone = new Drone("D1", "Falcon-X", 20);

        boolean result = center.addDrone(drone);

        assertTrue(result);
    }

    @Test
    void shouldNotRegisterDroneWhenDroneIsNull() {
        boolean result = center.addDrone(null);

        assertFalse(result);
    }

    @Test
    void shouldNotRegisterDroneWhenIdIsBlank() {
        Drone drone = new Drone("", "Falcon-X", 20);

        boolean result = center.addDrone(drone);

        assertFalse(result);
    }

    @Test
    void shouldNotRegisterSecondDroneWhenIdIsDuplicated() {
        Drone drone1 = new Drone("D1", "Falcon-X", 20);
        Drone drone2 = new Drone("D1", "Falcon-Y", 30);

        center.addDrone(drone1);
        boolean result = center.addDrone(drone2);

        assertFalse(result);
    }

    @Test
    void shouldRegisterDroneAsAvailableByDefault() {
        Drone drone = new Drone("D2", "Falcon-Z", 15);
        center.addDrone(drone);
        assertTrue(drone.isAvailable());
    }
}