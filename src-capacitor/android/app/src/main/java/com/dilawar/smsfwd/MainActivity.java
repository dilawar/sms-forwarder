package com.dilawar.smsfwd;

import android.content.pm.PackageManager;
import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.PreviewableHandwritingGesture;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;

import com.dilawar.capplugins.SmsPlugin;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {
    private final String TAG = "main";
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        registerPlugin(SmsPlugin.class);
        super.onCreate(savedInstanceState);

        // Register dynamic permission.
        // Dynamic handling of permissions.
        // system permissions dialog. Save the return value, an instance of
        // ActivityResultLauncher, as an instance variable.
        this.requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        // Permission is granted. Continue the action or workflow in your
                        // app.
                        Log.i(TAG, "Requested permission is granted.");
                    } else {
                        // Explain to the user that the feature is unavailable because the
                        // feature requires a permission that the user has denied. At the
                        // same time, respect the user's decision. Don't link to system
                        // settings in an effort to convince the user to change their
                        // decision.
                        Log.w(TAG, "Requested permission is not granted!");
                    }
                });

        ensurePermissionsAreGranted(Manifest.permission.READ_SMS);
        ensurePermissionsAreGranted(Manifest.permission.SEND_SMS);
        ensurePermissionsAreGranted(Manifest.permission.RECEIVE_SMS);
    }

    private void ensurePermissionsAreGranted(String perm) {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), perm) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            Log.i(TAG, "Permission has been granted: " + perm);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected, and what
                // features are disabled if it's declined. In this UI, include a
                // "cancel" or "no thanks" button that lets the user continue
                // using your app without granting the permission.
            } else {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(perm);
            }
        }
    }


}
