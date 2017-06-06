package org.fiodorov.service.api;

import java.util.List;

import org.fiodorov.model.Question;
import org.fiodorov.model.User;

/**
 * @author rfiodorov
 *         on 1/30/17.
 */
public interface StatisticsServiceApi {

    List<User> getBestKarma(Integer limit);

    List<Question> getBestQuestions(Integer limit);

}
