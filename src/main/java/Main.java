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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
                if (loaded != null) {
                    taskList = loaded;
                } else {
                    taskList = new ArrayList<>();
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
                taskList = new ArrayList<>();
            }
        } else {
            taskList = new ArrayList<>();
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
                    // Take user input
                    System.out.print("Add a short description of the task: ");
                    String description = scanner.nextLine();

                    // Create new task object
                    int id;
                    if (taskList == null || taskList.isEmpty()) {
                        id = 1;
                    } else {
                        id = taskList.size() + 1;
                    }

                    Task newTask = new Task(id,description);
                    taskList.add(newTask);
                    break;
                case 2:
                    if (taskList == null || taskList.isEmpty()) {
                        System.out.println("No tasks found.");
                        break;
                    }

                    // Set up a formatter for LocalDateTime
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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
                                t.getCreatedAt().format(formatter),
                                t.getUpdatedAt().format(formatter));
                    }
                    break;
                case 3:
                    System.out.println("Update");
                    break;
                case 4:
                    System.out.println("Delete");
                    break;
                default:
                    System.out.println("Invalid option");
            }
            System.out.print("Press [1] to continue or [0]  to exit:");
            isContinue = scanner.nextInt();

            if (!(isContinue == 0 || isContinue == 1)) {
                System.err.println("Invalid choice (No changes are saved).");
                System.exit(1);
            }
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
}
