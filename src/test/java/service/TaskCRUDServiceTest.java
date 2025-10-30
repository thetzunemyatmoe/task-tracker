package service;

import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TaskCRUDServiceTest {

    private TaskCRUDService service;


    @BeforeEach
    void setUp() {
        service = new TaskCRUDService();
    }


    @Test
    void testDisplayTasks_ReturnsFalse_WhenTaskListIsEmpty() {
        assertFalse(service.displayTasks(new ArrayList<>(), 1));
    }

    @Test
    void testDisplayTasks_ReturnsTrue_WhenTaskListIsNotEmpty() {
        List<Task> taskList = new ArrayList<>();
        taskList.add(new Task(1, "TESTING"));
        assertFalse(service.displayTasks(new ArrayList<>(), 1));
    }

    @Test
    void testAddTask_addTask_WhenTaskListIsEmpty() {
        String userInput = "testing"; // selects DONE
        System.setIn(new ByteArrayInputStream(userInput.getBytes()));
        Scanner scanner = new Scanner(System.in);

        Set<Integer> idSet = new HashSet<>();
        List<Task> taskList = new ArrayList<>();

        service.addTask(scanner, idSet, taskList);

        assertFalse(idSet.isEmpty(), "Id set must not be empty");
        assertEquals(1, idSet.size(), "Id set must contain exactly 1 item");

        assertFalse(taskList.isEmpty(), "Task list must not be empty");
        assertEquals(1, taskList.size(), "Task list must contain exactly 1 item");

        Task createdTask = taskList.get(0);
        assertEquals(Status.TODO, createdTask.getStatus(), "Status of the added task must be set to TODO by default.");
        assertEquals(userInput, createdTask.getDescription(), "Description of the added task must be the same with the user input.");
    }

    @Test
    void testAddTask_emptyDescriptionThenValid() {
        // Simulate user typing: empty line, then valid input
        String simulatedInput = "\nTesting";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        Scanner scanner = new Scanner(System.in);

        Set<Integer> idSet = new HashSet<>();
        List<Task> taskList = new ArrayList<>();

        service.addTask(scanner, idSet, taskList);

        // Assertions
        assertEquals(1, taskList.size(), "Task list should have exactly 1 task");
        assertEquals(1, idSet.size(), "ID set should have exactly 1 ID");

        Task createdTask = taskList.get(0);
        assertEquals("Testing", createdTask.getDescription());
        assertEquals(Status.TODO, createdTask.getStatus());
    }


    @Test
    void testAddTask_OverExceedingDescriptionThenValid() {
        // Simulate user typing: empty line, then valid input
        String simulatedInput = "1234567890123456789012345678901\nTesting";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        Scanner scanner = new Scanner(System.in);

        Set<Integer> idSet = new HashSet<>();
        List<Task> taskList = new ArrayList<>();

        service.addTask(scanner, idSet, taskList);

        // Assertions
        assertEquals(1, taskList.size(), "Task list should have exactly 1 task");
        assertEquals(1, idSet.size(), "ID set should have exactly 1 ID");

        Task createdTask = taskList.get(0);
        assertEquals("Testing", createdTask.getDescription());
        assertEquals(Status.TODO, createdTask.getStatus());
    }

    @Test
    void testDeleteTask_RemoveTask() {
        String simulatedInput = "1\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        Scanner scanner = new Scanner(System.in);

        Set<Integer> idSet = new HashSet<>();
        List<Task> taskList = new ArrayList<>();

        idSet.add(1);
        taskList.add(new Task(1, "TESTING"));


        service.deleteTask(scanner, taskList, idSet);

        assertTrue(taskList.isEmpty());
        assertTrue(idSet.isEmpty());
    }

    @Test
    void testUpdateStatusDone() {
        TaskCRUDService service = new TaskCRUDService();
        List<Task> taskList = new ArrayList<>();
        Set<Integer> idSet = new HashSet<>();
        Task task = new Task(1, "Initial task");
        taskList.add(task);
        idSet.add(1);

        String simulatedInput = "1\n3\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        Scanner scanner = new Scanner(System.in);

        service.updateTask(scanner, taskList, idSet);

        assertEquals(Status.DONE, taskList.get(0).getStatus());
    }

    @Test
    void testUpdateDescription() {
        TaskCRUDService service = new TaskCRUDService();
        List<Task> taskList = new ArrayList<>();
        Set<Integer> idSet = new HashSet<>();
        Task task = new Task(1, "Initial task");
        taskList.add(task);
        idSet.add(1);

        String simulatedInput = "1\nUpdated description\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        Scanner scanner = new Scanner(System.in);

        service.updateTask(scanner, taskList, idSet);

        assertEquals("Updated description", taskList.get(0).getDescription());
    }


    @Test
    void testUpdateUndoZero() {
        TaskCRUDService service = new TaskCRUDService();
        List<Task> taskList = new ArrayList<>();
        Set<Integer> idSet = new HashSet<>();
        Task task = new Task(1, "Initial task");
        taskList.add(task);
        idSet.add(1);

        String simulatedInput = "0\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        Scanner scanner = new Scanner(System.in);

        service.updateTask(scanner, taskList, idSet);

        assertEquals("Initial task", taskList.get(0).getDescription());
        assertEquals(Status.TODO, taskList.get(0).getStatus());
    }
}