package com.nasable.cameralib;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    Uri imageUri;
    public static int OLD_CAMERA_INTENT_REQUEST=0x01d;
    public static int COSTUME_CAMERA_INTENT_REQUEST=0xc0;

    ImageView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result=(ImageView)findViewById(R.id.result);
        if(Build.VERSION_CODES.LOLLIPOP <= Build.VERSION.SDK_INT ){
            Intent preview=new Intent(this,CameraPreviewActivity.class);
            startActivityForResult(preview,COSTUME_CAMERA_INTENT_REQUEST);
        }else{
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "New Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
            imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(cameraIntent, OLD_CAMERA_INTENT_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == OLD_CAMERA_INTENT_REQUEST && resultCode == RESULT_OK){
            try {
                Bitmap resultBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                result.setImageBitmap(resultBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (requestCode == COSTUME_CAMERA_INTENT_REQUEST && resultCode == RESULT_OK){
             final byte[] jpeg = ResultHolder.getImage();
            Bitmap resultBitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(jpeg));
            result.setImageBitmap(resultBitmap);
    }
            super.onActivityResult(requestCode, resultCode, data);
    }
}