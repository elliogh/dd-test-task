package com.digdes.school;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        JavaSchoolStarter starter = new JavaSchoolStarter();
        try {
//            List<Map<String, Object>> result1 = starter.execute("INSERT VALUES 'lastName' = 'Федоров' , 'id'=3, 'age'=40, 'active'=true");
//            List<Map<String, Object>> result2 = starter.execute("INSERT VALUES 'lastName' = 'Петров' , 'id'=5, 'age'=30, 'active'=false");
//            List<Map<String, Object>> result3 = starter.execute("INSERT VALUES 'lastName' = 'Сидоров' , 'id'=15, 'age'=20, 'active'=true");
//            List<Map<String, Object>> result4 = starter.execute("INSERT VALUES 'lastName' = 'Никитин' , 'id'=14, 'age'=17, 'active'=false");
//
//            List<Map<String, Object>> result5 = starter.execute("UPDATE VALUES 'active'=false, 'cost'=10.1 where 'id'=3");
//            List<Map<String, Object>> result6 = starter.execute("UPDATE VALUES 'active'=false, 'cost'=10.1 where 'id'=5");
//            List<Map<String, Object>> result7 = starter.execute("UPDATE VALUES 'active'=false, 'cost'=10.1 where 'id'=15");
//            List<Map<String, Object>> result8 = starter.execute("UPDATE VALUES 'active'=false, 'cost'=10.1 where 'id'=14");
//            List<Map<String, Object>> result9 = starter.execute("UPDATE VALUES 'active'=false, 'cost'=1.4 where 'id'=17 or 'age'>20");
//
//            //Вставка строки в коллекцию
//            List<Map<String,Object>> result10 = starter.execute("INSERT VALUES 'lastName' = 'Федоров' , 'id'=3, 'age'=40, 'active'=true");
//            //Изменение значения которое выше записывали
//            List<Map<String,Object>> result11 = starter.execute("UPDATE VALUES 'active'=false, 'cost'=10.1 where 'id'=3");
//            //Получение всех данных из коллекции (т.е. в данном примере вернется 1 запись)
//            List<Map<String,Object>> result12 = starter.execute("SELECT WHERE 'age'>=30 and 'lastName' ilike '%Д%'");
//
//            List<Map<String,Object>> result13 = starter.execute("SELECT WHERE 'age'>=30");
//
//            List<Map<String,Object>> result14 = starter.execute("DELETE WHERE 'id'=4");
//
//            List<Map<String,Object>> result15 = starter.execute("DELETE");
//
//            List<Map<String,Object>> result16 = starter.execute("DELETE");
//
//            List<Map<String,Object>> result17 = starter.execute("INSERT VALUES 'lastName' = 'Федоров' , 'id'=3, 'age'=40, 'active'=true");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}