/******************************************************************************
 * Copyright [2015] [Ivo Hradek]                                              *
 * Licensed under the Apache License, Version 2.0 (the "License");            *
 * you may not use this file except in compliance with the License.           *
 * You may obtain a copy of the License at                                    *
 * http://www.apache.org/licenses/LICENSE-2.0                                 *
 *                                                                            *
 * Unless required by applicable law or agreed to in writing,                 *
 * software distributed under the License is distributed on an                *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,               *
 * either express or implied. See the License for the specific language       *
 * governing permissions and limitations under the License                    *
 ******************************************************************************/
package com.hradek.androidxml;

import java.io.InputStream;

import java.util.List;
import java.util.ArrayList;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.hradek.androidxml.resources.*;
import com.hradek.androidxml.exception.AndroidXmlParserException;

/**
 * This class represents android binary xml parser
 *
 * @author Ivo Hradek
 */
public class AndroidXmlParser {
    /* Source stream */
    private InputStream in;

    /* Resource reader */
    private ResReader reader;

    /* Fields holding info about axml */
    private Object currentExtension;
    private ResXMLTreeNode currentNode;
    private ResStringPool stringPool;
    private ResResourceMap resourceMap;
    private List<ResXMLTreeAttribute> attributes;

    /* Fields used to create DOM */
    private Element currentElem;

    private Document doc;
    private DocumentBuilder docBuilder;
    private DocumentBuilderFactory docFactory;

    /* Constructors */
    public AndroidXmlParser(InputStream in) {
        this.in = in;
        reader = new ResReader(in);
        docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setNamespaceAware(true);
        currentElem = null;
    }

    /**
     * Returns DOM document
     *
     * @return DOM document
     */
    public Document getDocument() {
        return doc;
    }

    /**
     * Returns string pool of AXML file
     *
     * @return string pool
     */
    public ResStringPool getStrings() {
        return stringPool;
    }

    /**
     * Returns resource map of AXML file
     *
     * @return resource map
     */
    public ResResourceMap getResourceMap() {
        return resourceMap;
    }

    /**
     * Returns comment of current node from string pool
     *
     * @return current node comment
     */
    public String getComment() {
        return stringPool.getString(getCommentId());
    }

    private long getCommentId() {
        return currentNode == null ? -1L : currentNode.getComment().getIndex();
    }


    /**
     * Actual line number of current node in original XML file
     *
     * @return line number of current node
     */
    public long getLineNumber() {
        return currentNode.getLineNumber();
    }

    /**
     * Returns current node's namespace prefix from string pool
     *
     * @return namespace prefix
     */
    public String getNamespacePrefix() {
        return stringPool.getString(getNamespacePrefixID());
    }

   private long getNamespacePrefixID() {
        ResXMLTreeNamespaceExt namespaceExt = (ResXMLTreeNamespaceExt) currentExtension;
        return namespaceExt == null ? -1 : namespaceExt.getPrefix().getIndex();
    }


    /**
     * Returns current node's namespace URI from string pool
     *
     * @return namespace URI
     */
    public String getNamespaceUri() {
        return stringPool.getString(getNamespaceUriId());
    }

    private long getNamespaceUriId() {
        ResXMLTreeNamespaceExt namespaceExt = (ResXMLTreeNamespaceExt) currentExtension;
        return namespaceExt == null ? -1L : namespaceExt.getUri().getIndex();
    }

    /**
     * Returns current node's CDATA raw characters string pool
     *
     * @return CDATA raw characters
     */
    public String getCData() {
        return stringPool.getString(getCDataId());
    }

    private long getCDataId() {
        ResXMLCdataExt cdataExt = (ResXMLCdataExt) currentExtension;
        return cdataExt == null ? -1L : cdataExt.getData().getIndex();
    }


    /**
     * Returns current extension namespace string pool
     * If namespace was not defined return null
     *
     * @return element namespace or null
     */
    public String getElementNamespace() {
        int id = (int) getElementNamespaceId();
        return id == 0xfffffff ? null : stringPool.getString(id);
    }

    private long getElementNamespaceId() {
        ResXMLTreeAttExt attrExt = (ResXMLTreeAttExt) currentExtension;
        if (attrExt != null) return attrExt.getNs().getIndex();

        ResXMLTreeEndElementExt endElementExt = (ResXMLTreeEndElementExt) currentExtension;
        if (endElementExt != null) return endElementExt.getNs().getIndex();

        return 0xfffffff;
    }

    /**
     * Returns current element name from string pool
     *
     * @return element name
     */
    public String getElementName() {
        return stringPool.getString(getElementNameId());

    }
    private long getElementNameId() {
        ResXMLTreeAttExt attrExt = (ResXMLTreeAttExt) currentExtension;
        if (attrExt != null) return attrExt.getName().getIndex();

        ResXMLTreeEndElementExt endElementExt = (ResXMLTreeEndElementExt) currentExtension;
        if (endElementExt != null) return endElementExt.getName().getIndex();

        return -1;
    }


    private AttributeInfo getElementId() {
        return getAttribute((int) getElementIdIndex());
    }

    private long getElementIdIndex() {
        ResXMLTreeAttExt attrExt = (ResXMLTreeAttExt) currentExtension;
        if (attrExt != null) return attrExt.getName().getIndex();

        ResXMLTreeEndElementExt endElementExt = (ResXMLTreeEndElementExt) currentExtension;
        if (endElementExt != null) return endElementExt.getName().getIndex();

        return -1L;
    }

    /**
     * Element's class
     *
     * @return element's class
     */
    public AttributeInfo getElementClass() {
        return getAttribute(getElementClassId());
    }

    private int getElementClassId() {
        ResXMLTreeAttExt attrExt = (ResXMLTreeAttExt) currentExtension;
        if (null != attrExt) {
            return attrExt.getStyleIndex();
        }
        return -1;
    }

    /**
     * Get current node's count of attributes
     *
     * @return count of attributes
     */
    public int getAttributeCount() {
        return attributes == null ? 0 : attributes.size();
    }

    /**
     * Get i-th attribute of the current node
     *
     * @param index
     * @return attribute
     */
    public AttributeInfo getAttribute(int index) {
        ResXMLTreeAttribute attr = attributes.get(index);
        return new AttributeInfo(attr);
    }

    private void clearState() {
        attributes = null;
        currentNode = null;
        currentExtension = null;
    }

    /**
     * Parse file to DOM
     *
     * @throws AndroidXmlParserException when parser fail
     */
    public void parse() throws AndroidXmlParserException {
        try {
            docBuilder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new AndroidXmlParserException(e);
        }
        doc = docBuilder.newDocument();
        doc.setXmlStandalone(false);

        String prefix = null, uri = null;
        while (true) {
            clearState();

            /* Read header */
            ResChunkHeader header = reader.readResChunkHeader();
            if (isEndOfAXml(header)) break;

            switch (ResourceType.valueOf(header.getType())) {
                case RES_XML_TYPE:
                    /* Start, just continue */
                    continue;

                case RES_NULL_TYPE:
                    /* Nothing to do */
                    break;
                case RES_STRING_POOL_TYPE:
                    ResStringPoolHeader rsph = reader.readResStringPoolHeader(header);
                    stringPool = reader.readResStringPool(rsph);
                    break;

                case RES_XML_RESOURCE_MAP_TYPE:
                    ResResourceMap rrm = reader.readResourceMap(header);
                    resourceMap = rrm;
                    break;

                case RES_XML_START_NAMESPACE_TYPE:
                    currentNode = reader.readResXMLTreeNode(header);
                    currentExtension = reader.readResXMLTreeNamespaceExt();
                    prefix = getNamespacePrefix();
                    uri = getNamespaceUri();
                    break;

                case RES_XML_END_NAMESPACE_TYPE:
                    currentNode = reader.readResXMLTreeNode(header);
                    currentExtension = reader.readResXMLTreeNamespaceExt();
                    prefix = getNamespacePrefix();
                    uri = null;
                    break;

                case RES_XML_START_ELEMENT_TYPE:
                    Element element;
                    currentNode = reader.readResXMLTreeNode(header);
                    ResXMLTreeAttExt attrExt = reader.readResXMLTreeAttExt();
                    currentExtension = attrExt;
                    attributes = new ArrayList<>();

                    String name = getElementName();

                    element = doc.createElement(name);
                    if(null != prefix && null != uri)
                        element.setAttribute("xmlns:"+prefix, uri);

                    /* Read attributes */
                    for (int i = 0; i < attrExt.getAttributeCount(); ++i) {
                        attributes.add(reader.readResXMLTreeAttribute());
                    }

                    /* Create attributes in DOM */
                    for (int i = 0; i < attrExt.getAttributeCount(); ++i) {
                        Attr attr = doc.createAttribute(((prefix != null) ? prefix + ":" : "") + getAttribute(i).getName());
                        attr.setValue(getValue(getAttribute(i).getTypeValue()));
                        element.setAttributeNode(attr);
                    }

                    if (null != currentElem) currentElem.appendChild(element);
                    else doc.appendChild(element);

                    currentElem = element;

                    break;

                case RES_XML_END_ELEMENT_TYPE:
                    currentNode = reader.readResXMLTreeNode(header);
                    currentExtension = reader.readResXMLTreeEndElementExt();

                    if(isRootElement(currentElem)) currentElem = null;
                    else currentElem = (Element) currentElem.getParentNode();

                    break;

                case RES_XML_CDATA_TYPE:
                    currentNode = reader.readResXMLTreeNode(header);
                    currentExtension = reader.readResXMLTreeCDataExt();
                    if(null != currentElem) currentElem.appendChild(doc.createCDATASection(getCData()));
                    break;
            }
        }
    }

    /* Private helpers */
    private boolean isEndOfAXml(ResChunkHeader header) {
        return header.getHeaderSize() == 0 && header.getType() == 0 && header.getSize() == 0;
    }

    /* REFACTORED: Just elemenet is root */
    private boolean isRootElement(Element element) {
        return element.getParentNode() instanceof Document;
    }

    /* Format value depending on its type */
    private String getValue(ResValue value) {
        switch (ResValueType.valueOf(value.getDataType())) {
            case TYPE_STRING:
                return stringPool.getString(value.getStringValue());
            case TYPE_NULL:
                return "null";
            case TYPE_FLOAT:
                return String.valueOf(value.getDouble());
            case TYPE_FRACTION:
                int idx = value.getFraction();
                String[] vals = new String[]{"%", "%p"};
                return String.format("%g%s", value.getComplexValue(),
                        idx < 2 ? vals[idx] : "");
            case TYPE_DIMENSION:
                idx = value.getDimension();
                vals = new String[] {"px", "dip", "sp", "pt", "in", "mm"};
                return String.format(
                        "%g%s", value.getComplexValue(),
                        idx < 6 ? vals[idx] : "");
            case TYPE_INT_DEC:
                return String.valueOf(value.getInt());
            case TYPE_INT_HEX:
                return String.format("0x%x", value.getInt());
            case TYPE_INT_BOOLEAN:
                return value.getInt() == 0 ? "false" : "true";
            case TYPE_INT_COLOR_ARGB8:
            case TYPE_INT_COLOR_ARGB4:
            case TYPE_INT_COLOR_RGB8:
            case TYPE_INT_COLOR_RGB4:
            case TYPE_REFERENCE:
                return String.format("@%08x", value.getReference());
        }
        return String.format("%d:%08x", value.getDataType(), value.getData());
    }

    /* Helper class which represents attribute */
    private class AttributeInfo {
        private int namespaceId;
        private int nameId;
        private int valueStringId;
        private ResValue typeValue;


        public AttributeInfo(ResXMLTreeAttribute attribute) {
            typeValue = attribute.getTypedValue();
            valueStringId = (int) attribute.getRawValue().getIndex();
            nameId = (int) attribute.getName().getIndex();
            namespaceId = (int) attribute.getNs().getIndex();
        }

        public int getNamespaceId() {
            return namespaceId;
        }

        public String getNamespace() {
            return stringPool.getString(namespaceId);
        }

        public int getNameId() {
            return nameId;
        }

        public String getName() {
            return stringPool.getString(nameId);
        }

        public int getValueStringId() {
            return valueStringId;
        }

        public String getValueString() {
            return stringPool.getString(valueStringId);
        }

        public ResValue getTypeValue() {
            return typeValue;
        }
    }
}
