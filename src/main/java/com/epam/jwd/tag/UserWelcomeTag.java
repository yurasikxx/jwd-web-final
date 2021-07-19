package com.epam.jwd.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Optional;

import static com.epam.jwd.command.LogInCommand.PERSON_NAME_SESSION_ATTRIBUTE_NAME;

public class UserWelcomeTag extends TagSupport {

    private static final String PERSON_WELCOME_MSG = "Hello, %s";
    private static final String DEFAULT_WELCOME_MSG = "Hello! Click below to sign in :)";

    @Override
    public int doStartTag() throws JspException {
        final String tagResultText = buildResultText();
        printTextToOut(tagResultText);
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() {
        return EVAL_PAGE;
    }

    private String buildResultText() {
        return Optional.ofNullable(pageContext.getSession())
                .map(session -> session.getAttribute(PERSON_NAME_SESSION_ATTRIBUTE_NAME))
                .map(name -> String.format(PERSON_WELCOME_MSG, name))
                .orElse(DEFAULT_WELCOME_MSG);
    }

    private void printTextToOut(String tagResultText) throws JspException {
        final JspWriter out = pageContext.getOut();
        try {
            out.println(tagResultText);
        } catch (IOException e) {
            throw new JspException(e);
        }
    }

}