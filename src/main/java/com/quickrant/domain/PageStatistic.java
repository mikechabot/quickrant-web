package com.quickrant.domain;

public class PageStatistic {

    private StatisticType type;
    private int sortOrder;
    private long number;

    public PageStatistic(StatisticType type, int sortOrder, long number) {
        this.type = type;
        this.sortOrder = sortOrder;
        this.number = number;
    }

    public StatisticType getType() {
        return type;
    }

    public void setType(StatisticType type) {
        this.type = type;
    }

    public String getLabel() {
        return type.getLabel();
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

}
