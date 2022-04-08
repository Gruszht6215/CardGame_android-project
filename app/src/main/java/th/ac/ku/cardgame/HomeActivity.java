package th.ac.ku.cardgame;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.math.BigInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import th.ac.ku.cardgame.DeckOfCard.Card;
import th.ac.ku.cardgame.DeckOfCard.Deck;


public class HomeActivity extends AppCompatActivity {
    int currentState;
    Card npcCard;
    Card playerCard;
    Gson gson = new Gson();
    User user;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        TextView userEthAmount = findViewById(R.id.userEthTextView);

        //get User data from MainActivity
        Intent intent = getIntent();
        String userStr = intent.getStringExtra("user");
        user = gson.fromJson(userStr, User.class);
        String responseString = "Id : " + user.getId()
                + "\n" + "Key : " + user.getKey();
        Log.i("vac", responseString);
        user.retrievePlayerEth();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //set userEthTextView
        TextView userEthAmount = findViewById(R.id.userEthTextView);
        userEthAmount.setText("ETH: " + String.valueOf(user.userEth));

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                View radioButton = radioGroup.findViewById(i);
                int index = radioGroup.indexOfChild(radioButton);
                Log.i("vac", "index of radio"+index);

                int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                if (checkedRadioButtonId == -1) {
                    Log.i("vac", "nothing");
                    currentState = 0;
                }
                else{
                    if (checkedRadioButtonId == R.id.high) {
                        Log.i("vac", "high");
                        currentState = 1;
                    } else if (checkedRadioButtonId == R.id.draw){
                        Log.i("vac", "draw");
                        currentState = 2;
                    } else if (checkedRadioButtonId == R.id.low){
                        Log.i("vac", "low");
                        currentState = 3;
                    }
                }
            }
        });
    }

    public void playBtn(View view) {
        Log.i("vac", "play");
//        ImageButton npc_card = findViewById(R.id.npc_card);
//        ImageButton player_card = findViewById(R.id.player_card);
//        Button playButton = findViewById(R.id.play_button);
//
//        npc_card.setVisibility(View.VISIBLE);
//        player_card.setVisibility(View.VISIBLE);
//        playButton.setVisibility(View.INVISIBLE);

//        BigInteger b = user.retrievePLayerEth();
//        Log.i("vac", "play"+ b);
//        user.increasePlayerEth("122");
    }

    public void npcCard(View view) {
        cardApi("npc");
        Log.i("vac", "npc");
    }

    public void playerCard(View view) {
        cardApi("player");
        Log.i("vac", "player");
    }

    public void cardApi(String side) {
        Log.i("vac", "Call me");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://deckofcardsapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Call<Deck> call = retrofitAPI.getCard();
        call.enqueue(new Callback<Deck>() {
            @Override
            public void onResponse(Call<Deck> call, Response<Deck> response) {
                Deck responseFromAPI = response.body();
                Card card = responseFromAPI.getCards().get(0);
                Log.i("vac", "accept: " + response.toString());
                Log.i("vac", "accept: " + responseFromAPI);


                String responseString = "Response Code : " + response.code()
                        + "\nstatus : " + responseFromAPI.getSuccess()
                        + "\n" + "Cards Code: " + card.getCode()
                        + "\n" + "Cards Img: " + card.getImage()
                        + "\n" + "Cards Suit: " + card.getSuit()
                        + "\n" + "Cards Value: " + card.getValue();
                Log.i("vac", "accept: " + responseString);

                //Set The Card Img
                if (side.matches("player")){
                    playerCard = card;
                    ImageButton npcImageButton = findViewById(R.id.player_card);
                    Glide.with(HomeActivity.this).load(card.getImage()).into(npcImageButton);
                } else {
                    npcCard = card;
                    ImageButton playerImageButton = findViewById(R.id.npc_card);
                    Glide.with(HomeActivity.this).load(card.getImage()).into(playerImageButton);
                }
            }

            @Override
            public void onFailure(Call<Deck> call, Throwable t) {
                Log.i("vac", "Error found is : " + t.getMessage());
                Toast.makeText(HomeActivity.this, "Try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void judgment(int currentState, BigInteger value) {
        int stateResult;
        int playerValue;
        int npcValue;
        //give the right value to player value
        if (playerCard.getValue().matches("KING")) {
            playerValue = 12;
        } else if (playerCard.getValue().matches("QUEEN")) {
            playerValue = 11;
        } else if (playerCard.getValue().matches("JACK")) {
            playerValue = 10;
        } else {
            playerValue = Integer.parseInt(playerCard.getValue());
        }
        //give the right value to npc value
        if (npcCard.getValue().matches("KING")) {
            npcValue = 12;
        } else if (npcCard.getValue().matches("QUEEN")) {
            npcValue = 11;
        } else if (npcCard.getValue().matches("JACK")) {
            npcValue = 10;
        } else {
            npcValue = Integer.parseInt(npcCard.getValue());
        }

        //Caculate for player predict
        if (playerValue > npcValue && currentState == 1) {

        } else if (playerValue == npcValue && currentState == 2) {

        } else if (playerValue < npcValue && currentState == 3) {

        }
    }
}