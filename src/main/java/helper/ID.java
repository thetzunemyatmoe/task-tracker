package helper;

import model.Task;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ID {


    public static int generateId(Set<Integer> idSet) {
        int id = 1;
        while (idSet.contains(id)) {
            id++;
        }
        return id;
    }

    public static Set<Integer> getIdSet(List<Task> taskList) {
        Set<Integer> idSet = new HashSet<>();
        for (Task task: taskList) {
            idSet.add(task.getId());
        }
        return idSet;
    }
}
