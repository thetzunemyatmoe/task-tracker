import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import helper.LocalDateTimeAdapter;
import model.Task;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        File file = new File("./src/main/java/db/database.json");
        List<Task> taskList = null;

        // Gson to serialize and deserialize
        Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();


        if (file.exists()) {
            try (FileReader reader = new FileReader(file)){
                Type listType = new TypeToken<List<Task>>() {}.getType();
                List<Task> loaded = gson.fromJson(reader, listType);
                taskList = Objects.requireNonNullElseGet(loaded, ArrayList::new);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                taskList = new ArrayList<>();
            }
        } else {
            taskList = new ArrayList<>();
        }

        Set<Integer> idSet = new HashSet<>();
        for (Task task: taskList) {
            idSet.add(task.getId());
        }

        Scanner scanner = new Scanner(System.in);
        int isContinue;

        do {

            // TODO: Input mismatch
            System.out.print("""
                Options
                [1] To add
                [2] To view
                [3] To update
                [4] To delete
                """);
            System.out.print("Choose an option (1-4): ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
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
                    int id = generateId(idSet);
                    Task newTask = new Task(id,description);
                    taskList.add(newTask);
                    break;
                case 2:
                    if (taskList.isEmpty()) {
                        System.out.println("No tasks found.");
                        break;
                    }
                    displayList(taskList);
                    break;
                case 3:
                    System.out.println("Update");
                    break;
                case 4:
                    displayList(taskList);
                    if (delete(scanner, taskList, idSet)) break;
                    displayList(taskList);
                    break;
                default:
                    System.out.println("Invalid option");
            }

            do {
                System.out.print("Press [1] to continue or [0] to save the changes and exit: ");
                isContinue = scanner.nextInt();
            } while (isContinue != 0 && isContinue != 1);
        } while (isContinue == 1);

        try (FileWriter fileWriter = new FileWriter(file, false)){
            gson.toJson(taskList, fileWriter);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            System.out.println("Changes are saved.");
        }
        System.out.println("Program exited.");
        System.exit(0);

    }

    static int generateId(Set<Integer> idSet) {
        int id = 1;
        while (idSet.contains(id)) {
            id++;
        }
        return id;
    }

    static boolean delete(Scanner scanner, List<Task> taskList, Set<Integer> idSet) {
        int idToDelete;
        do {
            System.out.print("Enter ID of the task to delete or [0] to undo: ");
            idToDelete = scanner.nextInt();

            if (idToDelete == 0) {
                return true;
            }

            if (!idSet.contains(idToDelete)) {
                System.out.println("Invalid ID. Try again.");
            }

        } while (!idSet.contains(idToDelete));


        int deleteId = idToDelete;
        boolean removed = taskList.removeIf(task -> task.getId() == deleteId);

        if (removed) {
            System.out.println("Task [" + deleteId+ "] removed.");
            idSet.remove(deleteId);
        }
        return false;
    }

    // Display functionality
    static void displayList(List<Task> taskList) {
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
}
