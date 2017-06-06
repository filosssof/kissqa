package org.fiodorov.utils;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class of application default constants
 * Created by Roman Fiodorov on 16.11.2015.
 */
public class Defaults {
    public static final String SCHEMA = "public";

    private static final Logger LOGGER = LoggerFactory.getLogger(Defaults.class);

    public static final int QUESTION_ADD;
    public static final int QUESTION_MARK_UP;
    public static final int QUESTION_MARK_DOWN;
    public static final int ANSWER_MARK_UP;
    public static final int ANSWER_MARK_DOWN;
    public static final int ANSWER_ADD;
    public static final int BEST_ANSWER;
    public static final int START_KARMA;
    public static final String SALT = "h@5hsebe";
    public static final String GRAVATAR_LINK = "https://www.gravatar.com/avatar/{}?d=mm";

    static{
        Properties properties = new Properties();
        try {
            properties.load(Defaults.class.getResourceAsStream("/app.properties"));
            QUESTION_ADD = Integer.parseInt(properties.getProperty("question.add"));
            QUESTION_MARK_UP = Integer.parseInt(properties.getProperty("question.markUp.vote"));
            QUESTION_MARK_DOWN = Integer.parseInt(properties.getProperty("question.markDown.vote"));
            ANSWER_MARK_UP = Integer.parseInt(properties.getProperty("answer.markUp.vote"));
            ANSWER_MARK_DOWN = Integer.parseInt(properties.getProperty("answer.markDown.vote"));
            ANSWER_ADD = Integer.parseInt(properties.getProperty("answer.add"));
            BEST_ANSWER = Integer.parseInt(properties.getProperty("answer.best"));
            START_KARMA = Integer.parseInt(properties.getProperty("start.karma"));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(),e);
            throw new IllegalStateException(e);
        }
    }
}
