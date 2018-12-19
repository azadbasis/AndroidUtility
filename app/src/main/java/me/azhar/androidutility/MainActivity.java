package me.azhar.androidutility;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity {


    private TextView tvOutPut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvOutPut = (TextView) findViewById(R.id.tvOutput);
        try {
            String testData = "Man is distinguished, not only by his reason, but by this singular passion from other animals, " +
                    "which is a lust of the mind, that by a perseverance of delight in the continued and indefatigable " +
                    "generation of knowledge, exceeds the short vehemence of any carnal pleasure.";
            // Sending side
            byte[] data = new byte[0];
            data = testData.getBytes("UTF-8");
            Log.d("BYTE", data + "");
            String base64 = Base64.encodeToString(data, Base64.DEFAULT);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("EncodedToString: " + base64 + "\n");
            // Receiving side
            byte[] datas = Base64.decode(base64, Base64.DEFAULT);
            String text = new String(datas, "UTF-8");
            stringBuilder.append("DecodeString: " + text);
            tvOutPut.setText(stringBuilder);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


}