package com.epam.jwd.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

import static com.epam.jwd.constant.Constant.INITIAL_INDEX_VALUE;
import static com.epam.jwd.constant.Constant.MIN_INDEX_VALUE;
import static com.epam.jwd.constant.Constant.PERSON_NAME_SESSION_ATTRIBUTE_NAME;

public class UserWelcomeTag extends TagSupport {

    private static final String PERSON_WELCOME_MSG = "Hello, %s! See below to next moves:";
    private static final String DEFAULT_WELCOME_MSG = "Hello! Click below to sign up or sign in :)";
    private static final String REGEX = "_";

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
        final Function<String[], String> stringFunction =
                firstname -> firstname[MIN_INDEX_VALUE].substring(MIN_INDEX_VALUE, INITIAL_INDEX_VALUE).toUpperCase() +
                        firstname[MIN_INDEX_VALUE].substring(INITIAL_INDEX_VALUE);

        return Optional.ofNullable(pageContext.getSession())
                .map(session -> session.getAttribute(PERSON_NAME_SESSION_ATTRIBUTE_NAME))
                .map(Object::toString)
                .map(name -> name.split(REGEX))
                .map(stringFunction)
                .map(formattedName -> String.format(PERSON_WELCOME_MSG, formattedName))
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