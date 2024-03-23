package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.CsvQuestionDao;
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

@ExtendWith(MockitoExtension.class)
class TestServiceImplTest {

    private final Student student = new Student("TestName", "TestLastName");

    @Mock
    private StreamsIOService ioService;

    @Mock
    private CsvQuestionDao questionDao;

    private TestService testService;

    @BeforeEach
    void init() {
        testService = new TestServiceImpl(ioService, questionDao);

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