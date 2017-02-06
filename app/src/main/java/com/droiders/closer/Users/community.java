package com.droiders.closer.Users;

public class community {
    private String id;
    private String request;
    private String shared;
    private String post;

    public community() {
    }

    public community(String id, String request, String shared, String post) {
        this.id = id;
        this.request = request;
        this.shared = shared;
        this.post = post;
    }

    public String toString() {
        return getId();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getShared() {
        return shared;
    }

    public void setShared(String shared) {
        this.shared = shared;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof community && ((community) o).id == id;
    }
}
