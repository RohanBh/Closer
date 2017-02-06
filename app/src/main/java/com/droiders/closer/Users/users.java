package com.droiders.closer.Users;

public class users {
    private String id;
    private String name;
    private String sex;
    private String addr;
    private String bloodgrp;
    private String mob;
    private String dob;
    private String email;
    private String profession;
    private String skillSet;
    private String fburl;

    public users() {

    }

    public users(String id, String name, String sex, String addr, String bloodgrp, String mob, String dob, String email,String profession, String skillSet, String fburl) {
        this.name = name;
        this.sex = sex;
        this.addr = addr;
        this.bloodgrp = bloodgrp;
        this.mob = mob;
        this.dob = dob;
        this.id = id;
        this.email = email;
        this.profession = profession;
        this.skillSet = skillSet;
        this.fburl=fburl;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getBloodgrp() {
        return bloodgrp;
    }

    public void setBloodgrp(String bloodgrp) {
        this.bloodgrp = bloodgrp;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getSkillSet() {
        return skillSet;
    }

    public void setSkillSet(String skillSet) {
        this.skillSet = skillSet;
    }

    public String getFburl() {
        return fburl;
    }

    public void setFburl(String fburl) {
        this.fburl = fburl;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof users && ((users) o).id == id;
    }
}
