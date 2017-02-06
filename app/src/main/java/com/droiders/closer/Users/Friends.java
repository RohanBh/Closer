
package com.droiders.closer.Users;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Friends {

    private List<Datum> data = null;
    private Paging paging;
    private Summary summary;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Friends() {
    }

    /**
     * 
     * @param summary
     * @param data
     * @param paging
     */
    public Friends(List<Datum> data, Paging paging, Summary summary) {
        super();
        this.data = data;
        this.paging = paging;
        this.summary = summary;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
