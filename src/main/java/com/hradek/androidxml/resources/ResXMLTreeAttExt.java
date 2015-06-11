package com.hradek.androidxml.resources;

import com.hradek.androidxml.annotation.Size;
import com.hradek.androidxml.annotation.StringPoolRef;

import com.hradek.androidxml.util.ByteUtils;

/**
 * This class represents attribute extension
 *
 * @author Ivo Hradek
 */
public class ResXMLTreeAttExt {
    /* Private fields */
    @StringPoolRef
    private ResStringPoolRef ns;
    @StringPoolRef
    private ResStringPoolRef name;

    @Size(2)
    private byte[] attributeStart;
    @Size(2)
    private byte[] attributeSize;
    @Size(2)
    private byte[] attributeCount;
    @Size(2)
    private byte[] idIndex;
    @Size(2)
    private byte[] classIndex;
    @Size(2)
    private byte[] styleIndex;

    /**
     * Get namespace as string pool reference
     *
     * @return namespace
     */
    public ResStringPoolRef getNs() {
        return ns;
    }

    /**
     * Get name of attribute as string pool reference
     *
     * @return attribute name
     */
    public ResStringPoolRef getName() {
        return name;
    }

    /**
     * Get address of starting attribute
     *
     * @return address
     */
    public int getAttributeStart() {
        return ByteUtils.toUnsignedShort(attributeStart);
    }

    /**
     * Get attribute size
     *
     * @return size
     */
    public int getAttributeSize() {
        return ByteUtils.toUnsignedShort(attributeSize);
    }

    /**
     * Attributes count
     *
     * @return count
     */
    public int getAttributeCount() {
        return ByteUtils.toUnsignedShort(attributeCount);
    }

    /**
     * Get ID of attribute
     *
     * @return index
     */
    public int getIdIndex() {
        return ByteUtils.toUnsignedShort(idIndex);
    }

    /**
     * Get class ID
     *
     * @return index
     */
    public int getClassIndex() {
        return ByteUtils.toUnsignedShort(classIndex);
    }

    /**
     * Get style ID
     *
     * @return index
     */
    public int getStyleIndex() {
        return ByteUtils.toUnsignedShort(styleIndex);
    }
}
