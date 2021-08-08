package com.epam.jwd.command.change;

import com.epam.jwd.command.BaseCommandRequest;
import com.epam.jwd.command.BaseCommandResponse;
import com.epam.jwd.command.Command;
import com.epam.jwd.command.CommandResponse;
import com.epam.jwd.command.page.ShowMainPageCommand;
import com.epam.jwd.exception.IncorrectEnteredDataException;
import com.epam.jwd.manager.ApplicationMessageManager;
import com.epam.jwd.manager.BaseApplicationMessageManager;

import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.Objects;

import static com.epam.jwd.constant.Constant.BETSLIP_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.ERROR_ATTRIBUTE_NAME;
import static com.epam.jwd.constant.Constant.INDEX_JSP_PATH;
import static com.epam.jwd.constant.Constant.LOCALE_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.TRY_AGAIN_MESSAGE_KEY;

/**
 * A {@code LanguageSelectionCommand} class implements {@code Command}
 * interface and execute command that select application language.
 *
 * @see Command
 */
public class LanguageSelectionCommand implements Command {

    private static final String LANGUAGE_NOT_SELECTED_MESSAGE_KEY = "language.not.selected";
    private static final String EN_LANGUAGE = "en";
    private static final String SPA_LANGUAGE = "spa";
    private static final String FR_LANGUAGE = "fr";
    private static final String ENGLISH = "1";
    private static final String SPANISH = "2";
    private static final String FRENCH = "3";

    private static volatile LanguageSelectionCommand instance;

    private final BaseApplicationMessageManager messageManager;
    private final BaseCommandResponse successSelectingCommandResponse;

    private LanguageSelectionCommand() {
        this.messageManager = ApplicationMessageManager.getInstance();
        this.successSelectingCommandResponse = new CommandResponse(INDEX_JSP_PATH, true);
    }

    public static LanguageSelectionCommand getInstance() {
        if (instance == null) {
            synchronized (LanguageSelectionCommand.class) {
                if (instance == null) {
                    instance = new LanguageSelectionCommand();
                }
            }
        }

        return instance;
    }

    @Override
    public BaseCommandResponse execute(BaseCommandRequest request) {
        try {
            final String languageId = getCheckedLanguage(request);

            HttpSession session = null;

            if (request.getCurrentSession().isPresent()) {
                session = request.getCurrentSession().get();
            }

            switchLanguage(request, languageId, session);

            ShowMainPageCommand.getInstance().execute(request);
        } catch (IncorrectEnteredDataException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(LANGUAGE_NOT_SELECTED_MESSAGE_KEY));
            request.setAttribute(BETSLIP_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));
        }

        return successSelectingCommandResponse;
    }

    private void switchLanguage(BaseCommandRequest request, String languageId, HttpSession session) {
        switch (languageId) {
            case ENGLISH:
                Objects.requireNonNull(session).setAttribute(LOCALE_PARAMETER_NAME, Locale.ENGLISH);
                messageManager.setLocale(EN_LANGUAGE);
                break;
            case SPANISH:
                Objects.requireNonNull(session).setAttribute(LOCALE_PARAMETER_NAME, new Locale(SPA_LANGUAGE));
                messageManager.setLocale(SPA_LANGUAGE);
                break;
            case FRENCH:
                Objects.requireNonNull(session).setAttribute(LOCALE_PARAMETER_NAME, Locale.FRENCH);
                messageManager.setLocale(FR_LANGUAGE);
                break;
            default:
                request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(LANGUAGE_NOT_SELECTED_MESSAGE_KEY));
                request.setAttribute(BETSLIP_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));
                break;
        }
    }

    private String getCheckedLanguage(BaseCommandRequest request) throws IncorrectEnteredDataException {
        final String language;

        if (request.getParameter(LOCALE_PARAMETER_NAME) != null) {
            language = request.getParameter(LOCALE_PARAMETER_NAME);
            return language;
        }

        throw new IncorrectEnteredDataException(messageManager.getString(LANGUAGE_NOT_SELECTED_MESSAGE_KEY));
    }

}