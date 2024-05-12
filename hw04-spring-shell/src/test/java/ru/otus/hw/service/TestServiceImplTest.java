package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {CsvQuestionDao.class, LocalizedIOServiceImpl.class, TestServiceImpl.class})
class TestServiceImplTest {

    @MockBean
    private Student student;

    @MockBean
    private LocalizedIOService ioService;

    @MockBean
    private QuestionDao questionDao;

    @Autowired
    private TestService testService;

    @BeforeEach
    void init() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("q1", List.of(new Answer("a1", true), new Answer("a2", false))));

        when(questionDao.findAll()).thenReturn(questions);
    }

    @Test
    void executeTestForCorrectAnswer() {
        when(ioService.readIntForRangeWithPrompt(anyInt(), anyInt(), anyString(), anyString())).thenReturn(1);

        TestResult testResult = testService.executeTestFor(student);

        TestResult expectedResult = new TestResult(student);
        expectedResult.setRightAnswersCount(1);

        assertEquals(expectedResult.getRightAnswersCount(), testResult.getRightAnswersCount());
    }

    @Test
    void executeTestForIncorrectAnswer() {
        when(ioService.readIntForRangeWithPrompt(anyInt(), anyInt(), anyString(), anyString())).thenReturn(2);

        TestResult testResult = testService.executeTestFor(student);

        TestResult expectedResult = new TestResult(student);
        expectedResult.setRightAnswersCount(0);

        assertEquals(expectedResult.getRightAnswersCount(), testResult.getRightAnswersCount());
    }
}