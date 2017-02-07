package com.droiders.closer.Users;

import java.util.HashMap;
import java.util.Map;

public class Cover {

    private String id;
    private Integer offsetY;
    private String source;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     *
     */
    public Cover() {
    }

    /**
     *
     * @param id
     * @param source
     * @param offsetY
     */
    public Cover(String id, Integer offsetY, String source) {
        super();
        this.id = id;
        this.offsetY = offsetY;
        this.source = source;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(Integer offsetY) {
        this.offsetY = offsetY;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}