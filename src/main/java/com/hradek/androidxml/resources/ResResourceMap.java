package com.hradek.androidxml.resources;

import java.util.List;

/**
 * This class represents Resource Map chunk
 *
 * @author Ivo Hradek
 */
public class ResResourceMap {
    /* Private fields */
    public List<Long> resourceIds;
    private ResChunkHeader header;

    /**
     * Get resource name from resource map
     *
     * @param resourceId
     * @param stringPool
     * @return resourceName
     */
    public String getResourceName(Integer resourceId, ResStringPool stringPool) {
        for (long res : resourceIds) {
            if (res == resourceId) {
                return stringPool.getString((long) res);
            }
        }
        return null;
    }

    /**
     * Getter for chunk header
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
}
