package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.otus.hw.security.LoginContext;
import ru.otus.hw.service.TestRunnerService;

@RequiredArgsConstructor
@ShellComponent(value = "Application Commands")
public class ApplicationCommands {

    private final LoginContext loginContext;

    private final TestRunnerService testRunnerService;

    @ShellMethod(value = "Login command", key = {"l", "login"})
    public String login(@ShellOption(defaultValue = "AnyUser") String userName) {
        loginContext.login(userName);
        return String.format("User %s registered", userName);
    }

    @ShellMethod(value = "Test command", key = {"t", "test"})
    @ShellMethodAvailability(value = "isTestCommandAvailable")
    public String test() {
        testRunnerService.run();
        return "Test completed";
    }

    private Availability isTestCommandAvailable() {
        return loginContext.isUserLoggedIn()
                ? Availability.available()
                : Availability.unavailable("Use login or l to register first");
    }
}
