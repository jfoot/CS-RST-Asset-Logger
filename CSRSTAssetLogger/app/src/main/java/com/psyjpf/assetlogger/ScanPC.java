package com.psyjpf.assetlogger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.psyjpf.assetlogger.databinding.ActivityScanPcBinding;

import java.io.IOException;

public class ScanPC extends AppCompatActivity {


    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;

    private SurfaceView surfaceView;
    private TextView title;

    private AssetViewModel viewModel;


    private static final String TAG = "ScanPC";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(AssetViewModel.class);
        ActivityScanPcBinding v = ActivityScanPcBinding.inflate(LayoutInflater.from(this));
        setContentView(v.getRoot());
        v.setViewmodel(viewModel);
        v.setLifecycleOwner(this);

        Intent intent = getIntent();
        String assetTag = intent.getStringExtra("asset_tag");
        viewModel.Asset.getValue().setAssetTag(assetTag);

        surfaceView = v.getRoot().findViewById(R.id.surfaceView);
        title = v.getRoot().findViewById(R.id.title);
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
        if(hasScanned())
            surfaceView.setVisibility(View.GONE);
    }

    private void initialiseDetectorsAndSources() {
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScanPC.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScanPC.this, new
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
                    runOnUiThread(()-> {
                        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.beep);
                        mp.start();
                        surfaceView.setVisibility(View.GONE);
                        Gson gson = new GsonBuilder().create();
                        Log.i(TAG, "receiveDetections: " + barcodes.valueAt(0).displayValue);

                        Asset temp = gson.fromJson(barcodes.valueAt(0).displayValue, Asset.class);
                        viewModel.Asset.getValue().merge(temp);
                        title.setText("Confirm Values.");
                    });
                }
            }
        });
    }


    private boolean hasScanned(){
        return viewModel.isSetup;
    }

    public void logAsset(View view) {

    }
}