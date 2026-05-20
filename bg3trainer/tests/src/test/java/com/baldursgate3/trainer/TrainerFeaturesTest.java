package com.baldursgate3.trainer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Unit tests for TrainerFeatures using a mocked MemoryScanner.
 */
class TrainerFeaturesTest {

    private MemoryScanner mockScanner;
    private TrainerFeatures features;

    @BeforeEach
    void setUp() {
        mockScanner = Mockito.mock(MemoryScanner.class);
        features = new TrainerFeatures(mockScanner);
    }

    @Test
    void testSetHealthValid() {
        when(mockScanner.writeMemory(eq(0x123456L), any(byte[].class))).thenReturn(true);
        assertTrue(features.setHealth(500));
    }

    @Test
    void testSetHealthOutOfRange() {
        assertFalse(features.setHealth(10000));
        assertFalse(features.setHealth(-1));
    }

    @Test
    void testSetGoldValid() {
        when(mockScanner.writeMemory(eq(0x789ABCL), any(byte[].class))).thenReturn(true);
        assertTrue(features.setGold(9999));
    }

    @Test
    void testSetGoldNegative() {
        assertFalse(features.setGold(-100));
    }

    @Test
    void testSetActionPointsValid() {
        when(mockScanner.writeMemory(eq(0xDEF012L), any(byte[].class))).thenReturn(true);
        assertTrue(features.setActionPoints(5));
    }

    @Test
    void testSetActionPointsOutOfRange() {
        assertFalse(features.setActionPoints(11));
        assertFalse(features.setActionPoints(-1));
    }

    @Test
    void testSetHealthFailsWhenWriteFails() {
        when(mockScanner.writeMemory(eq(0x123456L), any(byte[].class))).thenReturn(false);
        assertFalse(features.setHealth(100));
    }

    @Test
    void testSetGoldFailsWhenWriteFails() {
        when(mockScanner.writeMemory(eq(0x789ABCL), any(byte[].class))).thenReturn(false);
        assertFalse(features.setGold(50));
    }
}
