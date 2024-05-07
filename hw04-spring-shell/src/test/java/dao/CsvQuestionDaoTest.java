package dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CsvQuestionDao.class)
class CsvQuestionDaoTest {

    @MockBean
    private AppProperties appProperties;

    @Autowired
    private QuestionDao questionDao;

    @BeforeEach
    void init() {
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