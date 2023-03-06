package clevertec.by.sokalau.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public enum CollectionEnum {

    LIST("java.util.List"),
    MAP("java.util.Map");

    private final String name;
    CollectionEnum(String name){
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
