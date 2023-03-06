package clevertec.by.sokalau.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum WrappersAndStringEnum {
    INTEGER("java.lang.Integer"),
    BYTE("java.lang.Byte"),
    SHORT("java.lang.Short"),
    LONG("java.lang.Long"),
    CHARACTER("java.lang.Character"),
    FLOAT("java.lang.Float"),
    DOUBLE("java.lang.Double"),
    BOOLEAN("java.lang.Boolean"),
    STRING("java.lang.String");

    private final String name;
    WrappersAndStringEnum(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
    public static List<String> getNames(){
        List<String> names = new ArrayList<>();
        Arrays.stream(values()).forEach(e -> names.add(e.getName()));
        return names;
    }
}
