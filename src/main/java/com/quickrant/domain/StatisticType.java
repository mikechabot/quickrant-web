package com.quickrant.domain;

public enum StatisticType {

    TOTAL_RANTS("total rants"),
    RANTS_VIEWED("rants viewed"),
    RANTS_TO_VIEW("rants left to view"),
    CURRENT_PAGE("current page"),
    PAGES_TO_VIEW("pages left to view"),
    TOTAL_PAGES("total pages");

    private String label;

    StatisticType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
