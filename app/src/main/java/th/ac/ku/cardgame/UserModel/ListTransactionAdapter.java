package th.ac.ku.cardgame.UserModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import th.ac.ku.cardgame.R;

public class ListTransactionAdapter extends ArrayAdapter<Transaction> {

    public ListTransactionAdapter(Context context, ArrayList<Transaction> transactionsArrayList) {
        super(context, R.layout.list_transaction, transactionsArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Transaction transaction = getItem(position);

        if (convertView == null){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_transaction,parent,false);

        }

        TextView task = convertView.findViewById(R.id.textViewTask);
        TextView value = convertView.findViewById(R.id.textViewValue);
        TextView detail = convertView.findViewById(R.id.textViewDetail);
        TextView created_at = convertView.findViewById(R.id.textViewCreated);

        task.setText(transaction.task);
        value.setText(transaction.value);
        detail.setText(transaction.detail);
        created_at.setText(transaction.created_at);

        return convertView;
    }
}
