package object;

public class Concert {

    private String name;
    private int cards;

    public Concert(String name, int cards) {
        this.name = name;
        this.cards = cards;
    }

    public String getName() {
        return name;
    }

    public int getCards() {
        return cards;
    }

    public void setCards(int cards) {
        this.cards = cards;
    }
}
