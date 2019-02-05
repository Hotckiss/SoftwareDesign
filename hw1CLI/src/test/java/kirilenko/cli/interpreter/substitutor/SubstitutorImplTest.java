package kirilenko.cli.interpreter.substitutor;

import kirilenko.cli.utils.Environment;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Substitutor test class
 */
public class SubstitutorImplTest {
    @Before
    public void prepare() {
        Environment.clear();
    }

    /**
     * No substitution test
     */
    @Test
    public void substituteTestSimple() throws Exception {
        Substitutor substitutor = new SubstitutorImpl();
        String statement = "aaa";

        assertEquals("aaa", substitutor.substitute(statement));
    }

    /**
     * No substitution in single quotes test
     */
    @Test
    public void substituteTestSingleQuoted() throws Exception {
        Substitutor substitutor = new SubstitutorImpl();
        Environment.setVariable("t", "text");
        String statement = "'$t'";

        assertEquals("'$t'", substitutor.substitute(statement));
    }

    /**
     * Substitution in double quotes test
     */
    @Test
    public void substituteTestDoubleQuoted() throws Exception {
        Substitutor substitutor = new SubstitutorImpl();
        Environment.setVariable("t", "text");
        String statement = '"' + "$t" + '"';

        assertEquals("\"text\"", substitutor.substitute(statement));
    }

    /**
     * Substitution and no substitution in double and single quotes test
     */
    @Test
    public void substituteTestMulti() throws Exception {
        Substitutor substitutor = new SubstitutorImpl();
        Environment.setVariable("t", "text");
        String statement = "\"$t\" '$t'";

        assertEquals("\"text\" '$t'", substitutor.substitute(statement));
    }
}