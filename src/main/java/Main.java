import helper.FileOperation;
import helper.ID;
import model.Task;
import service.TaskCRUDService;

import java.io.File;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        TaskCRUDService taskService = new TaskCRUDService();
        ID idHelper = new ID();
        FileOperation fileOperation = new FileOperation(new File("./src/main/java/db/database.json"));
        List<Task> taskList = fileOperation.loadTasks();
        Set<Integer> idSet = idHelper.getIdSet(taskList);


        int isContinue;
        do {
            System.out.print("""
                Options
                [1] To add
                [2] To view
                [3] To update
                [4] To delete
                """);
            int option = getValidInput(scanner, 1, 4, "Choose an option (1-4): ");
            switch (option) {
                case 1:
                    taskService.addTask(scanner, idHelper, idSet, taskList);
                    break;
                case 2:
                    taskService.displayTaskList(taskList);
                    break;
                case 3:
                    taskService.updateTaskList(scanner, taskList, idSet);
                    break;
                case 4:
                    taskService.delete(scanner, taskList, idSet);
                    break;
            }

            isContinue = getValidInput(scanner, 0, 1, "Press [1] to continue or [0] to save the changes and exit: ");
        } while (isContinue == 1);

        fileOperation.saveTask(taskList);
        System.out.println("Program exited.");
        System.exit(0);

    }

    private static int getValidInput(Scanner scanner, int min, int max, String prompt) {
        int val;
        do {
            System.out.print(prompt);
            try {
                val = scanner.nextInt();
                scanner.nextLine();

                if (val >= min && val <= max) {
                    return val;
                } else {
                    System.out.println("Choose between " + min + " and " + max +".");
                }
            } catch (InputMismatchException exception) {
                System.out.println("Enter a number.");
                scanner.nextLine();
            }
        } while (true);
    }
}
