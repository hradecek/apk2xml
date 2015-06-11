package com.hradek.androidxml.resources;

import java.util.List;

/**
 * This class represents String pool
 *
 * @author Ivo Hradek
 */
public class ResStringPool {
    /* PRIVATES FIELDS */
    private ResStringPoolHeader header;
    private List<String> stringData;
    private List<ResStringPoolSpan> styleData;

    /**
     * Return header
     *
     * @return header
     */
    public ResStringPoolHeader getHeader() {
        return header;
    }

    /**
     * Get string data in pool
     *
     * @return string data in pool
     */
    public List<String> getStringData() {
        return stringData;
    }

    /**
     * Get styles in pool
     *
     * @return styles data in pool
     */
    public List<ResStringPoolSpan> getStyleData() {
        return styleData;
    }

    /**
     * Set string data
     *
     * @param stringData of string data
     */
    public void setStringData(List<String> stringData) {
        this.stringData = stringData;
    }

    /**
     * Set style data
     *
     * @param styleData of style data
     */
    public void setStyleData(List<ResStringPoolSpan> styleData) {
        this.styleData = styleData;
    }

    /**
     * Set header
     *
     * @param header
     */
    public void setHeader(ResStringPoolHeader header) {
        this.header = header;
    }

    /**
     * Get string from pool by reference
     *
     * @param ref
     * @return string from pool
     */
    public String getString(ResStringPoolRef ref) {
        return getString(ref.getIndex());
    }

    /**
     * Get string from by index
     *
     * @param index
     * @throws java.lang.IllegalArgumentException if index is to high
     * @return string from pool
     */
    public String getString(long index) {
        if(index >= stringData.size()) {
            throw new IllegalArgumentException("index is too high");
        }
        return stringData.get((int) index);
    }

    /**
     * Get index of specified string in pool.
     * If string doesn't exist in string pool return -1
     *
     * @param target string
     * @return string index, otherwise -1
     */
    public int IndexOfString(String target) {
        if (target.isEmpty()) {
            return -1;
        }

        int index = 0;
        for (String str : stringData) {
            if (str == target) return index;
            ++index;
        }

        return -1;
    }

    /**
     * Returns string pool span of style specified by index
     *
     * @throws IllegalArgumentException if index is too high
     * @param stringIndex
     * @return resource string pool span
     */
    public ResStringPoolSpan getStyles(long stringIndex) {
        if(stringIndex >= stringData.size()) {
            throw new IllegalArgumentException("index too high");
        }
        int currentIdx = 0;
        for (ResStringPoolSpan style : styleData) {
            if (style.isEnd()) {

            } else if (currentIdx == stringIndex) {
                return style;
            }
        }
        return null;

    }
}