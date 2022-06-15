package com.hello.kaiser.progressbardemo;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    private ProgressBar mProgressBar;
    private TextView mPercentage;

    private int progress = 0;//變化參數
    private int hours, mins;
    private Context mContext;
    private TextView mShow;
    private CountDownTimer countDownTimer;
    private Button show_picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();

    }

    private void initView() {
        show_picker = (Button) findViewById(R.id.show_picker);
        mShow = (TextView) findViewById(R.id.show);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mPercentage = (TextView) findViewById(R.id.progress_tv);
    }


    //設定時間的按鈕
    public void timePickerBtn(View view) {
        //防呆
        show_picker.setClickable(true);
//        //假如重複選擇時間，暫停上一個計時器
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer.onFinish();
        }
        TimePickerDialog timepicker = new TimePickerDialog(mContext, onTimeSetListener, hours, mins, true);
        timepicker.show();
    }

    //取消倒數
    public void canelTimeBtn(View view) {
        show_picker.setClickable(true);
        mPercentage.setText("0%");
        mProgressBar.setProgress(0);
        progress = 0;
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer.onFinish();
        }
    }

    //TimePicker監聽
    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            //只format hours and mins
            SimpleDateFormat sdf = new SimpleDateFormat("HHmm");

            //抓取現在的時間
            Calendar calendar = Calendar.getInstance();
            int nowHours = calendar.get(Calendar.HOUR_OF_DAY);
            int nowMins = calendar.get(Calendar.MINUTE);

            Long time = null;
            //確認格式是我們在SimpleDateFormate內指定的樣子"HHmm"
            Log.d("checkpoint", " 確認選取的時間 = " + format(hourOfDay) + format(minute));
            Log.d("checkpoint", "卻認現在的時間 = " + format(nowHours) + format(nowMins));

            try {
                //選取時間 - 現在時間
                Date pickDate = sdf.parse(format(hourOfDay) + format(minute));
                Date nowDate = sdf.parse(format(nowHours) + format(nowMins));
                Long pickLongDate = pickDate.getTime();
                Long nowLongDate = nowDate.getTime();
                time = pickLongDate - nowLongDate;
                //設定ProgressBar
                mProgressBar.setMax(toIntExact(time));
                Log.d("checkpoint", "checkpoint = " + time);
            } catch (ParseException e) {
                e.printStackTrace();
                Log.d("checkpoint", "error - " + e);
            }

            if (time != null) {
                //time為毫秒 帶入倒數計時器CountDownTimer
                final Long finalTime = time;
                countDownTimer = new CountDownTimer(finalTime, 500) {
                    @Override
                    public void onFinish() {
                        mShow.setText("Done!");
                        if (progress != 0) {
                            mPercentage.setText("100%");
                            mProgressBar.setProgress(toIntExact(finalTime));
                        } else {
                            mPercentage.setText("0%");
                        }
                        show_picker.setClickable(true);
                    }

                    @Override
                    public void onTick(long millisUntilFinished) {
                        //這方法中間如果遇到個位數字，前方自動補0 "08"
                        NumberFormat f = new DecimalFormat("00");
                        long hour = (millisUntilFinished / 3600000) % 24;
                        long min = (millisUntilFinished / 60000) % 60;
                        long sec = (millisUntilFinished / 1000) % 60;

                        //換算必須用Double，如果使用int不管怎麼除都會是0
                        progress = (int) ((Double.valueOf(finalTime - millisUntilFinished) / Double.valueOf(finalTime)) * 100);
                        //在計時器內對ProgrssBar做每秒的更新
                        mProgressBar.setProgress(toIntExact(finalTime - millisUntilFinished));
                        //設定旁邊的文字%
                        mPercentage.setText(progress + "%");
                        //設定倒數計時
                        mShow.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
                    }
                }.start();
            }
        }
    };


    //重新把long變成String，中間如果遇到個位數字，前方自動補0 "08"
    private String format(long value) {
        String valueTwo = stringValue(value);
        if (valueTwo.length() == 1) {
            return "0" + valueTwo;
        }
        return valueTwo;
    }

    //轉換器long convert to String
    private String stringValue(long value) {
        return String.valueOf(value);
    }

    //轉換器 Long convert to int
    public static int toIntExact(long value) {
        if ((int) value != value) {
            throw new ArithmeticException("integer overflow");
        }
        return (int) value;
    }


}
















