package me.azhar.androidutility;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import me.azhar.androidutility.Common.Common;


public class NewActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_item);
        imageView=(ImageView)findViewById(R.id.image_view);
        textView=(TextView)findViewById(R.id.text_view);

        textView.setText(Common.currentItem.getName());

        if(Common.currentItem.isChecked())
            imageView.setImageResource(R.drawable.ic_done_black_24dp);
        else
            imageView.setImageResource(R.drawable.ic_clear_black_24dp);
    }
}
