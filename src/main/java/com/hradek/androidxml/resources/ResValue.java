package com.hradek.androidxml.resources;

import com.hradek.androidxml.annotation.Size;
import com.hradek.androidxml.util.ByteUtils;

/**
 * This class represent resource value
 *
 * @author Ivo Hradek
 */
public class ResValue {
    /* Private fields */
    @Size(2)
    private byte[] size;
    @Size(1)
    private byte res0;
    @Size(1)
    private byte dataType;
    @Size(4)
    private byte[] data;

    /**
     * Getter for value
     *
     * @return size of value
     */
    public int getSize() {
        return ByteUtils.toUnsignedShort(size);
    }

    /**
     * Getter for res0
     * Note: Not used
     *
     * @return res0
     */
    private short getRes0() {
        return ByteUtils.toUnsignedByte(res0);
    }

    /**
     * Getter for data type
     *
     * @return data type as byte
     */
    public byte getDataType() {
        return dataType;
        // return ByteUtils.toUnsignedByte(dataType);
    }

    /**
     * Getter for data
     *
     * @return data
     */
    public long getData() {
        return ByteUtils.toUnsignedInt(data);
    }

    /**
     * Get data as float
     *
     * @return data as float
     */
    public float getDouble() {
        return ByteUtils.toFloat(data);
    }

    /**
     * Get data as int
     *
     * @return data as int
     */
    public int getInt() {
        return ByteUtils.toInt(data);
    }

    /**
     * Getter for reference
     *
     * @return reference
     */
    public long getReference() {
        return ByteUtils.toUnsignedInt(data);
    }

    /**
     *
     * @return
     */
    public ResStringPoolRef getStringValue() {
        ResStringPoolRef ref = new ResStringPoolRef();
        if (ByteUtils.toUnsignedInt(data) == 0xFFFFFFFF) {
            ref = null;
        } else {
            ref.setIndex(data);
        }
        return ref;
    }

    public int getFraction() {
        return ByteUtils.toInt(data) & 0xF;
    }

    public int getDimension() {
        return ByteUtils.toInt(data) & 0xF;
    }

    public float getComplexValue() {
        int radix = (int) (ByteUtils.toUnsignedInt(data) & 030L) >> 4;
        int mant = (int) (ByteUtils.toUnsignedInt(data) & ~0xFF) >> 8;
        switch (radix) {
            case 0:
                return mant;
            case 1:
                return mant / 128f;
            case 2:
                return mant / 32768f;
            case 3:
                return mant / 8388608f;
        }
        return 0.0f;
    }
}
