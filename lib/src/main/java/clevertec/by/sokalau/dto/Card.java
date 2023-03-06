package clevertec.by.sokalau.dto;

import java.util.Arrays;

public class Card {

    private int id;
    private String name;
    private String[] simpleArr;

    public Card(){}

    public Card(int id, String name, String[] simpleArr) {
        this.id = id;
        this.name = name;
        this.simpleArr = simpleArr;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", simpleArr=" + Arrays.toString(simpleArr) +
                '}';
    }
}
