package ru.otus.hw.commandlinerunners;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import ru.otus.hw.service.TestRunnerService;

@RequiredArgsConstructor
@Component
public class CommandlineTestRunner implements CommandLineRunner {
    private final TestRunnerService testRunnerService;

    @Override
    public void run(String... args) {

        testRunnerService.run();
    }

}
