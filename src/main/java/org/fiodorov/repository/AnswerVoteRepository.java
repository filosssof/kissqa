package org.fiodorov.repository;

import java.util.Optional;

import org.fiodorov.model.AnswerUserVote;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author rfiodorov
 *         on 5/18/17.
 */
@Repository(value = "answerUserVoteRepository")
public interface AnswerVoteRepository extends JpaRepository<AnswerUserVote,Long> {

    @EntityGraph(value = "noJoinsVoteAnswer", type = EntityGraph.EntityGraphType.FETCH)
    Optional<AnswerUserVote> findByAnswerIdAndUserId(Long answerId, Long userId);
}
