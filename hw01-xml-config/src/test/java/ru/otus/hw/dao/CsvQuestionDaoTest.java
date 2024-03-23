package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvQuestionDaoTest {

    @Test
    void findAll() {
        QuestionDao questionDao = new CsvQuestionDao(new AppProperties("questions.csv"));

        List<Question> result = questionDao.findAll();

        List<Question> expected = new ArrayList<>();
        expected.add(new Question("Question1", new ArrayList<>(List.of(
                new Answer("Answer1", true),
                new Answer("Answer2", false)
        ))));

        assertArrayEquals(expected.toArray(), result.toArray());
    }
}