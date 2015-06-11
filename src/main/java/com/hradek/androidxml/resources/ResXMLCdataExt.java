package com.hradek.androidxml.resources;

import com.hradek.androidxml.annotation.Value;
import com.hradek.androidxml.annotation.StringPoolRef;

/**
 * This class represents CDATA extension
 *
 * @author Ivo Hradek
 */
public class ResXMLCdataExt {
    /* Private fields */
    @StringPoolRef
    private ResStringPoolRef data;
    @Value
    private ResValue typedData;

    /**
     * Getter for data as resource  string pool reference
     *
     * @return data
     */
    public ResStringPoolRef getData() {
        return data;
    }

    /**
     * Getter value
     *
     * @return value
     */
    public ResValue getValue() {
        return typedData;
    }
}

