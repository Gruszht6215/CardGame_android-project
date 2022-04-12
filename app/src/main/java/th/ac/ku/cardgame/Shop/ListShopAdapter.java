package th.ac.ku.cardgame.Shop;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import th.ac.ku.cardgame.R;
import th.ac.ku.cardgame.UserModel.User;

public class ListShopAdapter extends ArrayAdapter<CardItem> {
    public static String PACKAGE_NAME;

    public ListShopAdapter(Context context, ArrayList<CardItem> cardsArrayList) {
        super(context, R.layout.list_shop, cardsArrayList);
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
        buyButton.setText("$" + String.valueOf(card.getPrice()));

        return convertView;
    }
}
