package org.fiodorov.controller;

import java.security.Principal;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.fiodorov.exception.NotEnoughKarmaException;
import org.fiodorov.exception.NotLoggedInException;
import org.fiodorov.exception.RepeatVotingException;
import org.fiodorov.exception.VotingOwnPostException;
import org.fiodorov.service.api.QuestionServiceApi;
import org.fiodorov.view.AddAnswerView;
import org.fiodorov.view.AddQuestionView;
import org.fiodorov.view.AnswerListView;
import org.fiodorov.view.QuestionRankView;
import org.fiodorov.view.QuestionResponseView;
import org.fiodorov.view.QuestionView;
import org.fiodorov.view.VoteView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling questions queries
 * Created by Roman Fiodorov
 * on 17.11.2015.
 */
@RestController
@RequestMapping("/questions")
public class QuestionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuestionController.class);

    private static final String ANONIM = "ANONIM";

    private final QuestionServiceApi questionService;

    private RestResponses restResponses = RestResponses.json();

    public QuestionController(QuestionServiceApi questionService) {
        this.questionService = questionService;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<QuestionView> getAllQuestions(@RequestParam(value = "set", required = false, defaultValue = "0") int set, Principal principal) {
        LOGGER.debug("Get all questions for the set={} by {}", set, principal != null ? principal.getName() : ANONIM);
        return questionService.getAllQuestions(set, principal);
    }

    @RequestMapping(method = RequestMethod.POST, headers = { "Content-type=application/json" })
    @ResponseBody
    public ResponseEntity<Void> createQuestion(@RequestBody final AddQuestionView questionView, Principal principal)
            throws NotEnoughKarmaException, NotLoggedInException {
        LOGGER.debug("Add question with {} by {}", questionView, principal.getName());
        questionService.createNewQuestion(questionView, principal.getName());
        return restResponses.created();
    }

    @RequestMapping("/{id}")
    @ResponseBody
    public QuestionResponseView getQuestionById(@PathVariable Long id, Principal principal) throws EntityNotFoundException {
        LOGGER.debug("Get question with id={} by {}", id, principal != null ? principal.getName() : ANONIM);
        return questionService.getQuestionById(id, principal);
    }

    @RequestMapping("/{id}/answers")
    @ResponseBody
    public AnswerListView getAnswersByQuestionId(@PathVariable Long id, Principal principal) {
        LOGGER.debug("Get answers for the question with id={} by {} ", id, principal!=null ? principal.getName() : ANONIM);
        return questionService.getAnswerByQuestionId(id, principal);
    }

    @RequestMapping(value = "/{id}/answers", method = RequestMethod.POST,headers = {"Content-type=application/json"})
    @ResponseBody
    public ResponseEntity<Void> addAnswer(@PathVariable Long id, @RequestBody AddAnswerView answerView,
            Principal principal)
            throws NotLoggedInException, NotEnoughKarmaException {
        LOGGER.debug("Called addAnswer for question id={} with content={} by {}",
                id, answerView.getAnswer().getContent(), principal.getName());
        questionService.addAnswerForQuestion(id, answerView,principal.getName());
        return restResponses.ok();
    }


    @RequestMapping(value = "/vote", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> voteQuestion(@RequestBody VoteView voteView, Principal principal)
            throws EntityNotFoundException, RepeatVotingException, NotLoggedInException, VotingOwnPostException {
        LOGGER.debug("Vote {} the question with id={} by {} ", voteView.getVote(), voteView.getEntityId(), principal.getName());
        questionService.voteQuestion(voteView, principal.getName());
        return restResponses.accepted();
    }

    @RequestMapping("/{id}/rank")
    @ResponseBody
    public ResponseEntity<QuestionRankView> getRankForQuestion(@PathVariable Long id, Principal principal){
        LOGGER.debug("Called getRankForQuestion for the question with id={} by {} ",id, principal.getName());
        QuestionRankView rankView = new QuestionRankView();
        rankView.setRank(questionService.getQuestionRank(id));
        return restResponses.ok(rankView);
    }
}
