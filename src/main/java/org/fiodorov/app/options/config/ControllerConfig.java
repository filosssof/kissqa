package org.fiodorov.app.options.config;

import org.fiodorov.controller.AnswerController;
import org.fiodorov.controller.LoginController;
import org.fiodorov.controller.QuestionController;
import org.fiodorov.service.api.AnswerServiceApi;
import org.fiodorov.service.api.LoginServiceApi;
import org.fiodorov.service.api.QuestionServiceApi;
import org.fiodorov.service.api.UserServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author rfiodorov
 *         on 1/30/17.
 */
@Configuration
@EnableWebMvc
@Import(value = {
        ServiceConfig.class
})
public class ControllerConfig {

    @Autowired
    private QuestionServiceApi questionService;

    @Autowired
    private AnswerServiceApi answerService;

    @Autowired
    private LoginServiceApi loginService;

    @Autowired
    private UserServiceApi userServiceApi;

    @Bean
    public QuestionController questionController(){
        return new QuestionController(questionService);
    }

    @Bean
    public AnswerController answerController(){
        return new AnswerController(answerService);
    }

    @Bean
    public LoginController loginController(){
        return new LoginController(loginService, userServiceApi);
    }
}
