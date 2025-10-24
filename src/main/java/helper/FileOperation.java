package helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.Task;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileOperation {

    private final File file;
    Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
    public FileOperation(File file) {
        this.file = file;
    }

    public List<Task> loadTasks() {
        List<Task> taskList;
        if (this.file.exists()) {
            try (FileReader reader = new FileReader(this.file)){
                Type listType = new TypeToken<List<Task>>() {}.getType();
                List<Task> loaded = this.gson.fromJson(reader, listType);
                taskList = Objects.requireNonNullElseGet(loaded, ArrayList::new);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                taskList = new ArrayList<>();
            }
        } else {
            taskList = new ArrayList<>();
        }
        return taskList;
    }

    public void saveTask(List<Task> taskList) {
        try (FileWriter fileWriter = new FileWriter(this.file, false)){
            gson.toJson(taskList, fileWriter);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            System.out.println("Changes are saved.");
        }
    }


}
