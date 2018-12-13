package io.github.wysohn.triggerreactor.bukkit;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;

/*
Run every test for each versions specified.

Test1, Test2, Test3, ... on 1.5.2 -> Test1, Test2, Test3, ... on 1.6.2 -> so on
 */
public class SpigotRunner extends Runner {
    private Class testClass;
    private HashMap<Method, Description>  methodDescriptions;

    public SpigotRunner(Class testClass) {
        this.testClass = testClass;
        methodDescriptions = new HashMap<>();
    }

    public Description getDescription() {
        Description description =
                Description.createSuiteDescription(
                        testClass.getName(),
                        testClass.getAnnotations());

        for(Method method : testClass.getMethods()) {
            Annotation annotation =
                    method.getAnnotation(Test.class);
            if(annotation != null) {
                Description methodDescription =
                        Description.createTestDescription(
                                testClass,
                                method.getName(),
                                annotation);
                description.addChild(methodDescription);

                methodDescriptions.put(method, methodDescription);
            }
        }

        return description;
    }

    public void run(RunNotifier runNotifier) {
    }
}
