package com.epam.jwd.tag;

import com.epam.jwd.manager.ApplicationMessageManager;
import com.epam.jwd.manager.BaseApplicationMessageManager;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

import static com.epam.jwd.constant.Constant.INITIAL_INDEX_VALUE;
import static com.epam.jwd.constant.Constant.MIN_INDEX_VALUE;
import static com.epam.jwd.constant.Constant.PERSON_NAME_SESSION_ATTRIBUTE_NAME;

/**
 * An {@code UserWelcomeTag} class is a custom JSP tag that used for to greet the application person.
 *
 * @see TagSupport
 */
public class UserWelcomeTag extends TagSupport {

    private static final String UNAUTHORIZED_WELCOME_MESSAGE_KEY = "person.unauthorized.welcome";
    private static final String AUTHORIZED_WELCOME_MESSAGE_KEY = "person.authorized.welcome";
    private static final String REGEX = "_";

    private final BaseApplicationMessageManager messageManager = ApplicationMessageManager.getInstance();

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
                .map(formattedName -> String.format(messageManager.getString(UNAUTHORIZED_WELCOME_MESSAGE_KEY),
                        formattedName))
                .orElse(messageManager.getString(AUTHORIZED_WELCOME_MESSAGE_KEY));
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