
package com.droiders.closer.Users;

import java.util.HashMap;
import java.util.Map;

public class Paging {

    private Cursors cursors;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Paging() {
    }

    /**
     * 
     * @param cursors
     */
    public Paging(Cursors cursors) {
        super();
        this.cursors = cursors;
    }

    public Cursors getCursors() {
        return cursors;
    }

    public void setCursors(Cursors cursors) {
        this.cursors = cursors;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
