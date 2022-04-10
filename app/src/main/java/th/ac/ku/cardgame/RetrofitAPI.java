package th.ac.ku.cardgame;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import th.ac.ku.cardgame.DeckOfCard.Deck;
import th.ac.ku.cardgame.UserModel.User;

public interface RetrofitAPI {
    @POST("/api/login-by-key/{key}")
    Call<User> login(@Path("key") String key);

    @GET("/api/deck/new/draw/?count=2")
    Call<Deck> getCard();

//    @GET("/api/deck/new/draw/?count=1")
//    Call<List<Deck>> getCard();
}
