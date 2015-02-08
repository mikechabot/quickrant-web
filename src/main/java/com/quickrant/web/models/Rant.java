package com.quickrant.web.models;

import com.quickrant.web.utils.StringUtil;

public class Rant extends Document {

    private String name;
    private String location;
    private String rant;
    private Selection selection;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRant() {
        return rant;
    }

    public void setRant(String rant) {
        this.rant = rant;
    }

    public Selection getSelection() {
        return selection;
    }

    public void setSelection(Selection selection) {
        this.selection = selection;
    }

    @Override
    public boolean isValid() {
        return !StringUtil.isEmpty(name)
                && !StringUtil.isEmpty(location)
                && !StringUtil.isEmpty(rant)
                && selection.isValid();
    }

    @Override
    public String toString() {
        return "Rant{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", rant='" + rant + '\'' +
                ", selection=" + selection.toString() +
                '}';
    }

}
