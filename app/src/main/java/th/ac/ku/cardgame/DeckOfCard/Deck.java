package th.ac.ku.cardgame.DeckOfCard;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    @Expose
    String success;
    @Expose
    List<Card> cards;

    public String getSuccess() {
        return success;
    }

    public List<Card> getCards() {
        return cards;
    }
}
