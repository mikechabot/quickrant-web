package com.quickrant.model;

import java.util.List;

public class RantPage {

    private List<Rant> rants;
    private Page page;

    public RantPage(List<Rant> rants, int pageSize, int pageNumber, int totalPages, long totalRants) {
        this.rants = rants;
        this.page = new Page(pageSize, pageNumber, totalPages, totalRants);
    }

    public List<Rant> getRants() {
        return rants;
    }

    public void setRants(List<Rant> rants) {
        this.rants = rants;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    private class Page {

        private int pageSize;
        private int pageNumber;
        private int totalPages;
        private long totalRants;

        public Page(int pageSize, int pageNumber, int totalPages, long totalRants) {
            this.pageSize = pageSize;
            this.pageNumber = pageNumber;
            this.totalPages = totalPages;
            this.totalRants = totalRants;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public long getTotalRants() {
            return totalRants;
        }

        public void setTotalRants(long totalRants) {
            this.totalRants = totalRants;
        }

    }

}
