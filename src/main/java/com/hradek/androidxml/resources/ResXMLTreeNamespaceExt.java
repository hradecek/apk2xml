package com.hradek.androidxml.resources;

import com.hradek.androidxml.annotation.StringPoolRef;

/**
 * This class represents namespace extension
 *
 * @author Ivo Hradek
 */
public class ResXMLTreeNamespaceExt {
    /* Private fields */
    private ResXMLTreeNode node;
    @StringPoolRef
    private ResStringPoolRef prefix;
    @StringPoolRef
    private ResStringPoolRef uri;

    /**
     * Get namespace prefix as string pool reference
     *
     * @return prefix
     */
    public ResStringPoolRef getPrefix() {
        return prefix;
    }

    /**
     * Get namespace uri as string pool reference
     *
     * @return URI
     */
    public ResStringPoolRef getUri() {
        return uri;
    }
}
