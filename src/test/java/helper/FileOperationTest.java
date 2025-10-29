package helper;

import com.google.gson.Gson;
import model.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileOperationTest {

    @TempDir
    File tempDir;

    /*
    loadTask Tests
     */

    // File exists and has valid JSON
    @Test
    void testLoadTask_ReturnsNonEmptyList_WhenFileExistsWithValidJson() throws IOException {

        File tempFile = new File(tempDir, "test.json");
        FileOperation fileOperation = new FileOperation(tempFile);

        // Add a task
        List<Task> taskList = new ArrayList<>();
        Task newTask = new Task(1,"testing");
        taskList.add(newTask);
        fileOperation.saveTask(taskList);

        List<Task> loadedTask =  fileOperation.loadTasks();
        assertFalse(loadedTask.isEmpty(), "The loaded list from the JSON file must not be empty.");
        assertEquals(1, loadedTask.size(), "The loaded list from the JSON file must only contain 1 item.");
        assertEquals(newTask, loadedTask.getFirst(), "The fetched item and the added item must be equal.");
    }


    // File exists and Empty JSON
    @Test
    void testLoadTask_ReturnsEmptyList_WhenFileExistsWithoutValidJson() throws IOException {

        File tempFile = new File(tempDir, "test.json");
        boolean created = tempFile.createNewFile();
        FileOperation fileOperation = new FileOperation(tempFile);
        List<Task> loadedTask =  fileOperation.loadTasks();


        assertTrue(created || tempFile.exists(), "Temp file should be created for the test.");
        assertNotNull(loadedTask, "The loaded list cannot be null.");
        assertTrue(loadedTask.isEmpty(), "The loaded list cannot have any items.");


    }

    // File exists but has malformed JSON
    @Test
    void testLoadTask_ReturnsEmptyList_WhenFileExistsWithMalformedJson() throws IOException {
        File tempFile = new File(tempDir, "test.json");
        FileOperation fileOperation = new FileOperation(tempFile);
        // Add a task
        List<Task> taskList = new ArrayList<>();
        Task newTask = new Task(1,"testing");
        taskList.add(newTask);
        fileOperation.saveTask(taskList);

        // Appending invalid json
        Files.writeString(tempFile.toPath(), "{invalid json [no quotes]");
        List<Task> loadedTask =  fileOperation.loadTasks();

        assertNotNull(loadedTask, "The loaded list cannot be null.");
        assertTrue(loadedTask.isEmpty(), "The loaded list cannot have any items.");
    }


    // File does not exist
    @Test
    void testLoadTask_ReturnsEmptyList_WhenFileNotExist() throws IOException {
        File tempFile = new File(tempDir, "test.json");
        FileOperation fileOperation = new FileOperation(tempFile);

        List<Task> loadedTask =  fileOperation.loadTasks();

        assertNotNull(loadedTask, "The loaded list cannot be null.");
        assertTrue(loadedTask.isEmpty(), "The loaded list cannot have any items.");
    }

    /*
    saveTask Tests
     */
    // File is created when it does not exist
    @Test
    void testSaveTask_CreateNewFile_WhenFileNotExist() throws IOException {
        File tempFile = new File(tempDir, "test.json");
        FileOperation fileOperation = new FileOperation(tempFile);

        // Add a task
        List<Task> taskList = new ArrayList<>();
        Task newTask = new Task(1,"testing");
        taskList.add(newTask);
        fileOperation.saveTask(taskList);

        assertTrue(tempFile.exists());
    }

    @Test
    void testSaveTask_WriteValidJsonList() throws IOException {

        File tempFile = new File(tempDir, "test.json");
        FileOperation fileOperation = new FileOperation(tempFile);

        // Add a task
        List<Task> taskList = new ArrayList<>();
        taskList.add(new Task(1,"testing"));
        taskList.add(new Task(2,"testing2"));
        fileOperation.saveTask(taskList);

        String json = Files.readString(tempFile.toPath());
        Gson gson = new Gson();




        assertEquals("[]", "" + json.charAt(0) + json.charAt(json.length() - 1));
        assertTrue(json.length() > 2, "JSON should not be an empty array.");


    }


    @Test
    void testSaveTask_Overwrites() throws IOException {

        File tempFile = new File(tempDir, "test.json");
        FileOperation fileOperation = new FileOperation(tempFile);

        // Add a task
        List<Task> taskList = new ArrayList<>();
        taskList.add(new Task(1,"testing"));
        taskList.add(new Task(2,"testing2"));
        fileOperation.saveTask(taskList);

        List<Task> newTask = new ArrayList<>();
        newTask.add(new Task(3,"testing3"));
        fileOperation.saveTask(newTask);
        String json = Files.readString(tempFile.toPath());

        assertTrue(json.contains("testing3"), "New task should be present in file after overwrite.");
        assertFalse(json.contains("testing1"), "Old task 'testing' should be removed after overwrite.");
        assertFalse(json.contains("testing2"), "Old task 'testing2' should be removed after overwrite.");

    }

}