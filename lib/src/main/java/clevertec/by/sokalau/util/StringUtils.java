package clevertec.by.sokalau.util;

public class StringUtils {

    public static String wrapInBuckets(String str){
        return "\"" + str + "\"";
    }



    public static String primitiveToWrapper(String str) {
        if("char".equals(str)){
            str = "Character";
        }
        if("int".equals(str)){
            str = "Integer";
        }
        return str;
    }

    public static String workWithField(String fieldType){
        if(fieldType.contains(".")){
            int lastIndex = fieldType.lastIndexOf('.');
            fieldType = fieldType.substring(++lastIndex);
        }
        fieldType = primitiveToWrapper(fieldType);

        return fieldType;
    }

    public static String getAnnotatedType(String str){
        int start = str.indexOf("<");
        int end = str.lastIndexOf(">");
        return str.substring(++start,end);
    }
}
