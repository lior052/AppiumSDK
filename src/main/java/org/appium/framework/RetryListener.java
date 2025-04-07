package org.appium.framework;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class RetryListener implements IAnnotationTransformer {
    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        if (annotation == null) {
            System.out.println("Skipping transformation due to null annotation.");
            return; // Avoid NullPointerException
        }

        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
}
