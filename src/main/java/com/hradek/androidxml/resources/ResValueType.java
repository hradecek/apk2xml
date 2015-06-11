package com.hradek.androidxml.resources;

/**
 * This enum represents resource value type
 *
 * @author Ivo Hradek
 */
public enum ResValueType {
    TYPE_NULL((byte) 0x00),
    TYPE_REFERENCE((byte) 0x01),
    TYPE_ATTRIBUTE((byte) 0x02),
    TYPE_STRING((byte) 0x03),
    TYPE_FLOAT((byte) 0x04),
    TYPE_DIMENSION((byte) 0x05),
    TYPE_FRACTION((byte) 0x06),
    TYPE_INT_DEC((byte) 0x10),
    TYPE_INT_HEX((byte) 0x11),
    TYPE_INT_BOOLEAN((byte) 0x12),
    TYPE_INT_COLOR_ARGB8((byte) 0x1c),
    TYPE_INT_COLOR_RGB8((byte) 0x1d),
    TYPE_INT_COLOR_ARGB4((byte) 0x1e),
    TYPE_INT_COLOR_RGB4((byte) 0x1f),
    TYPE_NOT_VALID((byte) 0xff);

    private byte type;
    ResValueType(byte type) {
        this.type = type;
    }

    public static ResValueType valueOf(byte type) {
        switch (type) {
            case 0x00:
                return TYPE_NULL;
            case 0x01:
                return TYPE_REFERENCE;
            case 0x02:
                return TYPE_ATTRIBUTE;
            case 0x03:
                return TYPE_STRING;
            case 0x04:
                return TYPE_FLOAT;
            case 0x05:
                return TYPE_DIMENSION;
            case 0x06:
                return TYPE_FRACTION;
            case 0x10:
                return TYPE_INT_DEC;
            case 0x11:
                return TYPE_INT_HEX;
            case 0x12:
                return TYPE_INT_BOOLEAN;
            case 0x1C:
                return TYPE_INT_COLOR_ARGB8;
            case 0x1D:
                return TYPE_INT_COLOR_RGB8;
            case 0x1E:
                return TYPE_INT_COLOR_ARGB4;
            case 0x1F:
                return TYPE_INT_COLOR_RGB4;
        }
        return TYPE_NOT_VALID;
    }
}
