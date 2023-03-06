package clevertec.by.sokalau;

import clevertec.by.sokalau.constant.JSONStrings;
import clevertec.by.sokalau.constant.PrimitiveTypesEnum;
import clevertec.by.sokalau.constant.WrappersAndStringEnum;
import clevertec.by.sokalau.dto.Person;
import clevertec.by.sokalau.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static clevertec.by.sokalau.constant.CollectionEnum.LIST;
import static clevertec.by.sokalau.constant.CollectionEnum.MAP;
import static clevertec.by.sokalau.constant.PrimitiveTypesEnum.CHAR;
import static clevertec.by.sokalau.constant.PrimitiveTypesEnum.INT;
import static clevertec.by.sokalau.util.StringUtils.getAnnotatedType;

public class JsonToObject {

    public static void main(String[] args) throws NoSuchFieldException, InstantiationException, IllegalAccessException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException {
        Person p = (Person) parseJSON(JSONStrings.JSON_SIMPLE_DATA, Person.class);
        System.out.println(p);

    }

    public static Object parseJSON(String json, Class<?> clazz) throws
            NoSuchFieldException,
            InstantiationException,
            IllegalAccessException,
            ClassNotFoundException,
            NoSuchMethodException,
            InvocationTargetException
    {
        Field[] dFields = clazz.getDeclaredFields();

        List<FieldData> fieldDataList = generateFieldData(Arrays.stream(dFields).collect(Collectors.toList()), json);
        //fieldDataList.forEach(System.out::println);

        final Object obj = clazz.getConstructor(new Class[]{}).newInstance();
        fieldDataList.forEach(fieldData -> {
            Field field = fieldData.getField();
            field.setAccessible(true);
            try {
                field.set(obj, fieldData.getContent());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        //System.out.println(fields);
        return obj;
    }



    private static List<FieldData> generateFieldData(List<Field> fields, String json) throws
            NoSuchFieldException,
            ClassNotFoundException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException,
            NoSuchMethodException
    {
        Collections.reverse(fields);

        List<FieldData> fieldDataList = new ArrayList<>();
        int currentIndex;
        int previousIndex = 0;

        for(Field field : fields){
            currentIndex = json.indexOf(StringUtils.wrapInBuckets(field.getName()));
            fieldDataList.add(new FieldData(field, currentIndex, previousIndex));
            previousIndex = currentIndex;
        }

        Collections.reverse(fieldDataList);

        fieldDataList.forEach(fieldData ->
                fieldData.setType(fieldData.getField().getType().getTypeName()));

        setContent(fieldDataList, json);
        return fieldDataList;
    }

    private static void setContent(List<FieldData> fieldDataList, String json) throws
            NoSuchFieldException,
            ClassNotFoundException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException,
            NoSuchMethodException {
        for (FieldData fieldData: fieldDataList) {
            final String dataType = fieldData.getType();

            if(WrappersAndStringEnum.getNames().contains(dataType) ||
                    PrimitiveTypesEnum.getNames().contains(dataType)){
                final String regexForPrimitive =
                        "(?:\\\"|\\')(?<key>[\\w\\d]+)(?:\\\"|\\')(?:\\:\\s*)" +
                                "(?:\\\"|\\')?(?<value>[\\w\\s-]*)(?:\\\"|\\')?";
                final Pattern pattern = Pattern.compile(regexForPrimitive, Pattern.MULTILINE);
                parsePrimitive(fieldData, json, pattern);
            } else if(dataType.contains("[]") || LIST.getName().equals(dataType) || MAP.getName().equals(dataType)){
                int value = 1000;

                if(fieldData.getNextFieldIndex() > 0)
                    value = fieldData.getNextFieldIndex() - fieldData.getStartIndex();

                final String regexForArray =
                        "(?:\\\"|\\')(?<key>[\\w\\d]+)(?:\\\"|\\')(?:\\:\\s*)?(?:\\[)(?<value>[\\w\\W]{1,"
                                + value + "})\\]";
                final Pattern pattern = Pattern.compile(regexForArray, Pattern.MULTILINE);

                String type = dataType.replace("[]","");
                if(WrappersAndStringEnum.getNames().contains(type) ||
                        CHAR.getName().equals(type) || INT.getName().equals(type)){
                    parseArrayOfPrimitive(fieldData, json, pattern);
                }
                System.out.println(dataType);
                if(LIST.getName().equals(dataType) || MAP.getName().equals(dataType)){
                    System.out.println("KEY");
                    parseArrayOfObject(fieldData, json, pattern);
                }
            } else {
                int value = 1000;

                if(fieldData.getNextFieldIndex() > 0)
                    value = fieldData.getNextFieldIndex() - fieldData.getStartIndex();
                //OBJECTS parse
                final String regexForObject =
                        "(?:\\\"|\\')(?<key>[\\w\\d]+)(?:\\\"|\\')(?:\\:\\s*)?(?:\\{)(?<value>[\\w\\W]{1,"
                                + value + "})\\}";
                final Pattern pattern = Pattern.compile(regexForObject, Pattern.MULTILINE);
                //System.out.println("OBJECT PARSE:");
                parseObject(fieldData, json, pattern);
            }
        }

    }

    private static void parsePrimitive(FieldData fieldData, String json, Pattern pattern)  {

        Matcher matcher;
        if(fieldData.getNextFieldIndex() > 0) {
            matcher =
                    pattern.matcher(json.substring(fieldData.getStartIndex() - 1, fieldData.getNextFieldIndex() - 1));
        } else {
            matcher =
                    pattern.matcher(json.substring(fieldData.getStartIndex() - 1));
        }
        //int value = 0;
        while (matcher.find()) {
            //fieldData.setNextFieldIndex(fieldData.getNextFieldIndex() - value);
            switch (fieldData.getType()) {
                case "java.lang.Integer", "int" -> fieldData.setContent(Integer.parseInt(matcher.group(2)));
                case "java.lang.Short", "short" -> fieldData.setContent(Short.parseShort(matcher.group(2)));
                case "java.lang.Byte", "byte" -> fieldData.setContent(Byte.parseByte(matcher.group(2)));
                case "java.lang.Long", "long" -> fieldData.setContent(Long.parseLong(matcher.group(2)));
                case "java.lang.Double", "double" -> fieldData.setContent(Double.parseDouble(matcher.group(2)));
                case "java.lang.Float", "float" -> fieldData.setContent(Float.parseFloat(matcher.group(2)));
                case "java.lang.Character", "char" -> fieldData.setContent(matcher.group(2));
                case "java.lang.Boolean", "boolean" -> fieldData.setContent(Boolean.parseBoolean(matcher.group(2)
                                .replace("\n", "")
                                .replace(" ","")));
                case "java.lang.String" -> fieldData.setContent(matcher.group(2));
                default -> {
                }
            }
        }
    }

    private static void parseArrayOfPrimitive(FieldData fieldData, String json, Pattern pattern)  {

        Matcher matcher;
        if(fieldData.getNextFieldIndex() > 0) {
            matcher =
                    pattern.matcher(json.substring(fieldData.getStartIndex() - 1, fieldData.getNextFieldIndex() - 1));
        } else {
            matcher =
                    pattern.matcher(json.substring(fieldData.getStartIndex() - 1));
        }

        while (matcher.find()) {
            String[] values = matcher.group(2)
                    .replace(" ","")
                    .replace("\n","")
                    .split(",");
            switch (fieldData.getType()) {
                case "java.lang.Integer[]" -> fieldData.setContent(Arrays.stream(values)
                        .map(Integer::parseInt)
                        .toList()
                        .toArray(new Integer[values.length]));
                case "java.lang.Short[]" -> fieldData.setContent(Arrays.stream(values)
                        .map(Short::parseShort)
                        .toList()
                        .toArray(new Short[values.length]));
                case "java.lang.Byte[]" -> fieldData.setContent(Arrays.stream(values)
                        .map(Byte::parseByte)
                        .toList()
                        .toArray(new Byte[values.length]));
                case "java.lang.Long[]" -> fieldData.setContent(Arrays.stream(values)
                        .map(Long::parseLong)
                        .toList()
                        .toArray(new Long[values.length]));
                case "java.lang.Double[]" -> fieldData.setContent(Arrays.stream(values)
                        .map(Double::parseDouble)
                        .toList()
                        .toArray(new Double[values.length]));
                case "java.lang.Float[]" -> fieldData.setContent(Arrays.stream(values)
                        .map(Float::parseFloat)
                        .toList()
                        .toArray(new Float[values.length]));
                //case "java.lang.Character[]" -> fieldData.setContent(Arrays.stream(values).toList().toArray(new String[0]));
                case "java.lang.Boolean[]" -> fieldData.setContent(Arrays.stream(values)
                        .map(Boolean::parseBoolean)
                        .toList()
                        .toArray(new Boolean[values.length]));
                case "int[]", "short[]", "byte[]" -> fieldData.setContent(Arrays.stream(values)
                        .mapToInt(Integer::parseInt)
                        .toArray());
                case "long[]" -> fieldData.setContent(Arrays.stream(values)
                        .mapToLong(Long::parseLong)
                        .toArray());
                case "double[]" -> fieldData.setContent(Arrays.stream(values)
                        .mapToDouble(Double::parseDouble)
                        .toArray());
                case "float[]" -> fieldData.setContent(Arrays.stream(values)
                        .mapToDouble(Float::parseFloat)
                        .toArray());
                case "char[]" -> {
                    fieldData.setContent(matcher.group(2)
                            .replace("\n","")
                            .replace(" ","")
                            .replace(",","")
                            .toCharArray());
                    //System.out.println("HERE");
                }
                //case "boolean[]" -> fieldData.setContent(Arrays.stream(values).map(Boolean::parseBoolean).toList().toArray(new Boolean[values.length]));
                case "java.lang.String[]" -> fieldData.setContent(values);
                default -> {
                }
            }
        }
    }


    private static void parseArrayOfObject(FieldData fieldData, String json, Pattern pattern) throws ClassNotFoundException, NoSuchFieldException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        //System.out.println(fieldData.getField().getType().getTypeName());
        //System.out.println(fieldData.getField().getAnnotatedType().getType().getTypeName());
        Matcher matcher;
        if(fieldData.getNextFieldIndex() > 0) {
            matcher =
                    pattern.matcher(json.substring(fieldData.getStartIndex() - 1, fieldData.getNextFieldIndex() - 1));
        } else {
            matcher =
                    pattern.matcher(json.substring(fieldData.getStartIndex() - 1));
        }
        String annotatedType = getAnnotatedType(fieldData.getField().getAnnotatedType().getType().getTypeName());

        String[] objects;
        while (matcher.find()) {

        }
    }

    private static void parseObject(FieldData fieldData, String json, Pattern pattern) throws
            ClassNotFoundException,
            NoSuchFieldException,
            InvocationTargetException,
            InstantiationException,
            IllegalAccessException,
            NoSuchMethodException
    {

        Matcher matcher;
        if(fieldData.getNextFieldIndex() > 0) {
            matcher =
                    pattern.matcher(json.substring(fieldData.getStartIndex() - 1, fieldData.getNextFieldIndex() - 1));
        } else {
            matcher =
                    pattern.matcher(json.substring(fieldData.getStartIndex() - 1));
        }
        System.out.println(fieldData.getField().getType().getTypeName());

        while (matcher.find()) {
            fieldData.setContent(parseJSON(matcher.group(0), Class.forName(fieldData.getField().getType().getName())));
        }
    }
}
