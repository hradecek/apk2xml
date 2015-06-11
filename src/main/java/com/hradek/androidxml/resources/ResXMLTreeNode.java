package com.hradek.androidxml.resources;

import com.hradek.androidxml.annotation.Size;
import com.hradek.androidxml.annotation.StringPoolRef;

import com.hradek.androidxml.util.ByteUtils;

/**
 * This class represents XML node
 *
 * @author Ivo Hradek
 */
public class ResXMLTreeNode {
    /* Private fields */
    private ResChunkHeader header;
    @Size(4)
    private byte[] lineNumber;
    @StringPoolRef
    private ResStringPoolRef comment;

    /**
     * Getter for header
     *
     * @return header
     */
    public ResChunkHeader getHeader() {
        return header;
    }

    /**
     * Setter for header
     *
     * @param header
     */
    public void setHeader(ResChunkHeader header) {
        this.header = header;
    }

    /**
     * Get original line number
     *
     * @return line number
     */
    public long getLineNumber() {
        return ByteUtils.toUnsignedInt(lineNumber);
    }

    /**
     * Get associated comment as string pool reference
     *
     * @return comment
     */
    public ResStringPoolRef getComment() {
        return comment;
    }

}
