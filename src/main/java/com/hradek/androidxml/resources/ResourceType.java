package com.hradek.androidxml.resources;

/**
 * This enum represents resource type
 *
 * @author Ivo Hradek
 */
public enum ResourceType {
    RES_NULL_TYPE(0x0000),
    RES_STRING_POOL_TYPE(0x0001),
    RES_TABLE_TYPE(0x0002),
    RES_XML_TYPE(0x0003),
    RES_XML_START_NAMESPACE_TYPE(0x0100),
    RES_XML_END_NAMESPACE_TYPE(0x0101),
    RES_XML_START_ELEMENT_TYPE(0x0102),
    RES_XML_END_ELEMENT_TYPE(0x0103),
    RES_XML_CDATA_TYPE(0x0104),
    RES_XML_RESOURCE_MAP_TYPE(0x0180),
    RES_TABLE_PACKAGE_TYPE(0x0200),
    RES_TABLE_TYPE_TYPE(0x0201),
    RES_TABLE_TYPE_SPEC_TYPE(0x0202),
    RES_NOT_VALID(0xFFFF);

    private int type;

    ResourceType(int type) {
        this.type = type;
    }

    public static ResourceType valueOf(int type) {
        switch (type) {
            case 0x0000:
                return RES_NULL_TYPE;
            case 0x0001:
                return RES_STRING_POOL_TYPE;
            case 0x0002:
                return RES_TABLE_TYPE;
            case 0x0003:
                return RES_XML_TYPE;
            case 0x0100:
                return RES_XML_START_NAMESPACE_TYPE;
            case 0x0101:
                return RES_XML_END_NAMESPACE_TYPE;
            case 0x0102:
                return RES_XML_START_ELEMENT_TYPE;
            case 0x0103:
                return RES_XML_END_ELEMENT_TYPE;
            case 0x0104:
                return RES_XML_CDATA_TYPE;
            case 0x0180:
                return RES_XML_RESOURCE_MAP_TYPE;
        }
        return RES_NOT_VALID;
    }
}
