package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Util {
    private static final Logger logger = LogManager.getLogger(Util.class);

    public static boolean waitUntilFuncTrue(Callable<Boolean> func, int timeOutInMilliSec) {
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTime < timeOutInMilliSec) {
            try {
                if (func.call()) {
                    return true;
                }
            } catch (Exception e) {
                // something went wrong
                logger.debug("Util.waitUntilFuncTrue: ", e);
                return false;
            }

            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }

        return false;
    }
}
