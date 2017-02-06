
package com.droiders.closer.Users;

import java.util.HashMap;
import java.util.Map;

public class Summary {

    private int totalCount;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Summary() {
    }

    /**
     * 
     * @param totalCount
     */
    public Summary(int totalCount) {
        super();
        this.totalCount = totalCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
