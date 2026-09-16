package edu.eci.dosw.tdd.skyrescue.center;

import static org.junit.jupiter.api.Assertions.assertTrue;
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

    /**
     * Verifies that a drone with valid data is successfully
     * registered in the RescueCenter.
     */
    @Test
    void shouldRegisterDroneWhenDataIsValid() {
        Drone drone = new Drone("D-1", "Falcon", 50);

        boolean result = center.addDrone(drone);

        assertTrue(result, "A valid drone should be registered successfully");
    }

    /**
     * Verifies that assigning a mission to a valid operator and
     * a registered drone creates a mission with ACTIVE status.
     */
    @Test
    void shouldCreateActiveMissionWhenOperatorAndDroneAreValid() {
        Drone drone = new Drone("D-1", "Falcon", 50);
        center.addDrone(drone);

        Mission mission = center.assignMission(operator.getId(), drone.getId(), "Downtown", 20);

        assertTrue(mission.getStatus() == MissionStatus.ACTIVE, "The created mission should be ACTIVE");
    }

    /**
     * Verifies that completing an active mission changes its
     * status to COMPLETED.
     */
    @Test
    void shouldCompleteMissionWhenMissionIsActive() {
        Drone drone = new Drone("D-1", "Falcon", 50);
        center.addDrone(drone);
        Mission mission = center.assignMission(operator.getId(), drone.getId(), "Downtown", 20);

        Mission completedMission = center.completeMission(mission.getId());

        assertTrue(completedMission.getStatus() == MissionStatus.COMPLETED, "Mission status should be COMPLETED");
    }
}