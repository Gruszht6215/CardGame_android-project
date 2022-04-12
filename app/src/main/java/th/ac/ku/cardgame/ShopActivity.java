package th.ac.ku.cardgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import th.ac.ku.cardgame.Shop.CardItem;
import th.ac.ku.cardgame.Shop.ListShopAdapter;
import th.ac.ku.cardgame.UserModel.ListTransactionAdapter;
import th.ac.ku.cardgame.UserModel.Transaction;
import th.ac.ku.cardgame.UserModel.User;

public class ShopActivity extends AppCompatActivity {
    Gson gson = new Gson();
    User user;
    BigInteger tempUserEth;
    ProgressBar progressBar;
    TextView userEthAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        progressBar = findViewById(R.id.progressBarShop);
        progressBar.setVisibility(View.VISIBLE);
        userEthAmount = findViewById(R.id.userEthShopTextView);

        Intent intent = getIntent();
        String userStr = intent.getStringExtra("user");
        user = gson.fromJson(userStr, User.class);

//        user.initialPlayerEth();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tempUserEth = user.getUserEth();
        userEthAmount.setText("ETH: " + String.valueOf(user.getUserEth()));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Call<User> call = retrofitAPI.getWallet(user.getId());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User responseFromAPI = response.body();
                Log.i("vac", "Create Card Equipped: " + response.code());
                user = responseFromAPI;
                progressBar.setVisibility(View.GONE);
                setShopItem();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("vac", "Error found is : " + t.getMessage());
            }
        });
    }

    public void setShopItem() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Call<List<CardItem>> call = retrofitAPI.getCardItem();
        call.enqueue(new Callback<List<CardItem>>() {
            @Override
            public void onResponse(Call<List<CardItem>> call, Response<List<CardItem>> response) {
                List<CardItem> cardItems = response.body();
                List<CardItem> ownedCardItems = user.getCards();

                ArrayList<CardItem> cardItemArrayList = new ArrayList<>();

                for(CardItem cardItem : cardItems) {
                    CardItem item = new CardItem(cardItem.getId(), cardItem.getName(), cardItem.getPrice());

                    for (CardItem ownedCardItem : ownedCardItems) {
                        if (ownedCardItem.getId().matches(cardItem.getId())) {
                            item = new CardItem(cardItem.getId(), cardItem.getName(), 0);
                            if (ownedCardItem.getId().matches(String.valueOf(user.getUsing_card_id()))) {
                                item = new CardItem(cardItem.getId(), cardItem.getName(), -1);
                            }
                        }
                    }
                    cardItemArrayList.add(item);
                }

                ListView ls = findViewById(R.id.listviewShop);
                user.setUserEth(tempUserEth);
                Log.i("vac", "cant afford: " + user.toString());
                ListShopAdapter listAdapter = new ListShopAdapter(ShopActivity.this, cardItemArrayList, user);
                ls.setAdapter(listAdapter);
            }

            @Override
            public void onFailure(Call<List<CardItem>> call, Throwable t) {
                Toast.makeText(ShopActivity.this, "Enable to load Item.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setUserEth(String amountEth) {
        userEthAmount.setText("ETH: " + amountEth);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        String jsonInString = gson.toJson(user);
        intent.putExtra("user", jsonInString);
        finish();
        startActivity(intent);
    }
}