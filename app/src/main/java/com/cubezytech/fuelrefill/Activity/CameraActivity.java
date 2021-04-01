package com.cubezytech.fuelrefill.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PointF;
import android.hardware.Camera;
import android.media.MediaActionSound;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cubezytech.fuelrefill.BaseActivity;
import com.cubezytech.fuelrefill.BuildConfig;
import com.cubezytech.fuelrefill.Pref.SharedPrefrance;
import com.cubezytech.fuelrefill.R;
import com.cubezytech.fuelrefill.Utils.Const;
import com.otaliastudios.cameraview.CameraException;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraLogger;
import com.otaliastudios.cameraview.CameraOptions;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.FaceDetection.FaceOverlayView;
import com.otaliastudios.cameraview.FaceDetection.FaceUtils;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.VideoResult;
import com.otaliastudios.cameraview.controls.Engine;
import com.otaliastudios.cameraview.controls.Facing;
import com.otaliastudios.cameraview.controls.Flash;
import com.otaliastudios.cameraview.controls.Mode;
import com.otaliastudios.cameraview.controls.Preview;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static android.os.Build.VERSION.SDK_INT;

public class CameraActivity extends BaseActivity implements SurfaceHolder.Callback, View.OnClickListener {

    private final static CameraLogger LOG = CameraLogger.create(BuildConfig.APPNAME);

    CameraView camera;
    ImageView img_camera, img_gallery;
    ImageView img_flash;
    FaceOverlayView mFaceView;
    boolean flag = false, flash_flag = false;
    private String from = "";
    private Camera mCamera;
    private long mCaptureTime;
    private Camera.FaceDetectionListener faceDetectionListener = (faces, camera) -> {
        Log.e("LLLL_onFaceDetection", "Number of Faces:" + faces.length);
        // Update the view now!
        mFaceView.setFaces(faces);
    };


    @Override
    public void permissionGranted() {

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        from = getIntent().getStringExtra("from");

        camera = findViewById(R.id.camera);
        img_camera = findViewById(R.id.img_camera);
        img_gallery = findViewById(R.id.img_gallery);
        img_flash = findViewById(R.id.img_flash);

        if (SDK_INT >= 23) {
            camera.setEngine(Engine.CAMERA2);
        } else
            camera.setEngine(Engine.CAMERA1);

        camera.setLifecycleOwner(this);
        camera.addCameraListener(new Listener());
        camera.setFacing(Facing.BACK);

        if (SharedPrefrance.getFlash(CameraActivity.this).equals("OFF")) {
            img_flash.setImageDrawable(CameraActivity.this.getResources().getDrawable(R.drawable.ic_flash_off));
            camera.setFlash(Flash.OFF);
        } else if (SharedPrefrance.getFlash(CameraActivity.this).equals("ON")) {
            camera.setFlash(Flash.ON);
            img_flash.setImageDrawable(CameraActivity.this.getResources().getDrawable(R.drawable.ic_flash_on));
        }

        // OnClick
        img_camera.setOnClickListener(this);
        img_flash.setOnClickListener(this);
        img_gallery.setOnClickListener(this);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = Camera.open(1);
        mCamera.setFaceDetectionListener(faceDetectionListener);
        mCamera.startFaceDetection();
        try {
            mCamera.setPreviewDisplay(holder);
        } catch (Exception e) {
            Log.e("LLLLLLL", "Could not preview the image.", e);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // We have no surface, return immediately:
        if (holder.getSurface() == null) {
            return;
        }
        // Try to stop the current preview:
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // Ignore...
        }
        // Get the supported preview sizes:
        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
        Camera.Size previewSize = previewSizes.get(0);
        // And set them:
        parameters.setPreviewSize(previewSize.width, previewSize.height);
        mCamera.setParameters(parameters);
        // Now set the display orientation for the camera. Can we do this differently?
        int mDisplayRotation = FaceUtils.getDisplayRotation(CameraActivity.this);
        int mDisplayOrientation = FaceUtils.getDisplayOrientation(mDisplayRotation, 0);
        mCamera.setDisplayOrientation(mDisplayOrientation);

        if (mFaceView != null) {
            mFaceView.setDisplayOrientation(mDisplayOrientation);
        }

        // Finally start the camera preview again:
        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.setPreviewCallback(null);
        mCamera.setFaceDetectionListener(null);
        mCamera.setErrorCallback(null);
        mCamera.release();
        mCamera = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_camera) {
            camera.setMode(Mode.PICTURE);
            capturePictureSnapshot();

        } else if (v.getId() == R.id.img_gallery) {
            Intent intent = new Intent(CameraActivity.this,AlbumActivity.class);
            intent.putExtra("from",from);
            startActivity(intent);
        } else if (v.getId() == R.id.img_flash) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                runOnUiThread(() -> {
                    if (camera.getFlash() == Flash.ON) {
                        SharedPrefrance.setFlash(CameraActivity.this, "OFF");
                        img_flash.setImageDrawable(CameraActivity.this.getResources().getDrawable(R.drawable.ic_flash_off));
                        camera.setFlash(Flash.OFF);
                    } else if (camera.getFlash() == Flash.OFF) {
                        SharedPrefrance.setFlash(CameraActivity.this, "ON");
                        img_flash.setImageDrawable(CameraActivity.this.getResources().getDrawable(R.drawable.ic_flash_on));
                        camera.setFlash(Flash.ON);
                    }
                });
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void capturePictureSnapshot() {
        if (camera.isTakingPicture()) return;
        if (camera.getPreview() != Preview.GL_SURFACE) {
            message("Picture snapshots are only allowed with the GL_SURFACE preview.", true);
            return;
        }
        mCaptureTime = System.currentTimeMillis();
        message("Capturing picture snapshot...", false);
        if (SharedPrefrance.getFlash(CameraActivity.this).equalsIgnoreCase(String.valueOf(Flash.OFF))) {
            camera.setFlash(Flash.OFF);
        } else if (SharedPrefrance.getFlash(CameraActivity.this).equalsIgnoreCase(String.valueOf(Flash.AUTO))) {
            camera.setFlash(Flash.AUTO);
        } else
            camera.setFlash(Flash.ON);

        camera.setPictureMetering(true);
        camera.takePictureSnapshot();
    }

    private File createImageFile() {
        clearTempImages();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new
                Date());
        File file = new File(Const.IMAGE_PATH, "IMG_" + timeStamp +
                ".jpg");
        return file;
    }

    private void clearTempImages() {
        try {
            File tempFolder = new File(Const.IMAGE_PATH);
            for (File f : tempFolder.listFiles())
                f.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void message(@NonNull String content, boolean important) {
        if (important) {
            LOG.w(content);
        } else {
            LOG.e(content);
        }
    }

    private class Listener extends CameraListener {

        @Override
        public void onCameraOpened(@NonNull CameraOptions options) {
            Log.d("mn13option", options.toString());

            camera.setCameraDistance(10 * camera.getWidth());
            int cameraid = 0;
            if (camera.getFacing() == Facing.BACK) {
                cameraid = 0;
            } else if (camera.getFacing() == Facing.FRONT) {
                cameraid = 1;
            }

            Log.d("mn13camera", cameraid + ":");

        }

        @Override
        public void onOrientationChanged(int orientation) {
            super.onOrientationChanged(orientation);
            Log.d("mn13option1", orientation + "");

        }

        @Override
        public void onCameraError(@NonNull CameraException exception) {
            super.onCameraError(exception);
            message("Got CameraException #" + exception.getReason(), true);
        }

        @Override
        public void onPictureTaken(@NonNull PictureResult result) {
            super.onPictureTaken(result);
            long callbackTime = System.currentTimeMillis();
            if (mCaptureTime == 0) mCaptureTime = callbackTime - 300;
            LOG.w("onPictureTaken called! Launching activity. Delay:", callbackTime - mCaptureTime);

            String filePath = "";
            FileOutputStream outStream;
            try {
                File wallpaperDirectory = new File(Const.IMAGE_PATH);
                if (!wallpaperDirectory.exists()) {
                    wallpaperDirectory.mkdirs();
                }

                try {
                    File f = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".jpg");
                    f.createNewFile();
                    f.getAbsoluteFile();
                    Log.e("selectedfile", f + "");
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(result.getData());

                    fo.close();
                    filePath = f.getAbsolutePath();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                MediaActionSound sound = new MediaActionSound();
                Log.e("LLLL_filepath: ", filePath);

                switch (from) {
                    case "carDriver":
                        Const.carDriver = filePath;
                        break;
                    case "beforeRefill":
                        Const.beforeRefill = filePath;
                        break;
                    case "afterRefill":
                        Const.afterRefill = filePath;
                        break;
                }

                Intent intent = new Intent(CameraActivity.this, FewRefilleActivity.class);
                intent.putExtra("path", filePath);
                startActivity(intent);
                finish();

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("LLLLL_Exc: ", Objects.requireNonNull(e.getLocalizedMessage()));
            }

        }

        @Override
        public void onVideoTaken(@NonNull VideoResult result) {
            super.onVideoTaken(result);
            LOG.w("onVideoTaken called! Launched activity.");
        }

        @Override
        public void onVideoRecordingStart() {
            super.onVideoRecordingStart();
            Log.e("LLLLLLL_Video: ", "start");
            LOG.w("onVideoRecordingStart!");
        }

        @Override
        public void onVideoRecordingEnd() {
            super.onVideoRecordingEnd();
            Log.e("LLLLLLL_Video: ", "end");
            message("Video taken. Processing...", false);
            LOG.w("onVideoRecordingEnd!");
        }

        @Override
        public void onExposureCorrectionChanged(float newValue, @NonNull float[] bounds, @Nullable PointF[] fingers) {
            super.onExposureCorrectionChanged(newValue, bounds, fingers);
            message("Exposure correction:" + newValue, false);
        }


        @Override
        public void onZoomChanged(float newValue, @NonNull float[] bounds, @Nullable PointF[] fingers) {
            super.onZoomChanged(newValue, bounds, fingers);
            message("Zoom:" + newValue, false);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CameraActivity.this,FewRefilleActivity.class);
        intent.putExtra("path","");
        startActivity(intent);
        finish();
    }
}