package com.example.arjun.su_bca.Utils;

import java.util.List;

public class SubjectsFromFireStoreModel {

    private List<String> names;
    private List<String> unames;

    public SubjectsFromFireStoreModel() {
        // Default constructor required for Firestore
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public List<String> getUnames() { return unames; }

    public void setUnames(List<String> unames) { this.unames = unames; }

}
