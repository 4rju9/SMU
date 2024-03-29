package com.example.arjun.su_bca.Utils;

import java.util.List;

public class SyllabusFromFireStoreModel {

    private List<String> title_list;
    private List<String> syllabus_list;
    private String uname;

    public SyllabusFromFireStoreModel() {
        // Default constructor required for Firestore
    }

    public List<String> getTitle_list() {
        return title_list;
    }

    public void setTitle_list(List<String> title_list) {
        this.title_list = title_list;
    }

    public List<String> getSyllabus_list() {
        return syllabus_list;
    }

    public void setSyllabus_list(List<String> syllabus_list) {
        this.syllabus_list = syllabus_list;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }
}
