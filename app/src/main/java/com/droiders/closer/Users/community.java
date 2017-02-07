package com.droiders.closer.Users;

public class community {
    private String id;
    private boolean request;
    private boolean shared;
    private String post;
    private String userid;

    public community() {
    }

    public community(String userid, boolean request, boolean shared, String post ) {
        this.request = request;
        this.shared = shared;
        this.post = post;
        this.userid = userid;
    }

    public String toString() {
        return getUserid();
    }

    public boolean getRequest() {
        return request;
    }

    public void setRequest(boolean request) {
        this.request = request;
    }

    public boolean getShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof community && ((community) o).id == id;
    }
}
