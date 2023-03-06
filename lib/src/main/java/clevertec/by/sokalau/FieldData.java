package clevertec.by.sokalau;

import java.lang.reflect.Field;

public class FieldData {

    private Field field;
    private int startIndex;
    private int nextFieldIndex;

    private String type;
    private Object content;

    public FieldData(Field field, int startIndex, int nextFieldIndex) {
        this.field = field;
        this.startIndex = startIndex;
        this.nextFieldIndex = nextFieldIndex;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getNextFieldIndex() {
        return nextFieldIndex;
    }

    public void setNextFieldIndex(int nextFieldIndex) {
        this.nextFieldIndex = nextFieldIndex;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "FieldData{" +
                "field=" + field +
                ", startIndex=" + startIndex +
                ", nextFieldIndex=" + nextFieldIndex +
                ", type='" + type + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
