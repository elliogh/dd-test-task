package com.digdes.school.commands;

import com.digdes.school.CollectionManager;

import java.util.List;
import java.util.Map;

/**
 * Класс для команды обновления.
 */
public class Update implements Command {
    private final Map<String, Object> values; // значения, которые идут после VALUES
    private final Map<String, Object> conditions; // условия, которые идут после WHERE
    private final List<String> signs; // знаки для условий (хранятся отдельно сохраняя порядок)
    private final List<String> andOr; // логические И ИЛИ (хранятся отдельно сохраняя порядок)

    public Update(Map<String, Object> values, Map<String, Object> conditions, List<String> signs, List<String> andOr) {
        this.values = values;
        this.conditions = conditions;
        this.signs = signs;
        this.andOr = andOr;
    }

    @Override
    public List<Map<String, Object>> execute(CollectionManager collectionManager) throws Exception {
        return collectionManager.update(values, conditions, signs, andOr);
    }
}
