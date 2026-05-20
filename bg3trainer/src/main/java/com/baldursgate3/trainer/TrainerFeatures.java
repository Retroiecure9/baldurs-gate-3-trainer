package com.baldursgate3.trainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * High-level trainer features for Baldur's Gate 3.
 * Provides methods to modify in-game stats like health, gold, and action points.
 */
public class TrainerFeatures {

    private static final Logger logger = LoggerFactory.getLogger(TrainerFeatures.class);

    private final MemoryScanner scanner;

    // Known memory offsets (example values — would be reverse-engineered from the game)
    private static final long HEALTH_OFFSET = 0x123456;
    private static final long GOLD_OFFSET = 0x789ABC;
    private static final long ACTION_POINTS_OFFSET = 0xDEF012;

    /**
     * Constructs a TrainerFeatures instance with a given MemoryScanner.
     *
     * @param scanner an active MemoryScanner attached to BG3
     */
    public TrainerFeatures(MemoryScanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Sets the player's health to a specific value.
     *
     * @param health the desired health value (max 9999)
     * @return true if successful
     */
    public boolean setHealth(int health) {
        if (health < 0 || health > 9999) {
            logger.warn("Health value out of range: {}", health);
            return false;
        }
        byte[] data = intToBytes(health);
        logger.info("Setting health to {}", health);
        return scanner.writeMemory(HEALTH_OFFSET, data);
    }

    /**
     * Sets the player's gold amount.
     *
     * @param gold the desired gold amount
     * @return true if successful
     */
    public boolean setGold(int gold) {
        if (gold < 0) {
            logger.warn("Gold cannot be negative");
            return false;
        }
        byte[] data = intToBytes(gold);
        logger.info("Setting gold to {}", gold);
        return scanner.writeMemory(GOLD_OFFSET, data);
    }

    /**
     * Sets the player's action points (for combat).
     *
     * @param points the desired action points (max 10)
     * @return true if successful
     */
    public boolean setActionPoints(int points) {
        if (points < 0 || points > 10) {
            logger.warn("Action points out of range: {}", points);
            return false;
        }
        byte[] data = intToBytes(points);
        logger.info("Setting action points to {}", points);
        return scanner.writeMemory(ACTION_POINTS_OFFSET, data);
    }

    /**
     * Converts an integer to a 4-byte little-endian array.
     *
     * @param value the integer value
     * @return byte array in little-endian order
     */
    private byte[] intToBytes(int value) {
        return new byte[]{
                (byte) (value & 0xFF),
                (byte) ((value >> 8) & 0xFF),
                (byte) ((value >> 16) & 0xFF),
                (byte) ((value >> 24) & 0xFF)
        };
    }
}
