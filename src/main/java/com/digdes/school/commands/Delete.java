package com.digdes.school.commands;

import com.digdes.school.CollectionManager;

import java.util.List;
import java.util.Map;

/**
 * Класс для команды удаления.
 */
public class Delete implements Command {
    private final Map<String, Object> conditions; // условия, которые идут после WHERE
    private final List<String> signs; // знаки для условий (хранятся отдельно сохраняя порядок)
    private final List<String> andOr; // логические И ИЛИ (хранятся отдельно сохраняя порядок)

    public Delete(Map<String, Object> conditions, List<String> signs, List<String> andOr) {
        this.conditions = conditions;
        this.signs = signs;
        this.andOr = andOr;
    }

    @Override
    public List<Map<String, Object>> execute(CollectionManager collectionManager) throws Exception {
        return collectionManager.delete(conditions, signs, andOr);
    }
}
