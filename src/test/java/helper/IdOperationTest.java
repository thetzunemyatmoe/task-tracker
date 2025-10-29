package helper;

import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class IdOperationTest {

    @Test
    void testGetIdSet_ReturnsEmptySet_ForEmptyTaskList() {
        Set<Integer> id_set = IdOperation.getIdSet(new ArrayList<>());
        Assertions.assertTrue(id_set.isEmpty());
    }

    @Test
    void testGetIdSet_ReturnsValidSet_ForValidTaskList() {
        List<Task> taskList = new ArrayList<>();
        Set<Integer> validIdSet = new HashSet<>();

        for (int i = 0; i < 3; i++) {
            taskList.add(new Task(i, "testing" + i));
            validIdSet.add(i);
        }

        Set<Integer> idSet = IdOperation.getIdSet(taskList);
        Assertions.assertEquals(validIdSet, idSet);

    }

    @Test
    void testGetIdSet_ReturnsIdSetWithoutDuplicate() {
        List<Task> taskList = new ArrayList<>();
        Set<Integer> validIdSet = new HashSet<>();

        for (int i = 0; i < 3; i++) {
            taskList.add(new Task(i, "testing" + i));
            validIdSet.add(i);
        }

        // Two task with same id
        taskList.add(new Task(1, "testing" + 1));

        Set<Integer> idSet = IdOperation.getIdSet(taskList);
        Assertions.assertEquals(validIdSet, idSet);
    }

    @Test
    void testGenerate_Returns1_WhenIdSetIsEmpty() {
        int returnId = IdOperation.generateId(new HashSet<>());
        Assertions.assertEquals(1, returnId);
    }

    @Test
    void testGenerate_Returns1_WhenIdSetStartsFrom2() {
        Set<Integer> idSet = new HashSet<>();

        for (int i = 2; i < 5; i++){
            idSet.add(i);
        }
        int returnId = IdOperation.generateId(idSet);
        Assertions.assertEquals(1, returnId);
    }

    @Test
    void testGenerate_ReturnsFirstValidId_ForConsecutiveIdSequence() {
        Set<Integer> idSet = new HashSet<>();

        for (int i = 1; i < 5; i++){
            idSet.add(i);
        }
        int returnId = IdOperation.generateId(idSet);
        Assertions.assertEquals(5, returnId);
    }

    @Test
    void testGenerate_ReturnsFirstValidId_ForNonConsecutiveIdSequence() {
        Set<Integer> idSet = new HashSet<>();

        idSet.add(1);
        idSet.add(2);
        idSet.add(4);
        int returnId = IdOperation.generateId(idSet);
        Assertions.assertEquals(3, returnId);
    }




}