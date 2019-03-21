package kirilenko.cli.commands.implementation;

import kirilenko.cli.exceptions.CliException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class LsCommandTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testLsNoArguments() throws Exception {
        File testFolder = createTemporaryFolderWithFiles("file1", "f2");
        new CdCommand(Collections.singletonList(testFolder.getPath())).execute(Collections.emptyList());

        List<String> result = new LsCommand(Collections.emptyList())
                .execute(Collections.emptyList()).getOutput();
        assertEquals(1, result.size());
        assertTrue(result.get(0).equals("file1 f2") || result.get(0).equals("f2 file1"));
    }

    @Test
    public void testLsOneArgument() throws Exception {
        File testFolder = createTemporaryFolderWithFiles("file1", "f2");
        List<String> result = new LsCommand(Collections.singletonList(testFolder.getPath()))
                .execute(Collections.emptyList()).getOutput();
        assertEquals(1, result.size());
        assertTrue(result.get(0).equals("file1 f2") || result.get(0).equals("f2 file1"));
    }

    @Test
    public void testLsMultipleArgument() throws Exception {
        File testFolder1 = createTemporaryFolderWithFiles("file1", "f2");
        File testFolder2 = createTemporaryFolderWithFiles("f1", "abc");

        List<String> result = new LsCommand(Arrays.asList(testFolder1.getPath(), testFolder2.getPath()))
                .execute(Collections.emptyList()).getOutput();
        assertEquals(2, result.size());
        assertTrue(result.get(0).equals("file1 f2") || result.get(0).equals("f2 file1"));
        assertTrue(result.get(1).equals("abc f1") || result.get(1).equals("f1 abc"));
    }

    @Test
    public void testLsNotExistingDirectory() throws Exception {
        File testFolder = createTemporaryFolderWithFiles();
        exception.expect(CliException.class);
        exception.expectMessage("ls: " + testFolder.getPath() + "k: no such directory");
        new LsCommand(Collections.singletonList(testFolder.getPath() + "k")).execute(Collections.emptyList());
    }

    private File createTemporaryFolderWithFiles(String... filesToCreate) throws IOException {
        File testFolder = folder.newFolder();
        for (String file : filesToCreate) {
            new File(testFolder, file).createNewFile();
        }
        return testFolder;
    }
}