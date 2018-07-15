package com.vel.rest.repository.exam;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vel.rest.domain.exam.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
}
