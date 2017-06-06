package org.fiodorov.repository;

import org.fiodorov.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository for <code>Question</code>
 * Created by Roman Fiodorov on 17.11.2015.
 */
@Transactional(readOnly = true)
@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {

    Long countByIsAnswered(boolean answered);

    @Override
    @EntityGraph(value = "noJoins", type = EntityGraph.EntityGraphType.FETCH)
    Page<Question> findAll(Pageable pageable);

    @Query("select q.rank from Question q where q.id = ?1")
    Integer loadRankForQuestion(Long questionId);
}
