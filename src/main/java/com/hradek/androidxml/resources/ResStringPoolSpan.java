package com.hradek.androidxml.resources;

import com.hradek.androidxml.annotation.Size;
import com.hradek.androidxml.annotation.StringPoolRef;

import com.hradek.androidxml.util.ByteUtils;


/**
 * This class represents resource string pool span
 *
 * @see ResStringPool
 * @author Ivo Hradek
 */
public class ResStringPoolSpan {
    /* Privates fields */
    @StringPoolRef
    private ResStringPoolRef name;
    @Size(4)
    private byte[] firstChar;
    @Size(4)
    private byte[] lastChar;

    /**
     * Getter for name
     *
     * @return reference of name
     */
    public ResStringPoolRef getName() {
        return name;
    }

    /**
     * Getter for first char
     *
     * @return start of first char
     */
    public long getFirstChar() {
        return ByteUtils.toUnsignedInt(firstChar);
    }

    /**
     * Getter for last char
     *
     * @return start of last char
     */
    public long getLastChar() {
        return ByteUtils.toUnsignedInt(lastChar);
    }

    /**
     * Signalize end of span
     *
     * @return true if it is end of span, otherwise false
     */
    public boolean isEnd() {
        return name.getIndex() == -1; // 0xFFFFFFF
    }
}
