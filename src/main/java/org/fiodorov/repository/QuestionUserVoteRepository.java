package org.fiodorov.repository;

import java.util.Optional;

import org.fiodorov.model.QuestionUserVote;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author rfiodorov
 *         on 5/17/17.
 */
@Repository(value = "questionUserVoteRepository")
public interface QuestionUserVoteRepository extends JpaRepository<QuestionUserVote,Long> {

    @EntityGraph(value = "noJoinsVoteQuestion", type = EntityGraph.EntityGraphType.FETCH)
    Optional<QuestionUserVote> findByQuestionIdAndUserId(Long questionId, Long userId);
}
