package br.com.microserv.msvmobilepdv.extra;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;

import br.com.microserv.msvmobilepdv.R;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ActivateDeviceActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView _zx = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //_zx = new ZXingScannerView(this);
        //setContentView(_zx);

        // setContentView(R.layout.activity_activate_device);
    }

    @Override
    public void onResume() {
        super.onResume();

        _zx = new ZXingScannerView(this);
        setContentView(_zx);

        _zx.setResultHandler(this);
        _zx.startCamera();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _zx.stopCamera();
    }

    @Override
    public void handleResult(Result result) {

        final String text = result.getText();

        Toast.makeText(ActivateDeviceActivity.this, text, Toast.LENGTH_SHORT).show();
    }
}
