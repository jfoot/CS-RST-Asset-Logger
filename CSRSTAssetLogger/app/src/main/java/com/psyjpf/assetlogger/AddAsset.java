package com.psyjpf.assetlogger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.Locale;

public class AddAsset extends AppCompatActivity {


    SurfaceView surfaceView;
    EditText txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    Button btnAction;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_asset);


        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
        btnAction = findViewById(R.id.btnAction);
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }

    private void initialiseDetectorsAndSources() {
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(AddAsset.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(AddAsset.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0 && !hasScanned()) {

                    txtBarcodeValue.post(() -> {
                        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.beep);
                        mp.start();
                        btnAction.setText("Confirm");
                        txtBarcodeValue.setText(barcodes.valueAt(0).displayValue);
                        Toast.makeText(getApplicationContext(), "Asset Tag Found : " + getAssetTag(), Toast.LENGTH_SHORT).show();
                    });

                }
            }
        });
    }


    private boolean hasScanned(){
        return !getAssetTag().equals("");
    }

    private String getAssetTag(){
        return txtBarcodeValue.getText().toString().trim().toUpperCase(Locale.ROOT);
    }

    public void next(View view) {
        if(hasScanned()){
            barcodeDetector.release();
            cameraSource.stop();
            startActivity(new Intent(AddAsset.this, ScanPC.class).putExtra("asset_tag", getAssetTag()));
        }else{
            Toast.makeText(getApplicationContext(), "Please scan an asset tag.", Toast.LENGTH_SHORT).show();
        }
    }

    public void clear(View view) {
        btnAction.setText("Please Scan Asset Tag");
        txtBarcodeValue.setText("");
    }
}