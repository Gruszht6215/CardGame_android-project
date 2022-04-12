package th.ac.ku.cardgame;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import th.ac.ku.cardgame.UserModel.Transaction;
import th.ac.ku.cardgame.UserModel.User;


public class HomeActivity extends AppCompatActivity {
    int currentState;
    int countCardOpened = 0;
    Card npcCard;
    Card playerCard;
    Gson gson = new Gson();
    User user;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //get User data from MainActivity
        Intent intent = getIntent();
        String userStr = intent.getStringExtra("user");
        user = gson.fromJson(userStr, User.class);
        String responseString = "Id : " + user.getId()
                + "\n" + "Key : " + user.getKey()
                + "\n" + "ETH : " + user.getUserEth();
        Log.i("vac", responseString);
//        user.initialPlayerEth();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //set userEthTextView
        TextView userEthAmount = findViewById(R.id.userEthTextView);
        userEthAmount.setText("ETH: " + String.valueOf(user.getUserEth()));

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
        EditText betPrice = findViewById(R.id.editTextBet);
        ImageButton npc_card = findViewById(R.id.npc_card);
        ImageButton player_card = findViewById(R.id.player_card);
        Button playButton = findViewById(R.id.play_button);

        if (currentState == 0 ||  betPrice.getText().toString().matches("")) {
            Toast.makeText(HomeActivity.this, "Input your bet first", Toast.LENGTH_SHORT).show();
        } else {
            if (!betPrice.getText().toString().trim().matches("[0-9]+")) {
                Toast.makeText(HomeActivity.this, "Bet price contains Special Characters", Toast.LENGTH_SHORT).show();
            } else {
                if (Integer.parseInt(betPrice.getText().toString().trim()) > Integer.parseInt(String.valueOf(user.getUserEth()))) {
                    Toast.makeText(HomeActivity.this, "Your bet price must be lower than your ETH amount", Toast.LENGTH_LONG).show();
                } else {
                    ConstraintLayout betStat = findViewById(R.id.constraintLayoutBet);
                    npc_card.setVisibility(View.VISIBLE);
                    player_card.setVisibility(View.VISIBLE);
                    playButton.setVisibility(View.INVISIBLE);
                    betStat.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    public void npcCard(View view) throws InterruptedException {
        EditText betPrice = findViewById(R.id.editTextBet);
        ImageButton npcImageButton = findViewById(R.id.npc_card);
        Log.i("vac", "npc");
        if (countCardOpened == 1) {
//            Glide.with(HomeActivity.this).load(npcCard.getImage()).into(npcImageButton);
//            judgment(currentState, betPrice.getText().toString().trim());
        }else {
            cardApi("npc");
            countCardOpened++;
        }
    }

    public void playerCard(View view) throws InterruptedException {
        EditText betPrice = findViewById(R.id.editTextBet);
        ImageButton playerImageButton = findViewById(R.id.player_card);
        Log.i("vac", "player");
        if (countCardOpened == 1) {
//            Glide.with(HomeActivity.this).load(playerCard.getImage()).into(playerImageButton);
//            judgment(currentState, betPrice.getText().toString().trim());
        }else {
            cardApi("player");
            countCardOpened++;
        }
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
                //Card field in Deck is List type.
                Card card = responseFromAPI.getCards().get(0);
                Card card2 = responseFromAPI.getCards().get(1);
//                Log.i("vac", "accept: " + response.toString());
                Log.i("vac", "accept: " + responseFromAPI);

                String responseString = "Response Code : " + response.code()
                        + "\nstatus : " + responseFromAPI.getSuccess()
                        + "\n" + "Cards1 Code: " + card.getCode()
                        + "\n" + "Cards1 Img: " + card.getImage()
                        + "\n" + "Cards1 Suit: " + card.getSuit()
                        + "\n" + "Cards1 Value: " + card.getValue()
                        + "\n" + "Cards2 Code: " + card2.getCode()
                        + "\n" + "Cards2 Img: " + card2.getImage()
                        + "\n" + "Cards2 Suit: " + card2.getSuit()
                        + "\n" + "Cards2 Value: " + card2.getValue();
                Log.i("vac", "accept: " + responseString);

                //Set The Card Img
                ImageButton npcImageButton = findViewById(R.id.npc_card);
                Glide.with(HomeActivity.this).load(card2.getImage()).into(npcImageButton);
                npcCard = card2;
                ImageButton playerImageButton = findViewById(R.id.player_card);
                Glide.with(HomeActivity.this).load(card.getImage()).into(playerImageButton);
                playerCard = card;

                EditText betPrice = findViewById(R.id.editTextBet);
                judgment(currentState, betPrice.getText().toString().trim());
            }

            @Override
            public void onFailure(Call<Deck> call, Throwable t) {
                Log.i("vac", "Error found is : " + t.getMessage());
                Toast.makeText(HomeActivity.this, "Try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void judgment(int currentState, String value) {
        int playerValue;
        int npcValue;
//        Log.i("vac", "Error found is : " + playerCard.getValue());
//        Log.i("vac", "Error found is : " + npcCard.getValue());

        //give the right value to player value
        if (playerCard.getValue().matches("KING")) {
            playerValue = 12;
        } else if (playerCard.getValue().matches("QUEEN")) {
            playerValue = 11;
        } else if (playerCard.getValue().matches("JACK")) {
            playerValue = 10;
        } else if (playerCard.getValue().matches("ACE")) {
            playerValue = 0;
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
        } else if (npcCard.getValue().matches("ACE")) {
            npcValue = 0;
        }else {
            npcValue = Integer.parseInt(npcCard.getValue());
        }

        //Caculate for player predict
        ConstraintLayout progressStat = findViewById(R.id.layoutProgressStatus);
        ConstraintLayout betStat = findViewById(R.id.constraintLayoutBet);
        TextView result = findViewById(R.id.textGameResult);
        TextView userEthAmount = findViewById(R.id.userEthTextView);

        if (playerValue > npcValue && currentState == 1) {
            Log.i("vac", "Game Result : Win");
            result.setText("WIN!");
            user.increasePlayerEth(value);
            progressStat.setVisibility(View.VISIBLE);
            betStat.setVisibility(View.INVISIBLE);
            BigInteger strToBi = new BigInteger(value);
            BigInteger totalEth = user.getUserEth().add(strToBi);
            user.setUserEth(totalEth);
            userEthAmount.setText("ETH: " + String.valueOf(totalEth));
            createTransactionHistory("WITHDRAW", value);
        } else if (playerValue == npcValue && currentState == 2) {
            Log.i("vac", "Game Result : Win");
            result.setText("WIN!");
            user.increasePlayerEth(value);
            progressStat.setVisibility(View.VISIBLE);
            betStat.setVisibility(View.INVISIBLE);
            BigInteger strToBi = new BigInteger(value);
            BigInteger totalEth = user.getUserEth().add(strToBi);
            user.setUserEth(totalEth);
            userEthAmount.setText("ETH: " + String.valueOf(totalEth));
            createTransactionHistory("WITHDRAW", value);
        } else if (playerValue < npcValue && currentState == 3) {
            Log.i("vac", "Game Result : Win");
            result.setText("WIN!");
            user.increasePlayerEth(value);
            progressStat.setVisibility(View.VISIBLE);
            betStat.setVisibility(View.INVISIBLE);
            BigInteger strToBi = new BigInteger(value);
            BigInteger totalEth = user.getUserEth().add(strToBi);
            user.setUserEth(totalEth);
            userEthAmount.setText("ETH: " + String.valueOf(totalEth));
            createTransactionHistory("WITHDRAW", value);

        } else {
            Log.i("vac", "Game Result : Lose");
            result.setText("LOSE!");
            user.decreasePlayerEth(value);
            progressStat.setVisibility(View.VISIBLE);
            betStat.setVisibility(View.INVISIBLE);
            BigInteger strToBi = new BigInteger(value);
            BigInteger totalEth = user.getUserEth().subtract(strToBi);
            user.setUserEth(totalEth);
            userEthAmount.setText("ETH: " + String.valueOf(totalEth));
            createTransactionHistory("DEPOSIT", value);
        }
    }

    public void playAgainBtnClk(View view) {
//        Intent home = getIntent();
//        startActivity(home);
        ConstraintLayout progressStat = findViewById(R.id.layoutProgressStatus);
        ConstraintLayout betStat = findViewById(R.id.constraintLayoutBet);
        ImageButton playerImageButton = findViewById(R.id.player_card);
        ImageButton npcImageButton = findViewById(R.id.npc_card);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        EditText betText = findViewById(R.id.editTextBet);
        Button playButton = findViewById(R.id.play_button);

        betStat.setVisibility(View.VISIBLE);
        progressStat.setVisibility(View.INVISIBLE);
        playerImageButton.setImageResource(R.drawable.card_back_default);
        npcImageButton.setImageResource(R.drawable.card_back_default);
        playerImageButton.setVisibility(View.INVISIBLE);
        npcImageButton.setVisibility(View.INVISIBLE);
        radioGroup.clearCheck();
        betText.getText().clear();
        playButton.setVisibility(View.VISIBLE);
        countCardOpened = 0;
    }

    public void createTransactionHistory(String task, String value) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Call<Transaction> call = retrofitAPI.createTransaction(new Transaction(task, value, "play card game.", Integer.parseInt(user.getId())));
        call.enqueue(new Callback<Transaction>() {
            @Override
            public void onResponse(Call<Transaction> call, Response<Transaction> response) {
//                Transaction responseFromAPI = response.body();
                Log.i("vac", "Create Transaction: " + response.code());
                if (response.code() == 200 || response.code() == 201) {
                    Log.i("vac", "Create Transaction Success: " + response.code());
                } else {
                    Toast.makeText(HomeActivity.this, "Enable to create transaction.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Transaction> call, Throwable t) {
                Log.i("vac", "Error found is : " + t.getMessage());
                Toast.makeText(HomeActivity.this, "Enable to create transaction.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void profileButtonClk(View view) {
        Intent intent = new Intent(this, TransactionActivity.class);
        String jsonInString = gson.toJson(user);
//        Log.i("vac", "json" + jsonInString);
        intent.putExtra("user", jsonInString);
        startActivity(intent);
    }

    public void shopBtnClk(View view) {
        Log.i("vac", "Shop!!!");
        Intent intent = new Intent(this, ShopActivity.class);
        String jsonInString = gson.toJson(user);
//        Log.i("vac", "json" + jsonInString);
        intent.putExtra("user", jsonInString);
        startActivity(intent);
    }
}