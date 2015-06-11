package com.hradek.androidxml.resources;

import com.hradek.androidxml.annotation.AttrExt;
import com.hradek.androidxml.annotation.Node;

import java.util.List;

/**
 * This class represents star element chunk
 *
 * @author Ivo Hradek
 */
public class ResXMLTreeStartElement {
    /* Private fields */
    private List<ResXMLTreeAttribute> atrributes;
    @Node
    private ResXMLTreeNode node;
    @AttrExt
    private ResXMLTreeAttExt attrExt;

    /**
     * Getter for list of associated attributes
     *
     * @return associate attributes
     */
    public List<ResXMLTreeAttribute> getAtrributes() {
        return atrributes;
    }

    /**
     * Setter for attributes
     *
     * @param atrributes
     */
    public void setAtrributes(List<ResXMLTreeAttribute> atrributes) {
        this.atrributes = atrributes;
    }
}
