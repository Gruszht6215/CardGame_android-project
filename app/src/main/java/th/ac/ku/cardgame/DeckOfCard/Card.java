package th.ac.ku.cardgame.DeckOfCard;

import com.google.gson.annotations.Expose;

public class Card {
    @Expose
    String code;
    @Expose
    String image;
    @Expose
    String value;
    @Expose
    String suit;

    public String getCode() {
        return code;
    }

    public String getImage() {
        return image;
    }

    public String getValue() {
        return value;
    }

    public String getSuit() {
        return suit;
    }
}
