package edu.eci.dosw.tdd.skyrescue.center;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.eci.dosw.tdd.skyrescue.drone.Drone;
import edu.eci.dosw.tdd.skyrescue.mission.Mission;
import edu.eci.dosw.tdd.skyrescue.mission.MissionStatus;
import edu.eci.dosw.tdd.skyrescue.operator.RescueOperator;

/**
 * TDD tests for RescueCenter.
 */

class RescueCenterTest {

    private RescueCenter center;
    private RescueOperator operator;

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
    }

    // -------------------------------------------------------------------------
    // assignMission 
    // -------------------------------------------------------------------------

    /**
     * Case: Valid operator and drone, allowed distance.
     * Expected result: the mission is created with ACTIVE status
     * and the drone becomes unavailable.
     */
    @Test
    void shouldAssignMissionWhenDataIsValid() {
        Drone drone = new Drone("DR-1", "Falcon", 50);
        center.addDrone(drone);

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
            // If we get here, the method threw the correct exception.
            // Nothing else to do: the test passes.
        }
    }
}