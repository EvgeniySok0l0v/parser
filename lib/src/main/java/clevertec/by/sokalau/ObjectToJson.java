package clevertec.by.sokalau;

import clevertec.by.sokalau.constant.CollectionEnum;
import clevertec.by.sokalau.constant.PrimitiveTypesEnum;
import clevertec.by.sokalau.constant.WrappersAndStringEnum;
import clevertec.by.sokalau.dto.Card;
import clevertec.by.sokalau.dto.Person;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static clevertec.by.sokalau.constant.WrappersAndStringEnum.STRING;
import static clevertec.by.sokalau.util.StringUtils.getAnnotatedType;

public class ObjectToJson {

    public static void main(String[] args) throws IllegalAccessException, ClassNotFoundException {
        Person person = new Person(
                "defName",
                11,
                "defMale",
                true,
                new Card(
                        11,
                        "defCard",
                        new String[]{"defCardStr1", "defCardStr2"}),
                new String[]{"defStr1","defStr2"},
                new char[]{'1', '2', 'c'},
                List.of("defListStr1", "defListStr2"),
                List.of(
                        new Card(
                                12,
                                "defCard1",
                                new String[]{"defCardStr12", "defCardStr22"}),
                        new Card(
                                13,
                                "defCard2",
                                new String[]{"defCardStr13", "defCardStr23"})
                )
        );
        System.out.println(objectToJson(person));
    }
    public static String objectToJson(Object obj) throws IllegalAccessException, ClassNotFoundException {
        StringBuilder jsonBuilder  = new StringBuilder("{\n");

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field: fields){
            field.setAccessible(true);
            String type = field.getType().getTypeName();
            if(type.equals(STRING.getName())){
                jsonBuilder
                        .append("\"")
                        .append(field.getName())
                        .append("\"")
                        .append(": ")
                        .append("\"")
                        .append(field.get(obj))
                        .append("\"");

            } else if (WrappersAndStringEnum.getNames().contains(type)
                    || PrimitiveTypesEnum.getNames().contains(type)) {
                jsonBuilder
                        .append("\"")
                        .append(field.getName())
                        .append("\"")
                        .append(": ")
                        .append(field.get(obj));

            } else if (type.contains("[]")){
                simpleArrayToJson(field, jsonBuilder, obj);
            } else if (CollectionEnum.getNames().contains(type)) {
                if(type.equals(CollectionEnum.LIST.getName())){
                    if(WrappersAndStringEnum.getNames().contains(getAnnotatedType(field.getAnnotatedType().getType().getTypeName()))){
                        simpleListToJson(field, jsonBuilder, obj);
                    } else {
                        listOfObjectsToJson(field, jsonBuilder, obj);
                    }
                }
            } else {
                jsonBuilder
                        .append("\"")
                        .append(field.getName())
                        .append("\"")
                        .append(": ")
                        .append(objectToJson(field.get(obj)));

            }
            jsonBuilder
                    .append(",")
                    .append("\n");
        }
        jsonBuilder.deleteCharAt(jsonBuilder.lastIndexOf(","));
        return jsonBuilder.append("}").toString();
    }

    private static void simpleArrayToJson(Field field, StringBuilder jsonBuilder, Object obj) throws IllegalAccessException {
        jsonBuilder
                .append("\"")
                .append(field.getName())
                .append("\"")
                .append(": ")
                .append("[\n");
        switch (field.getType().getTypeName()){
            case "java.lang.Integer[]" -> {
                Integer[] arr = (Integer[]) field.get(obj);
                wrapperArrToJson(arr, jsonBuilder);
            }
            case "int[]" -> {
                int[] arr = (int[]) field.get(obj);
                intLongDoubleArrToJson(arr, jsonBuilder);
            }
            case "java.lang.Byte[]" -> {
                Byte[] arr = (Byte[]) field.get(obj);
                wrapperArrToJson(arr, jsonBuilder);
            }
            case "byte[]" -> {
                byte[] arr = (byte[]) field.get(obj);
                byteArrToJson(arr, jsonBuilder);
            }
            case "java.lang.Short[]" -> {
                Short[] arr = (Short[]) field.get(obj);
                wrapperArrToJson(arr, jsonBuilder);
            }
            case "short[]" -> {
                short[] arr = (short[]) field.get(obj);
                shortArrToJson(arr, jsonBuilder);
            }
            case "java.lang.Long[]" -> {
                Long[] arr = (Long[]) field.get(obj);
                wrapperArrToJson(arr, jsonBuilder);
            }
            case "long[]" -> {
                long[] arr = (long[]) field.get(obj);
                intLongDoubleArrToJson(arr, jsonBuilder);
            }
            case "java.lang.Double[]" -> {
                Double[] arr = (Double[]) field.get(obj);
                wrapperArrToJson(arr, jsonBuilder);
            }
            case "double[]" -> {
                double[] arr = (double[]) field.get(obj);
                intLongDoubleArrToJson(arr, jsonBuilder);
            }
            case "java.lang.Float[]" -> {
                Float[] arr = (Float[]) field.get(obj);
                wrapperArrToJson(arr, jsonBuilder);
            }
            case "float[]" -> {
                float[] arr = (float[]) field.get(obj);
                floatArrToJson(arr, jsonBuilder);
            }
            case "java.lang.Character[]" -> {
                Character[] arr = (Character[]) field.get(obj);
                wrapperArrToJson(arr, jsonBuilder);
            }
            case "char[]" -> {
                char[] arr = (char[]) field.get(obj);
                charArrToJson(arr, jsonBuilder);
            }
            case "java.lang.Boolean[]" -> {
                Boolean[] arr = (Boolean[]) field.get(obj);
                wrapperArrToJson(arr, jsonBuilder);
            }
            case "boolean[]" -> {
                boolean[] arr = (boolean[]) field.get(obj);
                booleanArrToJson(arr, jsonBuilder);
            }
            case "java.lang.String[]" -> {
                String[] arr = (String[]) field.get(obj);
                wrapperArrToJson(arr, jsonBuilder);
            }
            default -> {
            }
        }
        jsonBuilder
                .append("]");

    }

    private static void simpleListToJson(Field field, StringBuilder jsonBuilder, Object obj) throws IllegalAccessException {
        jsonBuilder
                .append("\"")
                .append(field.getName())
                .append("\"")
                .append(": ")
                .append("[\n");
        listToJson((List<?>) field.get(obj), jsonBuilder);
        jsonBuilder
                .append("]");

    }

    private static void listOfObjectsToJson(Field field, StringBuilder jsonBuilder, Object obj) throws IllegalAccessException {
        jsonBuilder
                .append("\"")
                .append(field.getName())
                .append("\"")
                .append(": ")
                .append("[\n");
        objectsToJson((List<?>) field.get(obj), jsonBuilder);
        jsonBuilder
                .append("]\n");
    }

    private static void listToJson(List<?> list, StringBuilder jsonBuilder){
        list.forEach(v ->
                    jsonBuilder
                            .append("\"")
                            .append(v)
                            .append("\"")
                            .append(",\n"));
        jsonBuilder.deleteCharAt(jsonBuilder.lastIndexOf(","));
    }

    private static void objectsToJson(List<?> list, StringBuilder jsonBuilder){
        list.forEach(v ->
        {
            try {
                jsonBuilder
                        .append(objectToJson(v))
                        .append(",\n");
            } catch (IllegalAccessException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        jsonBuilder.deleteCharAt(jsonBuilder.lastIndexOf(","));
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static void intLongDoubleArrToJson(int[] arr, StringBuilder jsonBuilder) {
        Arrays.stream(arr)
                .forEach(v -> jsonBuilder
                        .append("\"")
                        .append(v)
                        .append("\"")
                        .append(",\n"));
        jsonBuilder.deleteCharAt(jsonBuilder.lastIndexOf(","));
    }
    private static void intLongDoubleArrToJson(long[] arr, StringBuilder jsonBuilder) {
        Arrays.stream(arr)
                .forEach(v -> jsonBuilder
                        .append("\"")
                        .append(v)
                        .append("\"")
                        .append(",\n"));
        jsonBuilder.deleteCharAt(jsonBuilder.lastIndexOf(","));
    }

    private static void intLongDoubleArrToJson(double[] arr, StringBuilder jsonBuilder) {
        Arrays.stream(arr)
                .forEach(v -> jsonBuilder
                        .append("\"")
                        .append(v)
                        .append("\"")
                        .append(",\n"));
        jsonBuilder.deleteCharAt(jsonBuilder.lastIndexOf(","));
    }

    private static void floatArrToJson(float[] arr, StringBuilder jsonBuilder) {
        for(float f: arr){
            jsonBuilder
                    .append("\"")
                    .append(f)
                    .append("\"")
                    .append(",\n");
        }
        jsonBuilder.deleteCharAt(jsonBuilder.lastIndexOf(","));
    }

    private static void booleanArrToJson(boolean[] arr, StringBuilder jsonBuilder) {
        for(boolean b: arr){
            jsonBuilder
                    .append("\"")
                    .append(b)
                    .append("\"")
                    .append(",\n");
        }
        jsonBuilder.deleteCharAt(jsonBuilder.lastIndexOf(","));
    }
    private static void charArrToJson(char[] arr, StringBuilder jsonBuilder) {
        for(char c: arr){
            jsonBuilder
                    .append("'")
                    .append(c)
                    .append("'")
                    .append(",\n");
        }
        jsonBuilder.deleteCharAt(jsonBuilder.lastIndexOf(","));
    }

    private static void byteArrToJson(byte[] arr, StringBuilder jsonBuilder) {
       for(byte b: arr){
           jsonBuilder
                   .append("\"")
                   .append(b)
                   .append("\"")
                   .append(",\n");
       }
        jsonBuilder.deleteCharAt(jsonBuilder.lastIndexOf(","));
    }

    private static void shortArrToJson(short[] arr, StringBuilder jsonBuilder) {
        for(short s: arr){
            jsonBuilder
                    .append("\"")
                    .append(s)
                    .append("\"")
                    .append(",\n");
        }
        jsonBuilder.deleteCharAt(jsonBuilder.lastIndexOf(","));
    }

    private static void wrapperArrToJson(Object[] arr, StringBuilder jsonBuilder){
        Arrays.stream(arr)
                .forEach(v -> jsonBuilder
                        .append("\"")
                        .append(v)
                        .append("\"")
                        .append(",\n"));
        jsonBuilder.deleteCharAt(jsonBuilder.lastIndexOf(","));
    }
}
