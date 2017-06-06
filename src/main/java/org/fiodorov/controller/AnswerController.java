package org.fiodorov.controller;

import java.security.Principal;
import java.util.Objects;

import org.fiodorov.exception.NoRightsException;
import org.fiodorov.exception.NotLoggedInException;
import org.fiodorov.exception.RepeatBestAnswerException;
import org.fiodorov.service.api.AnswerServiceApi;
import org.fiodorov.view.QuestionRankView;
import org.fiodorov.view.VoteView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling answer queries
 * Created by Roman Fiodorov on 08.12.2015.
 */
@RestController
@RequestMapping("/answers")
public class AnswerController{

    private static final Logger LOGGER = LoggerFactory.getLogger(AnswerController.class);

    private RestResponses restResponses = RestResponses.json();

    private final AnswerServiceApi answerService;

    public AnswerController(AnswerServiceApi answerService) {
        this.answerService = Objects.requireNonNull(answerService);
    }

    @RequestMapping(value = "/vote", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> voteAnswer(@RequestBody VoteView voteView, Principal principal){
        LOGGER.debug("Called voteAnswer with id={} and voting {} by {}",
                voteView.getEntityId(), voteView.getVote(),principal.getName());
        answerService.voteAnswer(voteView,principal.getName());
        return restResponses.ok();
    }

    @RequestMapping("/{id}/rank")
    @ResponseBody
    public ResponseEntity<QuestionRankView> getRankForAnswer(@PathVariable Long id, Principal principal){
        LOGGER.debug("Called getRankForAnswer for the question with id={} by {} ",id, principal.getName());
        QuestionRankView rankView = new QuestionRankView();
        rankView.setRank(answerService.getAnswerRank(id));
        return restResponses.ok(rankView);
    }

    @RequestMapping(value = "{id}/best", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> bestAnswer(@PathVariable Long id, Principal principal)
            throws NotLoggedInException, NoRightsException, RepeatBestAnswerException {
        LOGGER.debug("Called bestAnswer with id={} by {}", id, principal.getName());
        answerService.bestAnswer(id,principal.getName());
        return restResponses.ok();
    }
}
