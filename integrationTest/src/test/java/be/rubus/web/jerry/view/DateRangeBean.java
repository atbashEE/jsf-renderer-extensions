package be.rubus.web.jerry.view;

import be.rubus.web.valerie.custom.DateRange;
import be.rubus.web.valerie.recording.RecordValue;

import javax.enterprise.inject.Model;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 *
 */
@Model
@DateRange(start = "startDate", end = "endDate")
public class DateRangeBean {

    @RecordValue
    private Date startDate;

    @RecordValue
    @NotNull
    private Date endDate;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {

    }
}
