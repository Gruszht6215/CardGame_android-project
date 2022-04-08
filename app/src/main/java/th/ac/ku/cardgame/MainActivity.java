package th.ac.ku.cardgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    int status = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void joinBtn(View view) {
        Intent intent = new Intent(this, HomeActivity.class);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        EditText key = findViewById(R.id.private_key);
        String key_check = key.getText().toString();
        if (key_check.matches("")){
//            Log.i("vac", key.getText().toString());
            Toast.makeText(MainActivity.this, "Wallet key is required", Toast.LENGTH_SHORT).show();
        }else {
            Call<User> call = retrofitAPI.login(key.getText().toString());
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User responseFromAPI = response.body();

                    String responseString = "Response Code : " + response.code()
                            + "\nId : " + responseFromAPI.getId()
                            + "\n" + "Key : " + responseFromAPI.getKey();
                    status = response.code();
                    Log.i("vac", "accept: " + responseString);
                    if (status == 200 || status == 201) {
                        Gson gson = new Gson();
                        String jsonInString = gson.toJson(responseFromAPI);
                        intent.putExtra("user", jsonInString);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.i("vac", "Error found is : " + t.getMessage());
                    Toast.makeText(MainActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}