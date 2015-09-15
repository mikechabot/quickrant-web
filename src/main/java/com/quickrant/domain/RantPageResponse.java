package com.quickrant.domain;

import com.quickrant.model.Rant;

import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RantPageResponse {

    private List<Rant> rants;
    private Map<StatisticType, PageStatistic> pageStatistics;

    public RantPageResponse(Page page, int numberOfRantsViewed) {

        rants = page.getContent();
        numberOfRantsViewed += rants.size();

        long totalRants = page.getTotalElements();
        int currentPage = page.getNumber() + 1;
        int totalPages = page.getTotalPages();

        addPageStatistic(new PageStatistic(StatisticType.TOTAL_RANTS, 0, totalRants));
        addPageStatistic(new PageStatistic(StatisticType.RANTS_VIEWED, 1, numberOfRantsViewed));
        addPageStatistic(new PageStatistic(StatisticType.RANTS_TO_VIEW, 2, totalRants - numberOfRantsViewed));
        addPageStatistic(new PageStatistic(StatisticType.CURRENT_PAGE, 3, currentPage));
        addPageStatistic(new PageStatistic(StatisticType.PAGES_TO_VIEW, 4, totalPages - currentPage));
        addPageStatistic(new PageStatistic(StatisticType.TOTAL_PAGES, 5, totalPages));
    }

    public List<Rant> getRants() {
        return rants;
    }

    public void setRants(List<Rant> rants) {
        this.rants = rants;
    }

    public Map<StatisticType, PageStatistic> getPageStatistics() {
        return pageStatistics;
    }

    public void setPageStatistics(Map<StatisticType, PageStatistic> pageStatistics) {
        this.pageStatistics = pageStatistics;
    }

    public void addPageStatistic(PageStatistic pageStatistic) {
        if (pageStatistics == null) {
            pageStatistics = new HashMap<>();
        }
        pageStatistics.put(pageStatistic.getType(), pageStatistic);
    }

}
