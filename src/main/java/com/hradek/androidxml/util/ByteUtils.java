package com.hradek.androidxml.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* Class handling bytes */
/* Ninja documentation a.k.a. self-documented */
public class ByteUtils {
    private static ByteBuffer bb;

    public static short toUnsignedByte(byte bytes) {
        bb = ByteBuffer.allocate(1);
        bb.put(bytes);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        return (short) (bb.get() & 0xff);
    }

    public static int toUnsignedShort(byte[] bytes) {
        bb = ByteBuffer.wrap(bytes, 0, bytes.length);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb.getShort() & 0xffff;
    }

    public static long toUnsignedInt(byte[] bytes) {
        bb = ByteBuffer.wrap(bytes, 0, bytes.length);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        long ret = bb.getInt(0) & 0xfffffffL;
        return ret;
    }

    public static int toInt(byte[] bytes) {
        bb = ByteBuffer.wrap(bytes, 0, bytes.length);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb.getInt();
    }

    public static long bytesToLong(byte[] bytes, int pos) {
        bb = ByteBuffer.wrap(bytes, 0, pos);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb.getLong();
    }

    public static float toFloat(byte[] bytes) {
        bb = ByteBuffer.wrap(bytes, 0, bytes.length);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb.getFloat();
    }
}
