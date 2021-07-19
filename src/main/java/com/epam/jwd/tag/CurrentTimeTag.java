package com.epam.jwd.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.time.LocalDateTime;

public class CurrentTimeTag extends SimpleTagSupport {

    private static final String TAG_OUTPUT = "Current time: %s";

    @Override
    public void doTag() throws JspException {
        final LocalDateTime currentTime = LocalDateTime.now();
        final JspWriter out = getJspContext().getOut();
        try {
            out.println(String.format(TAG_OUTPUT, currentTime));
        } catch (IOException e) {
            throw new JspException(e);
        }
    }

}
