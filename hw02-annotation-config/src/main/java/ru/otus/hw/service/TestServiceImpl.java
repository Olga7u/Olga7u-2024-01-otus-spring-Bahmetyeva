package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        List<Question> questions = questionDao.findAll();
        TestResult testResult = new TestResult(student);

        for (Question question : questions) {
            int answerInd = ioService.readIntForRangeWithPrompt(1, question.answers().size(),
                    getQuestionTestWithAnswers(question),
                    "Something goes wrong");
            boolean isAnswerValid = question.answers().get(answerInd - 1).isCorrect(); // Задать вопрос, получить ответ
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private String getQuestionTestWithAnswers(Question question) {
        StringBuilder text = new StringBuilder(question.text());
        int i = 0;
        for (Answer answer : question.answers()) {
            i++;
            text.append("\n   ").append(i).append(" - ").append(answer.text());
        }
        return text.toString();
    }
}
