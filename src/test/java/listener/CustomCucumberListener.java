package listener;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CustomCucumberListener implements ConcurrentEventListener {
    private static final Logger logger = LogManager.getLogger(CustomCucumberListener.class);

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestCaseStarted.class, this::onTestCaseStarted);
        publisher.registerHandlerFor(TestCaseFinished.class, this::onTestCaseFinished);
        publisher.registerHandlerFor(TestStepFinished.class, this::onTestStepFinished);
    }

    private void onTestCaseStarted(TestCaseStarted event) {
        logger.info("\uD83D\uDE80 Scenario started: {}", event.getTestCase().getName());
    }

    private void onTestCaseFinished(TestCaseFinished event) {
        logger.info("✅ Scenario finished: {}", event.getTestCase().getName());
    }

    private void onTestStepFinished(TestStepFinished event) {
        Result result = event.getResult();
        if (result.getStatus() == Status.FAILED) {
            logger.error("❌ Step failed: {}", String.valueOf(result.getError()));
        }
        if (result.getStatus() == Status.SKIPPED) {
            logger.error("Step skipped: {}", String.valueOf(result.getError()));
        }
    }
}
