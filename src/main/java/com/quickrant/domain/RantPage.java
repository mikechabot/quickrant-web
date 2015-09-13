package com.quickrant.domain;

import com.quickrant.model.Rant;
import org.springframework.data.domain.Page;

import java.util.List;

public class RantPage {

    private List<Rant> rants;
    private PageInfo pageInfo;

    public RantPage(Page page) {
        this.rants = page.getContent();
        this.pageInfo = new PageInfo(page.getNumber(), rants.size(), page.getTotalPages(), page.getTotalElements());
    }

    public List<Rant> getRants() {
        return rants;
    }

    public void setRants(List<Rant> rants) {
        this.rants = rants;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    private class PageInfo {

        private int pageNumber;
        private int pageSize;
        private int totalPages;
        private long totalRants;

        public PageInfo(int pageNumber, int pageSize, int totalPages, long totalRants) {
            this.pageNumber = pageNumber;
            this.pageSize = pageSize;
            this.totalPages = totalPages;
            this.totalRants = totalRants;
        }

        public int getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
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
