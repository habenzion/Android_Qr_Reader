package com.example.kasaqr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class MainActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner;
     Vibrator vibrator ;
     TextView nameTv,contractTv,meterTv,scannedTV;
    Button button,againBtn;
    LinearLayout buttonsll;
    CardView cardView;
    ShimmerFrameLayout shimmerFrameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        Parser();
        cardView.setVisibility(View.INVISIBLE);
        scannedTV.setVisibility(View.INVISIBLE);
        buttonsll.setVisibility(View.INVISIBLE);
        mCodeScanner = new CodeScanner(this, scannerView);
        RequestCam();
        shimmerFrameLayout.startShimmer();
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TranslateAnimation animate = new TranslateAnimation(0, 0, cardView.getHeight(), 0);
                        animate.setDuration(500);
                        animate.setFillAfter(true);
                        cardView.startAnimation(animate);
                        cardView.setVisibility(View.VISIBLE);
                        scannedTV.setVisibility(View.VISIBLE);
                        buttonsll.setVisibility(View.VISIBLE);
                        scannedTV.setText("SUCCESS");
                        Vibrator();
                        nameTv.setText("ምሉእ ስም: "+result.getText());
                        contractTv.setText("ቁጽሪ ኮንትራት.: 45235");
                        meterTv.setText("ዓይነት ቆጻሪ: A2/345");
                        shimmerFrameLayout.stopShimmer();
                    }
                });
            }
        });
        againBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
                shimmerFrameLayout.startShimmer();
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
                shimmerFrameLayout.startShimmer();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
    private void RequestCam()
    {
        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                mCodeScanner.startPreview();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }
    public void Parser()
    {
        cardView = findViewById(R.id.resultcv);
        nameTv = findViewById(R.id.nametv);
        contractTv = findViewById(R.id.contracttv);
        meterTv = findViewById(R.id.metertv);
        scannedTV = findViewById(R.id.scannedTV);
        againBtn = findViewById(R.id.againbtn);
        buttonsll = findViewById(R.id.line2);
        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);
    }

    public void Vibrator() {
        final VibrationEffect vibrationEffect1;
        // this is the only type of the vibration which requires system version Oreo (API 26)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            // this effect creates the vibration of default amplitude for 1000ms(1 sec)
            vibrationEffect1 = VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE);
            // it is safe to cancel other vibrations currently taking place
            vibrator.cancel();
            vibrator.vibrate(vibrationEffect1);
        }
    }
}