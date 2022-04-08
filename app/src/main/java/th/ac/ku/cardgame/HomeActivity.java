package th.ac.ku.cardgame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                View radioButton = radioGroup.findViewById(i);
                int index = radioGroup.indexOfChild(radioButton);
                Log.i("vac", "index og radio"+index);

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
        ImageButton npc_card = findViewById(R.id.npc_card);
        ImageButton player_card = findViewById(R.id.player_card);
        Button playButton = findViewById(R.id.play_button);

        npc_card.setVisibility(View.VISIBLE);
        player_card.setVisibility(View.VISIBLE);
        playButton.setVisibility(View.INVISIBLE);


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
}