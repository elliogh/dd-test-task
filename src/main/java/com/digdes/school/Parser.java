package com.digdes.school;

import com.digdes.school.commands.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс парсер команд.
 */
public class Parser {
    private final Map<String, List<String>> keysWithPossibleSigns; // Map полей с возможными операциями

    public Parser() {
        this.keysWithPossibleSigns = new HashMap<>();
        keysWithPossibleSigns.put("id", Arrays.asList("=", "!=", ">", "<", ">=", "<="));
        keysWithPossibleSigns.put("lastname", Arrays.asList("=", "!=", "like", "ilike"));
        keysWithPossibleSigns.put("age", Arrays.asList("=", "!=", ">", "<", ">=", "<="));
        keysWithPossibleSigns.put("cost", Arrays.asList("=", "!=", ">", "<", ">=", "<="));
        keysWithPossibleSigns.put("active", Arrays.asList("=", "!="));
    }

    /**
     * Метод для парсинга команды
     *
     * @param command команда
     * @return объект команды
     */
    public Command parseCommand(String command) throws Exception {
        command = command.trim();
        String typeOfCommand = command.trim().split(" ")[0].toLowerCase();

        String valuesStr = extractValues(command); // строка со значениями между VALUES и WHERE
        String conditionsStr = extractConditions(command); // строка с условиями после WHERE
        // Определяем тип команды и парсим ее
        if (typeOfCommand.equals("insert")) {
            return parseInsert(valuesStr);
        }
        if (typeOfCommand.equals("update")) {
            return parseUpdate(valuesStr, conditionsStr);
        }
        if (typeOfCommand.equals("select")) {
            return parseSelect(conditionsStr);
        }
        if (typeOfCommand.equals("delete")) {
            return parseDelete(conditionsStr);
        }

        return null; // если такой команды нет - возращаем null
    }

    private Command parseInsert(String valuesStr) throws Exception {
        Map<String, Object> values = parseExpression(valuesStr); // достаем значения
        return new Insert(values);
    }

    private Command parseUpdate(String valuesStr, String conditionsStr) throws Exception {
        List<String> signs = parseSigns(conditionsStr); // операторы
        List<String> andOrs = parseAndOr(conditionsStr); // AND OR
        Map<String, Object> values = parseExpression(valuesStr); // значения
        Map<String, Object> conditions = parseExpression(conditionsStr); // достаем
        checkSignsInConditionals(conditions, signs); // проверяем тип колонки с возможными операторами
        return new Update(values, conditions, signs, andOrs);
    }

    private Command parseSelect(String conditionsStr) throws Exception {
        List<String> signs = parseSigns(conditionsStr); // операторы
        List<String> andOrs = parseAndOr(conditionsStr); // AND OR
        Map<String, Object> conditions = parseExpression(conditionsStr); // условия
        checkSignsInConditionals(conditions, signs); // проверяем тип колонки с возможными операторами
        return new Select(conditions, signs, andOrs);
    }

    private Command parseDelete(String conditionsStr) throws Exception {
        List<String> signs = parseSigns(conditionsStr); // операторы
        List<String> andOrs = parseAndOr(conditionsStr); // AND OR
        Map<String, Object> conditions = parseExpression(conditionsStr); // условия
        checkSignsInConditionals(conditions, signs); // проверяем тип колонки с возможными операторами
        return new Delete(conditions, signs, andOrs);
    }

    /**
     * Метод, который достает значения из строки
     *
     * @param command команда
     * @return значения
     */
    private String extractValues(String command) {
        Pattern pattern = Pattern.compile("(?i)(?<=values\\s).+");
        Matcher matcher = pattern.matcher(command);
        String values = null;
        while (matcher.find()) {
            values = command.substring(matcher.start(), matcher.end()); // достаем то, что идет после VALUES
        }
        if (values != null) {
            pattern = Pattern.compile("(?i).+(?=where)");
            matcher = pattern.matcher(values);
            while (matcher.find()) {
                values = values.substring(matcher.start(), matcher.end()); // достаем то, что идет до WHERE
            }
        }

        return values;
    }

    /**
     * Метод, который достает условия из строки
     *
     * @param command команда
     * @return условия
     */
    private String extractConditions(String command) {
        Pattern pattern = Pattern.compile("(?i)(?<=where\\s).+");
        Matcher matcher = pattern.matcher(command);
        String conditions = null;
        while (matcher.find()) {
            conditions = command.substring(matcher.start(), matcher.end()); // достаем то, что идет после WHERE
        }

        return conditions;
    }

    /**
     * Метод, который парсит строку значений или условий
     *
     * @param str строка значений или условий
     * @return Map колонка -> значение
     */
    private Map<String, Object> parseExpression(String str) throws Exception {
        Map<String, Object> map = new LinkedHashMap<>(); // чтобы хранить упорядоченно
        if (str != null) {
            String[] terms = str.split("(?i)and|or|,"); // разделяем выражение
            for (String term : terms) {
                String[] a = term.split("=|!=|like|ilike|>=|<=|>|<"); // каждый терм разделяем по оператору
                String key = a[0].trim().replace("'", "").toLowerCase(); // ключ без кавычек в нижнем регистре
                checkKey(key); // проверяем, есть ли такая колонка в коллекции
                Object value = getValueWithType(a[1].trim()); // значение с нужным типом

                map.put(key, value);
            }
        }

        return map;
    }

    /**
     * Метод для получения значения с нужным типом
     *
     * @param value значение
     * @return значение с нужным типом
     */
    private Object getValueWithType(String value) {
        if (value.equals("true")) {
            return true;
        }
        if (value.equals("false")) {
            return false;
        }
        if (value.equals("null")) {
            return null;
        }
        try {
            return Long.parseLong(value);
        } catch (Exception ignored) {
        }
        try {
            return Double.parseDouble(value);
        } catch (Exception ignored) {
        }

        return value.replace("'", ""); // если просто строка, то удаляем кавычки
    }

    /**
     * Метод, который достает операторы в нужном порядке
     *
     * @param str строка
     * @return список операторов
     */
    private List<String> parseSigns(String str) {
        List<String> operators = new ArrayList<>();

        if (str != null) {
            Pattern pattern = Pattern.compile("=|!=|like|ilike|>=|<=|>|<");
            Matcher matcher = pattern.matcher(str);
            while (matcher.find()) {
                operators.add(matcher.group());
            }
        }
        return operators;
    }

    /**
     * Метод, который достает И ИЛИ в нужном порядке
     *
     * @param str строка
     * @return список И ИЛИ
     */
    private List<String> parseAndOr(String str) {
        List<String> andOr = new ArrayList<>();

        if (str != null) {
            Pattern pattern = Pattern.compile("(?i)\\band|or\\b");
            Matcher matcher = pattern.matcher(str);
            while (matcher.find()) {
                andOr.add(matcher.group());
            }
        }
        return andOr;
    }

    private void checkKey(String key) throws Exception {
        if (!keysWithPossibleSigns.containsKey(key)) {
            throw new Exception(key + " такой колонки нет");
        }
    }

    private void checkSignsInConditionals(Map<String, Object> conditions, List<String> signs) throws Exception {
        int i = 0;
        for (String key : conditions.keySet()) {
            List<String> possibleSigns = keysWithPossibleSigns.get(key);
            if (!possibleSigns.contains(signs.get(i))) {
                throw new Exception(signs.get(i) + " не допустимо с колонкой " + key);
            }
            i++;
        }
    }
}
