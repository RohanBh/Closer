
package com.droiders.closer.Users;

import java.util.HashMap;
import java.util.Map;

public class UserInfo {

    private String id;
    private String name;
    private String link;
    private String gender;
    private Picture picture;
    private boolean verified;
    private Friends friends;
    private String email;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public UserInfo() {
    }

    /**
     * 
     * @param picture
     * @param id
     * @param friends
     * @param email
     * @param verified
     * @param link
     * @param name
     * @param gender
     */
    public UserInfo(String id, String name, String link, String gender, Picture picture, boolean verified, Friends friends, String email) {
        super();
        this.id = id;
        this.name = name;
        this.link = link;
        this.gender = gender;
        this.picture = picture;
        this.verified = verified;
        this.friends = friends;
        this.email = email;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public Friends getFriends() {
        return friends;
    }

    public void setFriends(Friends friends) {
        this.friends = friends;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
