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
import static com.epam.jwd.constant.Constant.LOCALE_PARAMETER_NAME;
import static com.epam.jwd.constant.Constant.MAIN_JSP_PATH;
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
    private static final String RU_LANGUAGE = "ru";
    private static final String FR_LANGUAGE = "fr";

    private static volatile LanguageSelectionCommand instance;

    private final BaseApplicationMessageManager messageManager;
    private final BaseCommandResponse languageCommandResponse;

    private LanguageSelectionCommand() {
        this.messageManager = ApplicationMessageManager.getInstance();
        this.languageCommandResponse = new CommandResponse(MAIN_JSP_PATH, false);
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

            switch (languageId) {
                case "1":
                    Objects.requireNonNull(session).setAttribute(LOCALE_PARAMETER_NAME, Locale.ENGLISH);
                    messageManager.setLocale(EN_LANGUAGE);
                    break;
                case "2":
                    Objects.requireNonNull(session).setAttribute(LOCALE_PARAMETER_NAME, new Locale(RU_LANGUAGE));
                    messageManager.setLocale(RU_LANGUAGE);
                    break;
                case "3":
                    Objects.requireNonNull(session).setAttribute(LOCALE_PARAMETER_NAME, Locale.FRENCH);
                    messageManager.setLocale(FR_LANGUAGE);
                    break;
                default:
                    request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(LANGUAGE_NOT_SELECTED_MESSAGE_KEY));
                    request.setAttribute(BETSLIP_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));
                    break;
            }

            ShowMainPageCommand.getInstance().execute(request);
        } catch (IncorrectEnteredDataException e) {
            request.setAttribute(ERROR_ATTRIBUTE_NAME, messageManager.getString(LANGUAGE_NOT_SELECTED_MESSAGE_KEY));
            request.setAttribute(BETSLIP_ATTRIBUTE_NAME, messageManager.getString(TRY_AGAIN_MESSAGE_KEY));
        }

        return languageCommandResponse;
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