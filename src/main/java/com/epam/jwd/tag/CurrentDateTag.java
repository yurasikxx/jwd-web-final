package com.epam.jwd.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.Calendar;

public class CurrentDateTag extends SimpleTagSupport {

    private static final String TAG_OUTPUT = "%02d/%02d/%4d";

    @Override
    public void doTag() throws JspException {
        final JspWriter out = getJspContext().getOut();
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        try {
            out.println(String.format(TAG_OUTPUT, month, day, year));
        } catch (IOException e) {
            throw new JspException(e);
        }
    }

}