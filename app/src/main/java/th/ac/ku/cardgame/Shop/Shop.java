package th.ac.ku.cardgame.Shop;

public class Shop {
    String wallet_id;
    String card_id;

    public Shop(String wallet_id, String card_id) {
        this.wallet_id = wallet_id;
        this.card_id = card_id;
    }

    public String getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(String wallet_id) {
        this.wallet_id = wallet_id;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }
}
