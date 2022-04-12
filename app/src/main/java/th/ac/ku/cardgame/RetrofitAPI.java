package th.ac.ku.cardgame;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import th.ac.ku.cardgame.DeckOfCard.Deck;
import th.ac.ku.cardgame.Shop.CardItem;
import th.ac.ku.cardgame.Shop.Shop;
import th.ac.ku.cardgame.UserModel.Transaction;
import th.ac.ku.cardgame.UserModel.User;

public interface RetrofitAPI {
    @POST("/api/login-by-key/{key}")
    Call<User> login(@Path("key") String key);

    @GET("/api/deck/new/draw/?count=2")
    Call<Deck> getCard();

    @GET("/api/find-by-wallet-id/{user_id}")
    Call<List<Transaction>> getUserTransaction(@Path("user_id") String user_id);

    @POST("/api/transactions/")
    Call<Transaction> createTransaction(@Body Transaction transaction);

    @GET("/api/cards/")
    Call<List<CardItem>> getCardItem();

    @PUT("/api/wallets/{card_id}")
    Call<User> userEquipCard(@Body User user, @Path("card_id")String card_id );

    @GET("/api/wallets/{id}")
    Call<User> getWallet(@Path("id")String id);

    @POST("/api/user-buy-card/")
    Call<User> userBuyItem(@Body Shop shop);
}
