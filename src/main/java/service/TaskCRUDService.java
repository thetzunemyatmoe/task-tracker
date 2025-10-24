package service;

import helper.ID;
import model.Task;

import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class TaskCRUDService {

    public TaskCRUDService() {

    }

    // Create and add
    public void addTask(Scanner scanner, ID idHelper, Set<Integer> idSet, List<Task> taskList) {
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
        int id = idHelper.generateId(idSet);
        Task newTask = new Task(id,description);
        System.out.println("Task with description ('" + description + "') is created and set to 'TDOO' by default");
        taskList.add(newTask);
    }

    // List
    public void displayList(List<Task> taskList) {
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        // Print header
        System.out.printf("%-5s | %-30s | %-10s | %-16s | %-16s%n",
                "ID", "Description", "Status", "Created At", "Updated At");
        System.out.println("-------------------------------------------------------------------------------------");

        // Print each task
        for (Task t : taskList) {
            System.out.printf("%-5d | %-30s | %-10s | %-16s | %-16s%n",
                    t.getId(),
                    t.getDescription(),
                    t.getStatus(),
                    t.getCreatedAt().format(formatter1),
                    t.getUpdatedAt().format(formatter1));
        }
    }

    // Delete
    public void delete(Scanner scanner, List<Task> taskList, Set<Integer> idSet) {

        // Prompt use with the list
        displayList(taskList);

        int idToDelete;

        while (true) {
            System.out.print("Enter ID of the task to delete or [0] to undo: ");

            try {
                idToDelete = scanner.nextInt();
                scanner.nextLine(); // clear newline

                if (idToDelete == 0) {
                    return; // user chose to undo
                }

                if (idSet.contains(idToDelete)) {
                    break; // valid ID, exit loop
                } else {
                    System.out.println("Invalid ID. Try again.");
                }

            } catch (InputMismatchException e) {
                System.out.println("Enter a number.");
                scanner.nextLine(); // clear invalid input
            }
        }


        int deleteId = idToDelete;
        boolean removed = taskList.removeIf(task -> task.getId() == deleteId);

        if (removed) {
            System.out.println("Task [" + deleteId+ "] removed.");
            idSet.remove(deleteId);
            // Prompt use with the updated list
            displayList(taskList);
        }
    }
}
