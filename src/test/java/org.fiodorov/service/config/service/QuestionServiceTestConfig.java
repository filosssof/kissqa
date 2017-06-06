package org.fiodorov.service.config.service;

import org.fiodorov.repository.AnswerRepository;
import org.fiodorov.repository.AnswerVoteRepository;
import org.fiodorov.repository.QuestionRepository;
import org.fiodorov.repository.QuestionUserVoteRepository;
import org.fiodorov.service.api.QuestionServiceApi;
import org.fiodorov.service.api.UserServiceApi;
import org.fiodorov.service.impl.QuestionServiceImpl;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author rfiodorov
 *         on 1/30/17.
 */
@Configuration
public class QuestionServiceTestConfig {

    @Bean
    public AnswerRepository answerRepository(){
        return Mockito.mock(AnswerRepository.class);
    }

    @Bean
    public UserServiceApi userServiceApi(){
        return Mockito.mock(UserServiceApi.class);
    }

    @Bean
    public QuestionRepository questionRepository(){
        return Mockito.mock(QuestionRepository.class);
    }

    @Bean
    public QuestionUserVoteRepository userVoteRepository(){return Mockito.mock(QuestionUserVoteRepository.class);}

    @Bean
    public AnswerVoteRepository answerVoteRepository(){return Mockito.mock(AnswerVoteRepository.class);}

    @Bean
    public QuestionServiceApi questionServiceApi(){
        return new QuestionServiceImpl(questionRepository(),userServiceApi(), answerRepository(), userVoteRepository(), answerVoteRepository());
    }
}
