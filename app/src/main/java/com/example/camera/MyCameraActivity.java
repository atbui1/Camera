package com.example.camera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

public class MyCameraActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 998;
    private static final int CAMERA_VIDEO_REQUEST = 997;
    private ImageView imgPresent;
    private Uri outputFileUri;
    private static final String outputFileName = "takePhoto.jpg";
    private static final String outputVideoName = "takeVideo.mp4";
    private VideoView videoPresent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_camera);

        imgPresent = (ImageView) findViewById(R.id.imgResult);
        videoPresent = (VideoView) findViewById(R.id.videoViewResult);
    }

    public void clickToTakePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    public void clickToPhotoFile(View view) {
        File file = new File("/sdcard/DCIM/Camera", outputFileName);

        outputFileUri = Uri.fromFile(file);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    public void clickToVideo(View view) {
        File sdCard = Environment.getExternalStorageDirectory();
        String realPath = sdCard.getAbsolutePath();
        File directory = new File(realPath + "/MyDBs");
        File file = new File("/sdcard/DCIM/Camera", outputVideoName);

        outputFileUri = Uri.fromFile(file);

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(intent, CAMERA_VIDEO_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST) {
            if (data != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imgPresent.setImageBitmap(photo);
            } else {
                int width = imgPresent.getWidth();
                int height = imgPresent.getHeight();
                BitmapFactory.Options factoryOptions = new BitmapFactory.Options();
                factoryOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(outputFileUri.getPath(), factoryOptions);
                int imageWidth = factoryOptions.outWidth;
                int imageHeight = factoryOptions.outHeight;
                int scaleFator = Math.min(imageWidth / width, imageHeight / height);
                factoryOptions.inJustDecodeBounds = false;
                factoryOptions.inSampleSize = scaleFator;
                factoryOptions.inPurgeable = true;
                Bitmap bitmap = BitmapFactory.decodeFile(outputFileUri.getPath(), factoryOptions);
                Toast.makeText(this, "File has already store", Toast.LENGTH_LONG).show();
                imgPresent.setImageBitmap(bitmap);
            }
        } else if (requestCode == CAMERA_VIDEO_REQUEST) {
            Log.d("ddd", "abc");
            videoPresent.setVideoPath(outputFileUri.getPath());
            videoPresent.start();
        }
    }
}
