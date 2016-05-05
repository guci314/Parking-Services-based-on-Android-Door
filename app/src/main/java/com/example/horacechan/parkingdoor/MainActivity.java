package com.example.horacechan.parkingdoor;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.horacechan.parkingdoor.api.ParkingApp;
import com.example.horacechan.parkingdoor.api.model.StatusEntity;
import com.example.horacechan.parkingdoor.util.QREncodingHandler;
import com.example.horacechan.parkingdoor.util.thread.RefreshQRThread;
import com.google.zxing.WriterException;

public class MainActivity extends ActionBarActivity implements RefreshQRThread.OnDataRefreshListener {

    private ImageView qrImgImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        qrImgImageView = (ImageView) this.findViewById(R.id.iv_qr_image);
        RefreshQRThread.INSTANCE.init("",this).start();
    }



    @Override
    public void onDataRefresh(StatusEntity entity) {
            switch (entity.getOldStatus()){
                case 0:
                    break;
                case 1:
                    Toast.makeText(MainActivity.this,"欢迎进库！",Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(MainActivity.this,"欢迎出库！",Toast.LENGTH_LONG).show();
                    break;
            }
            try {
                String contentString = entity.getNewLogId()+","+ParkingApp.MARKERID;
                Bitmap qrCodeBitmap = QREncodingHandler.createQRCode(contentString, 400);
                qrImgImageView.setImageBitmap(qrCodeBitmap);
            }catch (WriterException e){
                e.printStackTrace();
            }
    }

    @Override
    protected void onDestroy() {
        RefreshQRThread.INSTANCE.setBlock(true);
        super.onDestroy();
    }
}
