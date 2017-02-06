
package com.droiders.closer.Users;

import java.util.HashMap;
import java.util.Map;

public class Picture {

    private Data data;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Picture() {
    }

    /**
     * 
     * @param data
     */
    public Picture(Data data) {
        super();
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
