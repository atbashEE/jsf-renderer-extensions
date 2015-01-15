package be.rubus.web.jerry.example;

import be.rubus.web.valerie.provider.DateProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Typed;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
@ApplicationScoped
public class DummyDateProvider implements DateProvider {

    private Date fixedNow;

    public DummyDateProvider() {
        DateFormat dft = new SimpleDateFormat("dd/MM/YYYY");
        try {
            fixedNow = dft.parse("01/01/2000");
        } catch (ParseException e) {
            ; // Can be safely ignored
        }
    }

    @Override
    public Date now() {
        return fixedNow;
    }
}
