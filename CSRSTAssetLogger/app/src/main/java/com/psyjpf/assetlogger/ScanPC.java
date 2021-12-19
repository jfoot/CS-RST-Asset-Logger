package com.psyjpf.assetlogger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.psyjpf.assetlogger.databinding.ActivityMainBinding;
import com.psyjpf.assetlogger.databinding.ActivityScanPcBinding;

import java.io.IOException;

public class ScanPC extends AppCompatActivity {

    SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;

    private String data = "";
    private String assetTag;

    private static final String TAG = "ScanPC";

    private Asset viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(Asset.class);
        ActivityScanPcBinding v = ActivityScanPcBinding.inflate(LayoutInflater.from(this));
        setContentView(v.getRoot());
        v.setViewmodel(viewModel);


        surfaceView = v.getRoot().findViewById(R.id.surfaceView);
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

                            data = barcodes.valueAt(0).displayValue;
                            //viewModel.AssetTag.setValue("DDDD");
                            //Toast.makeText(ScanPC.this,temp,Toast.LENGTH_LONG).show();

                            surfaceView.setVisibility(View.GONE);
                        });


                        viewModel.AssetTag.setValue("ddd");
//                        data = barcodes.valueAt(0).displayValue;
//                        viewModel.AssetTag.postValue(data);




//                        runOnUiThread(()-> {
//                            //cameraSource.release();
//                          //  surfaceView.setVisibility(View.GONE);
//                            //data = barcodes.valueAt(0).displayValue;
//                            //viewModel.AssetTag.setValue(data);
////                            Gson gson = new GsonBuilder().create();
////                            Asset temp = gson.fromJson(data,Asset.class);
////                            viewModel.merge(temp);
//                        });





                }
            }
        });
    }


    private boolean hasScanned(){
        return !data.trim().equals("");
    }

}