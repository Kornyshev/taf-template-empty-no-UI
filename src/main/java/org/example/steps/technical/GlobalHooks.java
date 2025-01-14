package org.example.steps.technical;

import io.cucumber.java.*;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.example.core.TestContext;
import org.example.core.properties.PropertiesLoader;

@Slf4j
public class GlobalHooks extends CucumberSteps {

    @BeforeAll(order = 1)
    public static void loadProperties() {
        PropertiesLoader.getInstance().readAllProperties();
    }

    @Before
    public void restContext() {
        TestContext.resetContext();
    }

    @After()
    public void performSoftAssert() {
        SoftAssertions assertions = TestContext.getContext(TestContext.ContextKeys.SOFT_ASSERT);
        assertions.assertAll();
    }

}
