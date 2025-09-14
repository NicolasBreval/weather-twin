package org.nbreval.weather_twin.gateway.test.util;

import java.nio.ByteBuffer;

/**
 * Some utils for Java type managing, only for tests
 */
public class TypeUtils {

    /**
     * Transforms a double number to a byte array representation
     * 
     * @param num The number to transform
     * @return Byte array representation of input number
     */
    public static byte[] doubleToBytes(double num) {
        ByteBuffer buffer = ByteBuffer.allocate(Double.BYTES);
        buffer.putDouble(num);
        return buffer.array();
    }
}
