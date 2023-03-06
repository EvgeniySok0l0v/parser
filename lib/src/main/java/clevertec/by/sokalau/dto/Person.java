package clevertec.by.sokalau.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Person implements Serializable {

    private String name;
    private int age;
    private String gender;
    private Boolean bool;

    private Card card;

    private String[] strings;

    private char[] as;

    private List<String> stringList;

    private List<Card> cards;

   public Person() {
    }

    public Person(String name, int age, String gender, Boolean bool, Card card, String[] strings, char[] as, List<String> stringList, List<Card> cards) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.bool = bool;
        this.card = card;
        this.strings = strings;
        this.as = as;
        this.stringList = stringList;
        this.cards = cards;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }

    public List<Card> getCards() {
        return cards;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", bool=" + bool +
                ", card=" + card +
                ", strings=" + Arrays.toString(strings) +
                ", as=" + Arrays.toString(as) +
                ", stringList=" + stringList +
                ", cards=" + cards +
                '}';
    }
}
