package kirilenko.cli.commands.implementation;

import kirilenko.cli.exceptions.CliException;
import kirilenko.cli.utils.Environment;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class CdCommandTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testCdNoArguments() throws Exception {
        List<String> result = new CdCommand(Collections.emptyList()).execute(Collections.emptyList()).getOutput();
        assertTrue(result.isEmpty());
        assertEquals(System.getProperty("user.home"), Environment.getCurrentDirectory().toString());
    }

    @Test
    public void testCd() throws Exception {
        File testFolder = folder.newFolder("tmp");
        List<String> result = new CdCommand(Collections.singletonList(testFolder.getPath()))
                .execute(Collections.emptyList()).getOutput();
        assertTrue(result.isEmpty());
        assertEquals(testFolder.toPath(), Environment.getCurrentDirectory());
    }

    @Test
    public void testCdMultipleArguments() throws Exception {
        exception.expect(CliException.class);
        exception.expectMessage("cd: too many arguments");
        new CdCommand(Arrays.asList("abc", "d")).execute(Collections.emptyList());
    }

    @Test
    public void testCdNotExistingDirectory() throws Exception {
        File testFolder = folder.newFolder("tmp");
        exception.expect(CliException.class);
        exception.expectMessage("cd: " + testFolder.getPath() + "k: " + "no such directory");
        new CdCommand(Collections.singletonList(testFolder.getPath() + "k")).execute(Collections.emptyList());
    }
}