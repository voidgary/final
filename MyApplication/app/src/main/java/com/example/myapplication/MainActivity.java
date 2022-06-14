package com.example.myapplication;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
public class MainActivity extends Activity implements OnClickListener {
    protected static final int MSG_WHAT = 0;
    //開始倒計時
    private Button btn_go;
    //獲取到輸入的時間
    private Button btn_get;
    //停止倒計時
    private Button btn_stop;
    //輸入的時間
    private static EditText et_time;
    //時間
    private static TextView tv_time;
    private Timer timer;
    private static int time;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            tv_time.setText(time + "");
            switch (msg.what) {
                case MSG_WHAT:
                    if (time > 0) {
                        time--;
                    } else {
                        Toast.makeText(MainActivity.this, "倒計時完成", Toast.LENGTH_SHORT).show();
                        if (timer != null) {

                            timer.cancel();
                            timer = null;
                        }
                    }
                    break;

                default:
                    break;
            }
        }

        ;
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_go = (Button) findViewById(R.id.btn_go);
        btn_get = (Button) findViewById(R.id.btn_get);
        btn_stop = (Button) findViewById(R.id.btn_stop);
        et_time = (EditText) findViewById(R.id.et_timer);
        tv_time = (TextView) findViewById(R.id.tv_time);
        btn_go.setOnClickListener(this);
        btn_get.setOnClickListener(this);
        btn_stop.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            //獲得倒計時間
            case R.id.btn_get:
                if (et_time.getText().toString().trim() != null && !(et_time.getText().toString().trim().equals(""))) {
                    try {
                        time = Integer.parseInt(et_time.getText().toString());
                        tv_time.setText(et_time.getText().toString());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "時間格式不正確", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                break;
            //開始倒計時
            case R.id.btn_go:
                if (timer == null) {
                    timer = new Timer();
                    timer.schedule(new TimerTask() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            mHandler.sendEmptyMessage(MSG_WHAT);

                        }
                    }, 0, 1000);
                } else {
                    Toast.makeText(MainActivity.this, "請勿多次點擊", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            //停止倒計時
            case R.id.btn_stop:
                if (timer != null) {

                    timer.cancel();
                    timer = null;
                }
                break;
            default:
                break;
        }
    }
}