package me.azhar.androidutility;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_READ_CONTACTS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();
    }

    String[] permissions = {
            Manifest.permission.READ_CONTACTS
    };

    private boolean checkPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{permissions[0]}, REQUEST_READ_CONTACTS);
            return false;
        } else {
            makeText(this, R.string.grant, LENGTH_SHORT).show();
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_CONTACTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeText(this, R.string.phone_granted, LENGTH_SHORT).show();
                } else {
                    makeText(this, R.string.request_permission, LENGTH_SHORT).show();
                }
                break;

        }

    }

    public void goMultiplePermission(View view) {
        startActivity(new Intent(this, Multiple_Permissions.class));
    }
}