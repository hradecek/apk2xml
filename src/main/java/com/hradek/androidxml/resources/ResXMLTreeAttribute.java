package com.hradek.androidxml.resources;

import com.hradek.androidxml.annotation.Value;
import com.hradek.androidxml.annotation.StringPoolRef;

/**
 * This class represents attribute
 *
 * @author Ivo Hradek
 */
public class ResXMLTreeAttribute {
    /* Private fields */
    @StringPoolRef
    private ResStringPoolRef rawValue;
    @StringPoolRef
    private ResStringPoolRef name;
    @StringPoolRef
    private ResStringPoolRef ns;
    @Value
    private ResValue typedValue;

    /**
     * Getter for raw value as string pool reference
     *
     * @return raw value of attribute
     */
    public ResStringPoolRef getRawValue() {
        return rawValue;
    }

    /**
     * Getter for name of attribute as string pool refernce
     *
     * @return name
     */
    public ResStringPoolRef getName() {
        return name;
    }

    /**
     * Getter for namespace of attribute as string pool refernce
     *
     * @return namespace
     */
    public ResStringPoolRef getNs() {
        return ns;
    }

    /**
     * Getter for typed value
     *
     * @return typed value
     */
    public ResValue getTypedValue() {
        return typedValue;
    }
}
