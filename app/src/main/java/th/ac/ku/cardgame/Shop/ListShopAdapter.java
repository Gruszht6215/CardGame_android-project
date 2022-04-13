package th.ac.ku.cardgame.Shop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import th.ac.ku.cardgame.HomeActivity;
import th.ac.ku.cardgame.MainActivity;
import th.ac.ku.cardgame.R;
import th.ac.ku.cardgame.RetrofitAPI;
import th.ac.ku.cardgame.ShopActivity;
import th.ac.ku.cardgame.TransactionActivity;
import th.ac.ku.cardgame.UserModel.Transaction;
import th.ac.ku.cardgame.UserModel.User;

public class ListShopAdapter extends ArrayAdapter<CardItem> implements ListAdapter {
    public static String PACKAGE_NAME;
    private ArrayList<CardItem> itemList = new ArrayList<CardItem>();
    private User user;
    private Context context;
    private String currentUserEth;

    public ListShopAdapter(Context context, ArrayList<CardItem> cardsArrayList, User user) {
        super(context, R.layout.list_shop, cardsArrayList);
        this.itemList = cardsArrayList;
        this.user = user;
        this.context = context;
        this.currentUserEth = String.valueOf(user.getUserEth());
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public CardItem getItem(int pos) {
        return itemList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return Integer.parseInt(itemList.get(pos).getId());
        //just return 0 if your list items do not have an Id variable.
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CardItem card = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_shop,parent,false);
        }

        ImageView cardImg = convertView.findViewById(R.id.cardImage);
        TextView cardName = convertView.findViewById(R.id.cardName);
        Button buyButton = convertView.findViewById(R.id.buyButton);

        //setup for set image from string
        Resources res = getContext().getResources();
        String cardDrawableName = "card_back_" + card.name;
        PACKAGE_NAME = getContext().getPackageName();
        int resID = res.getIdentifier(cardDrawableName , "drawable", PACKAGE_NAME);
        Drawable drawable = res.getDrawable(resID );
        cardImg.setImageDrawable(drawable);

        String firstLetterUppercase = card.getName().substring(0, 1).toUpperCase() + card.getName().substring(1);
        cardName.setText(firstLetterUppercase + " card");

        if (card.getPrice() == 0) {
            buyButton.setText("Equip");

        } else if (card.getPrice() == -1){
            buyButton.setText("Using");
            buyButton.setEnabled(false);
        } else {
            buyButton.setText("$" + String.valueOf(card.getPrice()));
        }

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.i("vac", "Shop Button: " + getItem(position).toString());
//                Log.i("vac", "Shop Button: " + buyButton.getText().toString());
                String cardStatus = buyButton.getText().toString();
                if (cardStatus.matches("Equip")) {
                    EquipItem((int)getItemId(position));
                    ((Activity)context).finish();
                    context.startActivity( ((Activity)context).getIntent());
                } else {
                    if (Integer.parseInt(currentUserEth) < getItem(position).getPrice()) {
//                        Log.i("vac", "cant afford: " + user.toString());
                        Toast.makeText(context, "Not enough eth.", Toast.LENGTH_SHORT).show();
                    } else {
                        buyItem(String.valueOf(getItemId(position)), String.valueOf(getItem(position).getPrice()));
                        ((Activity)context).finish();
                        context.startActivity( ((Activity)context).getIntent());
//                        TextView userEthAmount = findViewById(R.id.userEthShopTextView);

                    }
                }
            }
        });

        return convertView;
    }

    public void EquipItem(int card_id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        user.setUsing_card_id(card_id);
        Call<User> call = retrofitAPI.userEquipCard(user, user.getId());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User responseFromAPI = response.body();
                Log.i("vac", "Create Card Equipped: " + response.code());
                user.setUsing_card_id(responseFromAPI.getUsing_card_id());
//                user = responseFromAPI;
//                Intent intent = new Intent(context, ShopActivity.class);
//                Gson gson = new Gson();
//                String jsonInString = gson.toJson(user);
//                intent.putExtra("user", jsonInString);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("vac", "Error found is : " + t.getMessage());
            }
        });
    }

    public void buyItem(String cardId, String price) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Shop shop = new Shop(user.getId(), cardId);
        Call<User> call = retrofitAPI.userBuyItem(shop);
        call.enqueue(new Callback<User>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User responseFromAPI = response.body();
                Log.i("vac", "Card Buy Success: " + response.code());
                responseFromAPI.decreasePlayerEth(price);
                int totalEth = Integer.parseInt(currentUserEth) - Integer.parseInt(price);
                currentUserEth = String.valueOf(totalEth);
                Log.i("vac", "Current ETH: " + currentUserEth);
//                TextView uEth = ((ShopActivity)context).findViewById(R.id.userEthShopTextView);
//                uEth.setText(currentUserEth);

                BigInteger result = new BigInteger(currentUserEth);
                user.setUserEth(result);
                Gson gson = new Gson();
                Intent intent = new Intent(context, ShopActivity.class);
                String jsonInString = gson.toJson(user);
                Log.i("vac", "Json" + jsonInString);
                intent.putExtra("user", jsonInString);
//                ((ShopActivity)context).finish();
                if (context instanceof ShopActivity) {
//                    Log.i("vac", "Work?");
                    ((ShopActivity)context).setUserEth(currentUserEth);
                }
                ((ShopActivity)context).startActivity(intent);
                createTransactionHistory("WITHDRAW", price);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("vac", "Error found is : " + t.getMessage());
            }
        });
    }

    public void createTransactionHistory(String task, String value) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Call<Transaction> call = retrofitAPI.createTransaction(new Transaction(task, value, "buy a card.", Integer.parseInt(user.getId())));
        call.enqueue(new Callback<Transaction>() {
            @Override
            public void onResponse(Call<Transaction> call, Response<Transaction> response) {
//                Transaction responseFromAPI = response.body();
                Log.i("vac", "Create Transaction: " + response.code());
                if (response.code() == 200 || response.code() == 201) {
                    Log.i("vac", "Create Transaction Success: " + response.code());
                } else {
                    Log.i("vac", "Enable to create transaction: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Transaction> call, Throwable t) {
                Log.i("vac", "Error found is : " + t.getMessage());
            }
        });
    }
}
