package org.fiodorov.repository;

import java.util.List;

import org.fiodorov.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Roman Fiodorov
 * on 18.11.2015.
 */
@Transactional(readOnly = true)
public interface AnswerRepository extends JpaRepository<Answer,Long>{
    List<Answer> findByQuestionIdOrderByIsBestDescRankDescCreatedDateDesc(long questionId);

    @Query("select a.rank from Answer a where a.id = ?1")
    Integer loadRankForAnswer(Long answerId);
}

