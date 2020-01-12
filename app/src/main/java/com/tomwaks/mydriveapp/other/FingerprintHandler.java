package com.tomwaks.mydriveapp.other;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.widget.TextView;
import com.tomwaks.mydriveapp.R;

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;
    private ObjectAnimator ani;

    public FingerprintHandler(Context context, ObjectAnimator ani){

        this.context = context;
        this.ani = ani;

    }

    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject){

        CancellationSignal cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);

    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        TextView tv_info = ((Activity)context).findViewById(R.id.tv_info);
        tv_info.setText("Auth Error: " + errString);
        tv_info.setTextColor(Color.RED);

    }

    @Override
    public void onAuthenticationFailed() {
        TextView tv_info = ((Activity)context).findViewById(R.id.tv_info);
        tv_info.setText("Auth Failed");
        tv_info.setTextColor(Color.RED);
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        TextView tv_info = ((Activity)context).findViewById(R.id.tv_info);
        tv_info.setText("Error: " + helpString);
        tv_info.setTextColor(Color.RED);

    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        TextView tv_info = ((Activity)context).findViewById(R.id.tv_info);
        tv_info.setText("The fingerprint is correct");
        this.ani.cancel();
        tv_info.setTextColor(Color.GREEN);

    }
}
