
package com.droiders.closer.Users;

import java.util.HashMap;
import java.util.Map;

public class Cursors {

    private String before;
    private String after;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Cursors() {
    }

    /**
     * 
     * @param after
     * @param before
     */
    public Cursors(String before, String after) {
        super();
        this.before = before;
        this.after = after;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
