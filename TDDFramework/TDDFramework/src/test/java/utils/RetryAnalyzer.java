package utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private ThreadLocal<Integer> counter = ThreadLocal.withInitial(() -> 0);

    @Override
    public boolean retry(ITestResult result) {

        if (!result.isSuccess()) {
            if (counter.get() < TestRunConfig.RETRY_COUNTER) {
                counter.set(counter.get() + 1);
                return true;
            }
            else
            {
                result.setStatus(ITestResult.FAILURE);
                counter.set(0);
            }
        }
        else {
            result.setStatus(ITestResult.SUCCESS);      //If test passes, TestNG marks it as passed
        }
        return false;
    }
}


