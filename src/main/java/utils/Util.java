package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.awaitility.core.ConditionTimeoutException;
import static org.awaitility.Awaitility.await;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Util {
    private static final Logger logger = LogManager.getLogger(Util.class);

    public static boolean waitUntilFuncTrue(Callable<Boolean> func, int timeOutInMilliSec) {
        try {
            await()
                    .atMost(timeOutInMilliSec, TimeUnit.MILLISECONDS)
                    .pollInterval(500, TimeUnit.MILLISECONDS)
                    .until(func);
            return true;
        } catch (ConditionTimeoutException e) {
            return false;
        }
    }
}
