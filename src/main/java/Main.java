import helper.FileOperation;
import helper.ID;
import helper.InputValidator;
import model.Task;
import service.TaskCRUDService;
import java.io.File;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        TaskCRUDService taskService = new TaskCRUDService();
        FileOperation fileOperation = new FileOperation(new File("./src/main/java/db/database.json"));
        List<Task> taskList = fileOperation.loadTasks();
        Set<Integer> idSet = ID.getIdSet(taskList);


        int isContinue;
        do {
            System.out.print("""
                Options
                [1] To add
                [2] To view [ALL]
                [3] To update
                [4] To delete
                [5] To view [TODO ONLY]
                [6] To view [IN_PROGRESS ONLY]
                [7] To view [DONE ONLY]
                """);
            int option = InputValidator.getValidInput(scanner, 1, 7, "Choose an option (1-7): ");
            switch (option) {
                case 1:
                    taskService.addTask(scanner, idSet, taskList);
                    break;
                case 2:
                    taskService.displayTasks(taskList, 4);
                    break;
                case 3:
                    taskService.updateTask(scanner, taskList, idSet);
                    break;
                case 4:
                    taskService.deleteTask(scanner, taskList, idSet);
                    break;
                case 5:
                    taskService.displayTasks(taskList, 1);
                    break;
                case 6:
                    taskService.displayTasks(taskList, 2);
                    break;
                case 7:
                    taskService.displayTasks(taskList, 3);
                    break;
            }

            isContinue = InputValidator.getValidInput(scanner, 0, 1, "Press [1] to continue or [0] to save the changes and exit: ");
        } while (isContinue == 1);

        fileOperation.saveTask(taskList);
        System.out.println("Program exited.");
        System.exit(0);

    }


}
