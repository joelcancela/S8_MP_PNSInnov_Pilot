package unice.polytech.si4.pnsinnov.teamm.drools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataBase {
    private DataBase() {
    }

    private static Map<String, List<String>> INSTANCE = new HashMap<>();

    public static Map<String, List<String>> getInstance() {
        return INSTANCE;
    }
}
