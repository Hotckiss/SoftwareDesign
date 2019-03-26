package kirilenko.cli.interpreter.substitutor;

import kirilenko.cli.utils.Environment;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Substitutor test class
 */
public class SubstitutorImplTest {
    private final Environment testEnvironment = new Environment();

    @Before
    public void prepare() {
        testEnvironment.clear();
    }

    /**
     * No substitution test
     */
    @Test
    public void substituteTestSimple() throws Exception {
        Substitutor substitutor = new SubstitutorImpl();
        String statement = "aaa";

        assertEquals("aaa", substitutor.substitute(statement, testEnvironment));
    }

    /**
     * No substitution in single quotes test
     */
    @Test
    public void substituteTestSingleQuoted() throws Exception {
        Substitutor substitutor = new SubstitutorImpl();
        testEnvironment.setVariable("t", "text");
        String statement = "'$t'";

        assertEquals("'$t'", substitutor.substitute(statement, testEnvironment));
    }

    /**
     * Substitution in double quotes test
     */
    @Test
    public void substituteTestDoubleQuoted() throws Exception {
        Substitutor substitutor = new SubstitutorImpl();
        testEnvironment.setVariable("t", "text");
        String statement = '"' + "$t" + '"';

        assertEquals("\"text\"", substitutor.substitute(statement, testEnvironment));
    }

    /**
     * Substitution and no substitution in double and single quotes test
     */
    @Test
    public void substituteTestMulti() throws Exception {
        Substitutor substitutor = new SubstitutorImpl();
        testEnvironment.setVariable("t", "text");
        String statement = "\"$t\" '$t'";

        assertEquals("\"text\" '$t'", substitutor.substitute(statement, testEnvironment));
    }
}