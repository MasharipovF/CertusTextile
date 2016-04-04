package masharipov.certustextile;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {
    LinearLayout panelyoqa;
    Handler timerHand;
    Runnable backanim;
    int current_status;

    android.support.v4.app.Fragment oldi,yon,orqa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_second);

        panelyoqa=(LinearLayout) findViewById(R.id.panelanim);
        current_status=0;

        timerHand=new Handler();
        oldi=new ItemFragment(R.drawable.futblka);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame, oldi, "OLDI")
                .commit();

        findViewById(R.id.backa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                panelyoqa.animate().translationX(-140).start();
            }
        });
        findViewById(R.id.tovari).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                panelyoqa.animate().translationX(140).start();
                timerHand.postDelayed(backanim, 1000);
            }
        });
        backanim=new Runnable() {
            @Override
            public void run() {
                panelyoqa.animate().translationX(-140).start();
                }
        };
        findViewById(R.id.burish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current_status==0){
                getSupportFragmentManager()
                        .beginTransaction().replace(R.id.frame,yon).commit();
                current_status=1;}
                else if(current_status==1){
                    getSupportFragmentManager()
                            .beginTransaction().replace(R.id.frame,orqa).commit();
                current_status=2;
                }
                else if(current_status==2){
                    getSupportFragmentManager()
                            .beginTransaction().replace(R.id.frame,oldi).commit();
                    current_status=0;
                }
            }
        });
      /*  Intent intent = getIntent();
        int number = intent.getIntExtra("POS", -1);*/
       // eye.setVisibility(View.GONE);
    }
    @Override
    public void onStart(){
        super.onStart();
        //bu prosto initsalizovat qivoladi
        // tovar o`zgarganda fragmentdigi changeTovar(URI) funksiyasi chaqiriladi

        yon=new ItemFragment(R.drawable.futblkaikki);
        orqa=new ItemFragment(R.drawable.futblkaorqa);
    }

}
