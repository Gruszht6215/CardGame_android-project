package th.ac.ku.cardgame.Shop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.util.ArrayList;

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

    public ListShopAdapter(Context context, ArrayList<CardItem> cardsArrayList, User user) {
        super(context, R.layout.list_shop, cardsArrayList);
        this.itemList = cardsArrayList;
        this.user = user;
        this.context = context;
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
                Log.i("vac", "Shop Button: " + getItem(position).toString());
                Log.i("vac", "Shop Button: " + buyButton.getText().toString());
                String cardStatus = buyButton.getText().toString();
                if (cardStatus.matches("Equip")) {
                    EquipItem((int)getItemId(position));
                    ((Activity)context).finish();
                    context.startActivity( ((Activity)context).getIntent());
                } else {
                    if (user.getUserEth().intValue() < getItem(position).getPrice()) {
//                        Log.i("vac", "cant afford: " + user.toString());
                        Toast.makeText(context, "Not enough eth.", Toast.LENGTH_SHORT).show();
                    } else {
                        buyItem(String.valueOf(getItemId(position)));
                        ((Activity)context).finish();
                        context.startActivity( ((Activity)context).getIntent());
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
//                User responseFromAPI = response.body();
                Log.i("vac", "Create Card Equipped: " + response.code());
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

    public void buyItem(String cardId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Shop shop = new Shop(user.getId(), cardId);
        Call<User> call = retrofitAPI.userBuyItem(shop);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User responseFromAPI = response.body();
                Log.i("vac", "Card Buy Success: " + response.code());

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("vac", "Error found is : " + t.getMessage());
            }
        });
    }
}
