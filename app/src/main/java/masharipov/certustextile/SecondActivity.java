package masharipov.certustextile;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {
    LinearLayout panelyoqa;
    Handler timerHand;
    Runnable backanim;
    int current_status;
    TextView razmer,povorot;
    ItemFragment oldi,yon,orqa;
    float razmer_baland=0,razmer_eni=0;
    int razmerC[]={0,0};
    float povorotX=0,povorotY=0;
    int razmerPol[]={15,15,15};
    Vibrator vibr;
    private static final float KATTALASHTIRISH_BIRLIK=  0.05f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_second);
        vibr= (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        panelyoqa=(LinearLayout) findViewById(R.id.panelanim);
        current_status=0;
        razmer=(TextView) findViewById(R.id.razmerpolzunok);
        povorot=(TextView) findViewById(R.id.povorot);
        timerHand=new Handler();
        oldi=new ItemFragment(R.drawable.futblka);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame, oldi, "OLDI")
                .commit();

        findViewById(R.id.glaz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            switch (current_status){
                case 0:
                  oldi.changeSticker(R.drawable.a1);

                    break;
                case 1:
                    yon.changeSticker(R.drawable.a1);

                    break;
                case 2:

                    orqa.changeSticker(R.drawable.a1);
                    break;
            }

            }
        });

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

                    current_status=1;
                razmer.setText(Integer.toString(razmerPol[current_status]));
                }
                else if(current_status==1){
                    getSupportFragmentManager()
                            .beginTransaction().replace(R.id.frame,orqa).commit();
                current_status=2;
                    razmer.setText(Integer.toString(razmerPol[current_status]));
                }
                else if(current_status==2){
                    getSupportFragmentManager()
                            .beginTransaction().replace(R.id.frame,oldi).commit();
                    current_status=0;
                    razmer.setText(Integer.toString(razmerPol[current_status]));

                }
            }
        });

    }
    @Override
    public void onStart(){
        super.onStart();
        //bu prosto initsalizovat qivoladi
        // tovar o`zgarganda fragmentdigi changeTovar(URI) funksiyasi chaqiriladi

        yon=new ItemFragment(R.drawable.futblkaikki);
        orqa=new ItemFragment(R.drawable.futblkaorqa);
    }
   float startAction_DownX=0;
   float startAction_DownY=0;
    float scoree=0;
    @Override
    public boolean onTouchEvent(MotionEvent event){
        float evX = event.getX();
        float evY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startAction_DownX=evX;
                startAction_DownY=evY;
                scoree=startAction_DownX;
                break;
            case MotionEvent.ACTION_MOVE:
                if(startAction_DownY+20>razmerC[1]&&startAction_DownY<razmerC[1]+razmer_baland&&startAction_DownX>razmerC[0]&&startAction_DownX<razmerC[0]+razmer_eni){
                    ItemFragment temp03 = (ItemFragment) getSupportFragmentManager().
                            findFragmentById(R.id.frame);

                    if (scoree+70<evX&&razmerPol[current_status]<30){
                    scoree=evX;
                    scoree+=70;
                    razmerPol[current_status]++;
                   // razmer.animate().scaleX(1.1f).scaleY(1.1f).setDuration(100).scaleY(0.9f).scaleX(0.9f).setDuration(100).start();
                    razmer.setText(Integer.toString(razmerPol[current_status]));

                    temp03.plusSize(KATTALASHTIRISH_BIRLIK);

                    vibr.vibrate(30);
                    Log.d("touchl",Integer.toString(razmerPol[current_status]));
                }
                    if (scoree-70>evX&&razmerPol[current_status]>0){
                        scoree=evX;
                        scoree-=70;
                        razmerPol[current_status]--;
                        temp03.minusSize(KATTALASHTIRISH_BIRLIK);

                        razmer.setText(Integer.toString(razmerPol[current_status]));

                        vibr.vibrate(30);
                        Log.d("touchl",Integer.toString(razmerPol[current_status]));

                    }
                   // Log.d("touchl",Float.toString(event.getX())+"x"+Float.toString(event.getY()));

                }
                break;
            case MotionEvent.ACTION_UP:
                scoree=0;
                break;
        }
         return true;

    }
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        razmer.getLocationOnScreen(razmerC);

        razmer_baland=razmer.getHeight();
        razmer_eni=razmer.getWidth();


        Log.d("touchl",Integer.toString(razmerC[0])+"xXy"+Integer.toString(razmerC[1]));
        Log.d("touchl",Float.toString(razmer_eni)+"wXh"+Float.toString(razmer_baland));

    }
}
