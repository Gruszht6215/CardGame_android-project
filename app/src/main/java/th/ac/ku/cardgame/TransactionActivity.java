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
import th.ac.ku.cardgame.UserModel.ListTransactionAdapter;
import th.ac.ku.cardgame.UserModel.Transaction;
import th.ac.ku.cardgame.UserModel.User;
import th.ac.ku.cardgame.databinding.ActivityMainBinding;

public class TransactionActivity extends AppCompatActivity {
    Gson gson = new Gson();
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        Intent intent = getIntent();
        String userStr = intent.getStringExtra("user");
        user = gson.fromJson(userStr, User.class);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Call<List<Transaction>> call = retrofitAPI.getUserTransaction(user.getId());
        call.enqueue(new Callback<List<Transaction>>() {
            @Override
            public void onResponse(Call<List<Transaction>> call, Response<List<Transaction>> response) {
                List<Transaction> transactions = response.body();

                ArrayList<Transaction> transactionArrayList = new ArrayList<>();

                for(Transaction t : transactions){
                    Transaction transaction = new Transaction(t.getCreated_at(), t.getTask(), t.getValue()+"eth", t.getDetail(), Integer.parseInt(user.getId()));
                   transactionArrayList.add(transaction);
                }

                ListView ls = findViewById(R.id.listview);
                ListTransactionAdapter listAdapter = new ListTransactionAdapter(TransactionActivity.this,transactionArrayList);
                ls.setAdapter(listAdapter);
            }

            @Override
            public void onFailure(Call<List<Transaction>> call, Throwable t) {
                Toast.makeText(TransactionActivity.this, "Enable to load transaction history.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}