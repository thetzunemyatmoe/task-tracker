package service;

import helper.IdOperation;
import helper.InputValidator;
import model.Status;
import model.Task;

import java.time.format.DateTimeFormatter;
import java.util.*;

public class TaskCRUDService {

    public TaskCRUDService() {

    }

    // Add (C)
    public void addTask(Scanner scanner, Set<Integer> idSet, List<Task> taskList) {
        String description;
        do {
            System.out.print("Add a short description of the task: ");
            description = scanner.nextLine();

            if (description.isEmpty()) {
                System.out.println("Description required");
            } else if (description.length() > 30) {
                System.out.println("Description too long");
            }

        } while (description.isBlank() || description.length() > 30);

        // Create new task object
        int id = IdOperation.generateId(idSet);
        Task newTask = new Task(id,description);
        System.out.println("Task with description ('" + description + "') is created and set to 'TDOO' by default");
        taskList.add(newTask);
        idSet.add(id);
    }

    // List (R)
    public boolean displayTasks(List<Task> taskList, int mode) {

        if (taskList.isEmpty()) {
            System.out.println("No tasks found.");
            return false;
        }

        Set<Status> statusList = new HashSet<>();
        if (mode == 1) {
            statusList.add(Status.TODO);
        } else if (mode == 2) {
            statusList.add(Status.IN_PROGRESS);
        } else if (mode == 3) {
            statusList.add(Status.DONE);
        } else if (mode == 4) {
            statusList.add(Status.TODO);
            statusList.add(Status.IN_PROGRESS);
            statusList.add(Status.DONE);
        }


        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        // Print header
        System.out.printf("%-5s | %-30s | %-12s | %-16s | %-16s%n",
                "IdOperation", "Description", "Status", "Created At", "Updated At");
        System.out.println("-------------------------------------------------------------------------------------");

        // Print each task
        for (Task t : taskList) {
            if (statusList.contains(t.getStatus())) {
                System.out.printf("%-5d | %-30s | %-12s | %-16s | %-16s%n",
                        t.getId(),
                        t.getDescription(),
                        t.getStatus(),
                        t.getCreatedAt().format(formatter1),
                        t.getUpdatedAt().format(formatter1));
            }

        }

        return true;
    }

    // Update (U)
    public void updateTask(Scanner scanner, List<Task> taskList, Set<Integer> idSet) {
        // Prompt use with the list
        if(!displayTasks(taskList, 4)){
            return;
        };

        int updateId = InputValidator.getValidIdFromUser(scanner, idSet, "Enter IdOperation of the task to update or [0] to undo: ");
        if (updateId == 0) {
            System.out.println("Exiting");
            return;
        }

        System.out.println(updateId);
        Optional<Task> optionalTaskToUpdate = taskList.stream().filter(task -> task.getId() == updateId).findFirst();

        if (optionalTaskToUpdate.isEmpty()) {
            System.out.printf("Task with id [%d] does not exist.%n", updateId);
            return;
        }

        Task taskToUpdate = optionalTaskToUpdate.get();
        System.out.print("""
                Options
                [1] Set TODO
                [2] Set IN_PROGRESS
                [3] Set DONE
                [Any text] To update description
                """);

        String userInput;
        do {
            System.out.print("Enter choice: ");
            userInput = scanner.nextLine();

            if (userInput.isEmpty()) {
                System.out.println("Description required");
            } else if (userInput.length() > 30) {
                System.out.println("Description too long");
            }

        } while (userInput.isBlank() || userInput.length() > 30);


        if (userInput.matches("\\d+")) {
            switch (Integer.parseInt(userInput)){
                case 1:
                    taskToUpdate.setStatus(Status.TODO);
                    System.out.println("Status set as TODO");
                    break;
                case 2:
                    taskToUpdate.setStatus(Status.IN_PROGRESS);
                    System.out.println("Status set as IN PROGRESS");
                    break;
                case 3:
                    taskToUpdate.setStatus(Status.DONE);
                    System.out.println("Status set as DONE");
                    break;
                default:
                    System.out.println("Invalid input");
                    break;
            }
        } else {
            taskToUpdate.setDescription(userInput);
            System.out.println("Description updated.");

        }

        // Prompt user after update
        displayTasks(taskList, 4);

    }

    // Delete (D)
    public void deleteTask(Scanner scanner, List<Task> taskList, Set<Integer> idSet) {

        // Prompt use with the list
        if(!displayTasks(taskList, 4)) {
            return;
        };
        int deleteId = InputValidator.getValidIdFromUser(scanner, idSet, "Enter IdOperation of the task to delete or [0] to undo: ");

        if (deleteId == 0) {
            return;
        }
        boolean removed = taskList.removeIf(task -> task.getId() == deleteId);

        if (removed) {
            System.out.println("Task [" + deleteId+ "] removed.");
            idSet.remove(deleteId);
            // Prompt use with the updated list
            displayTasks(taskList, 4);
        }
    }


}
