package com.nasable.cameralib;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

public class CameraPreviewActivity extends AppCompatActivity {
    private ImageView takePictureButton;
    private ImageView animationOverlay;
    private AutoFitTextureView mTextureView;
    private CameraLib cameraLib;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_preview); mTextureView = (AutoFitTextureView) findViewById(R.id.texture);
        cameraLib=new CameraLib(this, mTextureView,new CameraLib.OnPictureTakenListener() {
            @Override
            public void onFinish(byte[] imageData) {
                ResultHolder.setImage(imageData);
                setResult(RESULT_OK, null);
                finish();
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
        mTextureView.setSurfaceTextureListener(cameraLib.getSurfaceTextureListener());
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

                cameraLib.takePicture();
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
    public void onResume() {
        super.onResume();
        cameraLib.startBackgroundThread();

        // When the screen is turned off and turned back on, the SurfaceTexture is already
        // available, and "onSurfaceTextureAvailable" will not be called. In that case, we can open
        // a camera and start preview from here (otherwise, we wait until the surface is ready in
        // the SurfaceTextureListener).
        if (mTextureView.isAvailable()) {
            cameraLib.openCamera(mTextureView.getWidth(), mTextureView.getHeight());
        } else {
            mTextureView.setSurfaceTextureListener(cameraLib.getSurfaceTextureListener());
        }
    }

    @Override
    public void onPause() {
        cameraLib.closeCamera();
        cameraLib.stopBackgroundThread();
        super.onPause();
    }
}

