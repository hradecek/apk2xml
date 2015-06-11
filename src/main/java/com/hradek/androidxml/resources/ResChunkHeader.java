package com.hradek.androidxml.resources;

import com.hradek.androidxml.util.ByteUtils;
import com.hradek.androidxml.annotation.Size;

/**
 * This class represents chunk header
 *
 * @author Ivo Hradek
 */
public class ResChunkHeader {
    /* Private fields */
    @Size(2)
    private byte[] type;
    @Size(2)
    private byte[] headerSize;
    @Size(4)
    private byte[] size;


    /**
     * Getter for type
     *
     * @return type of header
     */
    public int getType() {
        return ByteUtils.toUnsignedShort(type);
    }

    /**
     * Getter for size
     *
     * @return size of whole file
     */
    public long getSize() {
        return ByteUtils.toUnsignedInt(size);
    }

    /**
     * Getter for header size
     *
     * @return header size
     */
    public int getHeaderSize() {
        return ByteUtils.toUnsignedShort(headerSize);
    }
}
