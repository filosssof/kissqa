package org.fiodorov.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fiodorov.model.Question;
import org.fiodorov.model.User;
import org.fiodorov.repository.QuestionRepository;
import org.fiodorov.repository.UserRepository;
import org.fiodorov.service.api.StatisticsServiceApi;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Service for handling statistics data
 * (best questions, best users etc.)
 * Created by Roman Fiodorov on 11.03.2016.
 */
@Service
public class StatisticsServiceImpl implements StatisticsServiceApi{

    private static final int STANDARD_LIMIT_USERS_LIST = 10;
    private static final int STANDARD_LIMIT_QUESTION_LIST = 10;

    private final UserRepository userRepository;

    private final QuestionRepository questionRepository;

    public StatisticsServiceImpl(UserRepository userRepository, QuestionRepository questionRepository) {
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
    }

    public List<User> getBestKarma(Integer limit){
        if (limit == null || limit <= 0){
            limit = STANDARD_LIMIT_USERS_LIST;
        }

        final PageRequest page = new PageRequest(
                0, limit, new Sort(
                new Sort.Order(Sort.Direction.DESC,"rank")
        ));
        return userRepository.findAll(page).getContent();
    }


    public List<Question> getBestQuestions(Integer limit){
        if (limit == null || limit <= 0){
            limit = STANDARD_LIMIT_QUESTION_LIST;
        }

        final PageRequest page = new PageRequest(
                0, limit, new Sort(
                new Sort.Order(Sort.Direction.DESC,"rank"),
                new Sort.Order(Sort.Direction.DESC,"answersNum"),
                new Sort.Order(Sort.Direction.DESC,"createdDate")
        ));
        return questionRepository.findAll(page).getContent();
    }

    public Map<String,Long> getGeneralStatistics(){
        Map<String,Long> resultMap = new HashMap<>();
        Long totalQuestions = questionRepository.count();
        resultMap.put("totalQuestions",totalQuestions);
        Long unansweredQuestions = questionRepository.countByIsAnswered(false);
        resultMap.put("unansweredQuestions",unansweredQuestions);
        Long totalUsers = userRepository.count();
        resultMap.put("totalUsers",totalUsers);
        return resultMap;

    }
}
