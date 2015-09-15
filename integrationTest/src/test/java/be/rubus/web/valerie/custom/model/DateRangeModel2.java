package be.rubus.web.valerie.custom.model;

import be.rubus.web.valerie.custom.DateRange;

import java.util.Date;

/**
 *
 */
@DateRange(start = "startDate", end = "endDate", equalsAllowed = true)
public class DateRangeModel2 {

    private Date startDate;
    private Date endDate;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
