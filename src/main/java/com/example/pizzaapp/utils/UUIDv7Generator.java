package com.example.pizzaapp.utils;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.UUID;

/**
 * Utility class to generate UUID version 7 identifiers.
 * UUIDv7 includes a timestamp-based prefix for time-ordered UUIDs.
 */
public class UUIDv7Generator {

    // SecureRandom ensures strong randomness for the UUID's random part (LSB).
    private static final SecureRandom random = new SecureRandom();

    /**
     * Generates a UUIDv7-style UUID.
     * 
     * @return a UUID object conforming to the UUIDv7 time-ordered format.
     */
    public static UUID generate() {
        // Get the current timestamp in milliseconds since epoch
        long timestamp = Instant.now().toEpochMilli();

        // Start constructing the most significant bits (MSB) with the UUIDv7 version (0111 -> 0x7)
        long msb = 0x70L << 60;

        // Incorporate the lower 60 bits of the timestamp into MSB (leaving 4 bits for the version)
        msb |= (timestamp & 0x0FFFFFFFFFFFFFFFL) << 16;

        // Generate 64 bits of randomness for the least significant bits (LSB)
        long lsb = random.nextLong();

        // Return the UUID composed of the MSB and LSB
        return new UUID(msb, lsb);
    }
}
