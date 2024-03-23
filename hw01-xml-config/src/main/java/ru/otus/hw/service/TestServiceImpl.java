package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;
import java.util.Scanner;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");

        // Получить вопросы из дао и вывести их с вариантами ответов
        List<Question> questions = questionDao.findAll();

        Scanner scanner = new Scanner(System.in);
        questions.forEach(r -> {
            printQuestion(r);
            ioService.printLine("Answer is " + getAnswer(r, scanner) + "\n");
        });
    }

    private void printQuestion(Question question) {
        ioService.printLine(question.text());
        int i = 0;
        for (Answer item : question.answers()) {
            i++;
            ioService.printLine("   " + i + " - " + item.text());
        }
    }

    private boolean getAnswer(Question question, Scanner scanner) {
        try {
            int index = scanner.nextInt();
            if (index < 1 || index > question.answers().size()) {
                throw new IndexOutOfBoundsException();
            }
            return question.answers().get(index - 1).isCorrect();
        } catch (RuntimeException ex) {
            return false;
        }
    }
}
