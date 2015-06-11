package com.hradek.androidxml.resources;

import com.hradek.androidxml.annotation.StringPoolRef;

/**
 * This class represents end element
 *
 * @author Ivo Hradek
 */
public class ResXMLTreeEndElementExt {
    /* Private fields */
    @StringPoolRef
    private ResStringPoolRef ns;
    @StringPoolRef
    private ResStringPoolRef name;

    /**
     * Get namespace as string pool reference
     *
     * @return namespace
     */
    public ResStringPoolRef getNs() {
        return ns;
    }

    /**
     * Get name of the element as string pool reference
     *
     * @return name of element
     */
    public ResStringPoolRef getName() {
        return name;
    }
}
