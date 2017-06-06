package org.fiodorov.controller;

import java.util.List;
import java.util.Map;

import org.fiodorov.model.Question;
import org.fiodorov.model.User;
import org.fiodorov.service.impl.StatisticsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling statistics requests
 * Created by Roman Fiodorov on 11.03.2016.
 */
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    StatisticsServiceImpl statisticsService;

    @RequestMapping(value="karma", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<User> loadKarmaRating(@RequestParam(value = "limit", required = false, defaultValue = "0") int limit) {
        return statisticsService.getBestKarma(limit);
    }

    @RequestMapping(value="question", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Question> loadBestQuestions(@RequestParam(value = "limit", required = false, defaultValue = "0") int limit) {
        return statisticsService.getBestQuestions(limit);
    }

    @RequestMapping(value="general", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String,Long> loadGeneralStatistics(){
        return statisticsService.getGeneralStatistics();
    }

}
