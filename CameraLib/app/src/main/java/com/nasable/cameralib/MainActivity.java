package com.nasable.cameralib;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity {
    private ImageView takePictureButton;
    private ImageView animationOverlay;
    private TextureView textureView;
    private CameraLib cameraLib;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textureView = (TextureView) findViewById(R.id.texture);
        //assert textureView != null;
        cameraLib=new CameraLib(this,textureView);
        textureView.setSurfaceTextureListener(cameraLib.getTextureListener());
        takePictureButton = (ImageView) findViewById(R.id.btn_takepicture);
        animationOverlay = (ImageView) findViewById(R.id.animationOverlay);
        assert takePictureButton != null;
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationOverlay.setVisibility(View.VISIBLE);
                animationOverlay.setAnimation(cameraLib.getAnimationSet(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        animationOverlay.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                }));

                cameraLib.takePicture(new CameraLib.OnPictureTakenListener() {
                    @Override
                    public void onFinish(byte[] imageData) {
                        Log.d("gotit","bytes "+imageData.length);
                    }

                    @Override
                    public void onError(Exception e) {
                       e.printStackTrace();
                    }
                });
            }
        });
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CameraLib.REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(getApplicationContext(), "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show();
                finish();
            }else{

            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
      //  Log.e(TAG, "onResume");
        cameraLib.startBackgroundThread();
        if (textureView.isAvailable()) {
            cameraLib.openCamera();
        } else {
            textureView.setSurfaceTextureListener(cameraLib.getTextureListener());
        }
    }
    @Override
    protected void onPause() {
        //Log.e(TAG, "onPause");
       // cameraLib.closeCamera();
        cameraLib.stopBackgroundThread();
        super.onPause();
    }
}
