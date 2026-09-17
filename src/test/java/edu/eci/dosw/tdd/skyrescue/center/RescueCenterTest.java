package edu.eci.dosw.tdd.skyrescue.center;

import org.junit.jupiter.api.BeforeEach;

import edu.eci.dosw.tdd.skyrescue.operator.RescueOperator;
import edu.eci.dosw.tdd.skyrescue.drone.Drone;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
    
    @Test
    void shouldRegisterDroneWhenDataIsValid() {
        Drone drone = new Drone("D1", "Falcon-X", 20);

        boolean result = center.addDrone(drone);

        assertTrue(result);
    }
}