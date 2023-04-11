package com.digdes.school;

import java.util.*;

/**
 * Класс для управления коллекцией.
 */
public class CollectionManager {
    private final List<Map<String, Object>> collection;

    public CollectionManager() {
        this.collection = new ArrayList<>();
    }

    /**
     * Метод для вставки в коллекцию
     *
     * @param values значения
     * @return коллекция
     */
    public List<Map<String, Object>> insert(Map<String, Object> values) {
        collection.add(values);
        return getCollectionCopy(); // возвращаем копию коллекции
    }

    /**
     * Метод для обновления элементов коллекции
     *
     * @param values значения
     * @param where условия
     * @param signs операторы
     * @param andOrs И ИЛИ
     * @return коллекция
     */
    public List<Map<String, Object>> update(Map<String, Object> values, Map<String, Object> where, List<String> signs, List<String> andOrs) throws Exception {
        for (Map<String, Object> row : collection) {
            List<Boolean> listWhereResults = getWhereResults(where, signs, row); // получаем правдивость каждого условия

            if (isRowSatisfies(listWhereResults, andOrs)) { // проверяем, если все WHERE правдивое, то обновляем
                row.putAll(values);
            }
        }
        return getCollectionCopy(); // возвращаем копию коллекции
    }

    /**
     * Метод для удаления элементов коллекции
     *
     * @param where условия
     * @param signs операторы
     * @param andOrs И ИЛИ
     * @return коллекция
     */
    public List<Map<String, Object>> delete(Map<String, Object> where, List<String> signs, List<String> andOrs) throws Exception {
        if (!where.isEmpty()) {
            List<Map<String, Object>> rowsToDelete = new ArrayList<>();
            for (Map<String, Object> row : collection) {
                List<Boolean> listWhereResults = getWhereResults(where, signs, row); // получаем правдивость каждого условия

                if (isRowSatisfies(listWhereResults, andOrs)) { // проверяем, если все WHERE правдивое, то удаляем
                    rowsToDelete.add(row);
                }
            }

            collection.removeAll(rowsToDelete); // удаление строки
        } else {
            collection.clear(); // если пустой DELETE, то очищаем всю коллекцию
        }
        return getCollectionCopy(); // возвращаем копию коллекции
    }

    /**
     * Метод для выбора элементов коллекции
     *
     * @param where условия
     * @param signs операторы
     * @param andOrs И ИЛИ
     * @return коллекция
     */
    public List<Map<String, Object>> select(Map<String, Object> where, List<String> signs, List<String> andOrs) throws Exception {
        List<Map<String, Object>> result = getCollectionCopy();
        if (!where.isEmpty()) {
            result = new ArrayList<>();
            for (Map<String, Object> row : collection) {
                List<Boolean> listWhereResults = getWhereResults(where, signs, row); // получаем правдивость каждого условия

                if (isRowSatisfies(listWhereResults, andOrs)) { // проверяем, если все WHERE правдивое, то добавляем строку
                    result.add(row);
                }
            }
        }
        return result;
    }

    // Метод для получения логического списка для каждого выражения в одной строке
    private List<Boolean> getWhereResults(Map<String, Object> where, List<String> signs, Map<String, Object> row) throws Exception {
        List<Boolean> listWhereResults = new ArrayList<>();
        int i = 0;
        // проходим по всем условиям и проверяем правдивость каждого и записываем в listWhereResults
        for (Map.Entry<String, Object> wherePair : where.entrySet()) {
            String sign = signs.get(i);
            Object rowValue = row.get(wherePair.getKey()); // значение в строке
            Object whereValue = wherePair.getValue(); // значение в условии
            if (rowValue == null) nullCheck(sign); // проверка того, что в значении лежит null
            if (sign.equals("=")) {
                if (rowValue == null) {
                    listWhereResults.add(false);
                } else listWhereResults.add(rowValue.equals(whereValue));
            }
            if (sign.equals("!=")) {
                if (rowValue == null) {
                    listWhereResults.add(true);
                } else listWhereResults.add(!rowValue.equals(whereValue));
            }
            if (sign.equals(">")) {
                listWhereResults.add(asDouble(rowValue) > asDouble(whereValue));
            }
            if (sign.equals("<")) {
                listWhereResults.add(asDouble(rowValue) < asDouble(whereValue));
            }
            if (sign.equals(">=")) {
                listWhereResults.add(asDouble(rowValue) >= asDouble(whereValue));
            }
            if (sign.equals("<=")) {
                listWhereResults.add(asDouble(rowValue) <= asDouble(whereValue));
            }
            if (sign.equals("like")) {
                listWhereResults.add(matchesValue(rowValue, whereValue, false));
            }
            if (sign.equals("ilike")) {
                listWhereResults.add(matchesValue(rowValue, whereValue, true));
            }
            i++;
        }
        return listWhereResults;
    }

    // Метод, который проверяет, правдивое ли все WHERE
    private boolean isRowSatisfies(List<Boolean> listWhereResults, List<String> andOrs) {
        boolean whereResult = listWhereResults.get(0);
        // идем по всем И ИЛИ
        for (int i = 0; i < andOrs.size(); i++) {
            if (andOrs.get(i).equals("and")) {
                whereResult = whereResult && listWhereResults.get(i + 1);
            }
            if (andOrs.get(i).equals("or")) {
                whereResult = whereResult || listWhereResults.get(i + 1);
            }
        }
        return whereResult;
    }

    // Метод для операций like ilike
    private boolean matchesValue(Object value, Object pattern, boolean ignoreCase) {
        String regex = pattern.toString().replace("%", ".*"); // % = .* в регулярных выражениях
        String text = value.toString();
        if (ignoreCase) {
            regex = regex.toLowerCase();
            text = text.toLowerCase();
        }
        return text.matches(regex);
    }

    // Метод, возвращающий копию коллекции
    private List<Map<String, Object>> getCollectionCopy() {
        List<Map<String, Object>> copy = new ArrayList<>();
        for (Map<String, Object> originalMap : collection) {
            Map<String, Object> newMap = new HashMap<>(originalMap);
            copy.add(newMap);
        }
        return copy;
    }

    // Метод для каста к double, чтобы производить сравнения
    private Double asDouble(Object o) {
        Double val = null;
        if (o instanceof Number) {
            val = ((Number) o).doubleValue();
        }
        return val;
    }

    public void nullCheck(String sign) throws Exception {
        if (Arrays.asList(">", "<", ">=", "<=", "like", "ilike").contains(sign)) {
            throw new Exception(sign + " нельзя сравнивать с null");
        }
    }
}
