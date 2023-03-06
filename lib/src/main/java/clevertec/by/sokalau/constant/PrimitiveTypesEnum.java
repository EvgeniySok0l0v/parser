package clevertec.by.sokalau.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum PrimitiveTypesEnum {

    INT("int"),
    SHORT("short"),
    BYTE("byte"),
    LONG("long"),
    CHAR("char"),
    FLOAT("float"),
    DOUBLE("double"),
    BOOLEAN("boolean");
    private final String name;
    PrimitiveTypesEnum(String name){
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
