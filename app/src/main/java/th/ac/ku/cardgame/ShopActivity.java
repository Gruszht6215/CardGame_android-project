package th.ac.ku.cardgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

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

                ArrayList<CardItem> cardItemArrayList = new ArrayList<>();

                for(CardItem cardItem : cardItems) {
                    CardItem item = new CardItem(cardItem.getId(), cardItem.getName(), cardItem.getPrice());
                    cardItemArrayList.add(item);
                }

                ListView ls = findViewById(R.id.listviewShop);
                ListShopAdapter listAdapter = new ListShopAdapter(ShopActivity.this,cardItemArrayList);
                ls.setAdapter(listAdapter);
            }

            @Override
            public void onFailure(Call<List<CardItem>> call, Throwable t) {
                Toast.makeText(ShopActivity.this, "Enable to load Item.", Toast.LENGTH_SHORT).show();
            }
        });

        /*Intent intent = getIntent();
        String userStr = intent.getStringExtra("user");
        user = gson.fromJson(userStr, User.class);

        List<CardItem> cardItems = user.getCards();
        ArrayList<CardItem> shopArrayList = new ArrayList<>();

        for (CardItem cardItem : cardItems) {
            CardItem card = new CardItem(cardItem.getId(), cardItem.getName(), cardItem.getPrice());
            shopArrayList.add(card);
        }
//        CardItem card = new CardItem("1", "fire", 150);
//        shopArrayList.add(card);


        ListView ls = findViewById(R.id.listviewShop);
        ListShopAdapter listAdapter = new ListShopAdapter(ShopActivity.this,shopArrayList);
        ls.setAdapter(listAdapter);*/
    }

//    public void buyBtnClk(View view) {
//        Log.i("vac", "Buy!!!");
//    }

}