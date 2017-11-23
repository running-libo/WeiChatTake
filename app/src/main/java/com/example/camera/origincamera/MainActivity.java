package com.example.camera.origincamera;

import android.graphics.SurfaceTexture;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.TextureView;

public class MainActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener {
    private OpenCamera openCamera;
    private CiclePercentView percentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setCamera();

        //设置倒计时时间
        percentView = (CiclePercentView) findViewById(R.id.percentview);
        percentView.setCountdownTime(5000);
    }

    private void setCamera(){
        TextureView textureView = (TextureView) findViewById(R.id.textureview);
        openCamera = new OpenCamera(getApplicationContext(), textureView);

        openCamera.startCameraThread();

        if (!textureView.isAvailable()) {
            textureView.setSurfaceTextureListener(this);
        } else {
            openCamera.startPreview();
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        openCamera.setupCamera(width, height);  //设置相机参数,回调的是textureview的宽高
        openCamera.openCamera();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
