package com.hradek.androidxml.resources;

import com.hradek.androidxml.util.ByteUtils;
import com.hradek.androidxml.annotation.Size;

/**
 * This class represents header for string pool
 *
 * @see ResStringPool
 * @author Ivo Hradek
 */
public final class ResStringPoolHeader {
    /* Private fields */
    private ResChunkHeader header;

    @Size(4)
    private byte[] stringCount;
    @Size(4)
    private byte[] styleCount;
    @Size(4)
    private byte[] flags;
    @Size(4)
    private byte[] stringStart;
    @Size(4)
    private byte[] stylesStart;

    /**
     * Getter for header
     *
     * @return chunk header
     */
    public ResChunkHeader getHeader() {
        return header;
    }

    /**
     * Setter for chunk header
     *
     * @param header
     */
    public void setHeader(ResChunkHeader header) {
        this.header = header;
    }

    /**
     * Get string count in string pool
     *
     * @return string count
     */
    public long getStringCount() {
        return ByteUtils.toUnsignedInt(stringCount);
    }

    /**
     * Get styles count in string pool
     *
     * @return string count
     */
    public long getStyleCount() {
        return ByteUtils.toUnsignedInt(styleCount);
    }

    /**
     * Get flags of string pool section
     *
     * @return flags
     */
    public int getFlags() {
        return ByteUtils.toUnsignedShort(flags);
    }

    /**
     * Get address where strings starts
     *
     * @return address
     */
    public long getStringStart() {
        return ByteUtils.toUnsignedInt(stringStart);
    }

    /**
     * Get address where styles starts
     *
     * @return address
     */
    public long getStylesStart() {
        return ByteUtils.toUnsignedInt(stylesStart);
    }
}
