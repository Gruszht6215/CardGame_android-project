package th.ac.ku.cardgame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import th.ac.ku.cardgame.UserModel.ListTransactionAdapter;
import th.ac.ku.cardgame.UserModel.Transaction;
import th.ac.ku.cardgame.databinding.ActivityMainBinding;

public class TransactionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        String[] task = {"Christopher","Craig","Sergio","Mubariz","Mike","Michael","Toa","Ivana","Alex"};
        String[] detail = {"Heye","Supp","Let's Catchup","Dinner tonight?","Gotta go",
                "i'm in meeting","Gotcha","Let's Go","any Weekend Plans?"};
        String[] value = {"8:45 pm","9:00 am","7:34 pm","6:32 am","5:76 am",
                "5:00 am","7:34 pm","2:32 am","7:76 am"};
        String[] created_at = {"7656610000","9999043232","7834354323","9876543211","5434432343",
                "9439043232","7534354323","6545543211","7654432343"};

        ArrayList<Transaction> transactionArrayList = new ArrayList<>();

        for(int i = 0;i< 5;i++){
            Transaction transaction = new Transaction(task[i],detail[i],value[i],created_at[i]);
           transactionArrayList.add(transaction);

        }
        ListView ls = findViewById(R.id.listview);
        ListTransactionAdapter listAdapter = new ListTransactionAdapter(TransactionActivity.this,transactionArrayList);
        ls.setAdapter(listAdapter);
    }
}