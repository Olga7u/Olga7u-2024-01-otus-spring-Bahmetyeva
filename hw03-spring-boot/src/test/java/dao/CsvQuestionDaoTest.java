package dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CsvQuestionDaoTest {

    QuestionDao questionDao;

    @Mock
    AppProperties appProperties;

    @BeforeEach
    void init(){
        questionDao = new CsvQuestionDao(appProperties);

        when(appProperties.getTestFileName()).thenReturn("questions.csv");
    }

    @Test
    void findAll() {
        List<Question> result = questionDao.findAll();

        List<Question> expected = new ArrayList<>();
        expected.add(new Question("Question1", new ArrayList<>(List.of(
                new Answer("Answer1", true),
                new Answer("Answer2", false)
        ))));

        assertArrayEquals(expected.toArray(), result.toArray());
    }
}