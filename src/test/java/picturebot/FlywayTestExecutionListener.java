package picturebot;

import org.flywaydb.core.Flyway;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * Custom TestExecutionListener that uses Flyway to clean and migrate the database
 * before and after each test method.
 */
public class FlywayTestExecutionListener extends AbstractTestExecutionListener {

    /**
     * Cleans and migrates the database before each test method.
     *
     * @param testContext the test context
     */
    @Override
    public void beforeTestMethod(final TestContext testContext) {
        cleanAndMigrateDatabase(testContext);
    }

    /**
     * Cleans and migrates the database after each test method.
     *
     * @param testContext the test context
     */
    @Override
    public void afterTestMethod(final TestContext testContext) {
        cleanAndMigrateDatabase(testContext);
    }

    private void cleanAndMigrateDatabase(final TestContext testContext) {
        final Flyway flyway = testContext.getApplicationContext().getBean(Flyway.class);
        flyway.clean();
        flyway.migrate();
    }
}
