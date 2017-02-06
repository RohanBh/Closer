package com.droiders.closer.Users;

public class communities {
    private String id;
    private String name;
    private String desc;

    public communities() {

    }

    public communities(String id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc=desc;
    }

    public String toString() {
        return getName();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof communities && ((communities) o).id == id;
    }
}
