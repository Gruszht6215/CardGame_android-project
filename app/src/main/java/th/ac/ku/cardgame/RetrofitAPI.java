package th.ac.ku.cardgame;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitAPI {
    @POST("/api/login-by-key/{key}")
    Call<User> login(@Path("key") String key);
}
