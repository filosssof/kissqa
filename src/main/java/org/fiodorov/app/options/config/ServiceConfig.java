package org.fiodorov.app.options.config;

import org.fiodorov.repository.AnswerRepository;
import org.fiodorov.repository.AnswerVoteRepository;
import org.fiodorov.repository.QuestionRepository;
import org.fiodorov.repository.QuestionUserVoteRepository;
import org.fiodorov.repository.UserRepository;
import org.fiodorov.service.api.AnswerServiceApi;
import org.fiodorov.service.api.QuestionServiceApi;
import org.fiodorov.service.api.StatisticsServiceApi;
import org.fiodorov.service.api.UserServiceApi;
import org.fiodorov.service.impl.AnswerServiceImpl;
import org.fiodorov.service.impl.QuestionServiceImpl;
import org.fiodorov.service.impl.StatisticsServiceImpl;
import org.fiodorov.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author rfiodorov
 *         on 1/30/17.
 */
@Configuration
public class ServiceConfig {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionUserVoteRepository questionUserVoteRepository;

    @Autowired
    private AnswerVoteRepository answerVoteRepository;

    @Bean
    public QuestionServiceApi questionServiceApi(){
        return new QuestionServiceImpl(questionRepository, userServiceApi(), answerRepository, questionUserVoteRepository, answerVoteRepository);
    }

    @Bean
    public UserServiceApi userServiceApi(){
        return new UserServiceImpl(userRepository);
    }

    @Bean
    public AnswerServiceApi answerServiceApi(){
        return new AnswerServiceImpl(answerRepository,questionRepository, userServiceApi(), answerVoteRepository);
    }

    @Bean
    public StatisticsServiceApi statisticsServiceApi(){
        return new StatisticsServiceImpl(userRepository,questionRepository);
    }

}
