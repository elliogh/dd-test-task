package com.digdes.school;

import com.digdes.school.commands.Command;

import java.util.List;
import java.util.Map;

public class JavaSchoolStarter {
    private final CollectionManager collectionManager = new CollectionManager();

    //Дефолтный конструктор
    public JavaSchoolStarter() {

    }

    //На вход запрос, на выход результат выполнения запроса
    public List<Map<String, Object>> execute(String request) throws Exception {
        Parser parser = new Parser();
        Command command = parser.parseCommand(request);

        return command.execute(collectionManager);
    }
}
