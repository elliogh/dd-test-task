package com.digdes.school.commands;

import com.digdes.school.CollectionManager;

import java.util.List;
import java.util.Map;

/**
 * Интерфейс команды.
 */
public interface Command {
    List<Map<String, Object>> execute(CollectionManager collectionManager) throws Exception;
}
