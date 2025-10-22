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
                    Task newTask = new Task(description);
                    taskList.add(newTask);
                    break;
                case 2:
                    System.out.println("View");
                    break;
                case 3:
                    System.out.println("Update");
                    break;
                case 4:
                    System.out.println("Delete");
                    break;
                case 5:
                    System.out.println("Exit successfully");
                    break;
                default:
                    System.out.println("Invalid option");
                    System.exit(1);

            }
            System.out.print("Press [1] to continue or [0]  to exit:");
            isContinue = scanner.nextInt();
        } while (isContinue == 1);

        try (FileWriter fileWriter = new FileWriter(file, false)){
            gson.toJson(taskList, fileWriter);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        System.out.println("Program exited.");
        System.exit(0);



    }
}
