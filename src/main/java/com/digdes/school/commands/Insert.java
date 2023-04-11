package com.digdes.school.commands;

import com.digdes.school.CollectionManager;

import java.util.List;
import java.util.Map;

/**
 * Класс для команды добавления.
 */
public class Insert implements Command {
    private final Map<String, Object> values; // значения, которые идут после VALUES

    public Insert(Map<String, Object> values) {
        this.values = values;
    }

    @Override
    public List<Map<String, Object>> execute(CollectionManager collectionManager) {
        return collectionManager.insert(values);
    }
}
