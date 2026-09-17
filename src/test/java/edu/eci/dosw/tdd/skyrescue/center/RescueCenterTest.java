package edu.eci.dosw.tdd.skyrescue.center;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    // completeMission 
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
}