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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {
    LinearLayout panelyoqa;
    Handler timerHand;
    Runnable backanim;
    int current_status;
    TextView razmer, povorot;
    ItemFragment oldi, yon, orqa;
    float razmer_baland = 0, razmer_eni = 0;
    float rotate_baland = 0, rotate_eni = 0;
    int razmerC[] = {0, 0};
    int rotatC[] = {0, 0};
    float povorotX = 0, povorotY = 0;
    int razmerPol[] = {15, 15, 15};
    int povorotMas[] = {0, 0, 0};
    Vibrator vibr;
    RecyclerView tovarRecycler;

    // recycler uchun
    int tovarBoyi, tovarEni;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_second);

        // tovar recycler

         tovarRecycler = (RecyclerView) findViewById(R.id.tovarRecycler);


        // tovar redaktirovanie
        vibr = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        panelyoqa = (LinearLayout) findViewById(R.id.panelanim);
        current_status = 0;
        razmer = (TextView) findViewById(R.id.razmerpolzunok);
        povorot = (TextView) findViewById(R.id.povorot);
        timerHand = new Handler();
        oldi = new ItemFragment(R.drawable.futblka, new ItemFragment.eventZOOM() {
            @Override
            public void EVZ(int t) {
                vibr.vibrate(30);
                razmer.setText(Integer.toString(t));
                razmerPol[current_status]=t;
            }

            @Override
            public void EVR(int t) {
                vibr.vibrate(30);
                povorot.setText(Integer.toString(t));
                povorotMas[current_status]=t;
            }
        });
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame, oldi, "OLDI")
                .commit();

        findViewById(R.id.glaz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (current_status) {
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
        backanim = new Runnable() {
            @Override
            public void run() {
                panelyoqa.animate().translationX(-140).start();
            }
        };

        findViewById(R.id.burish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current_status == 0) {
                    getSupportFragmentManager()
                            .beginTransaction().replace(R.id.frame, yon).commit();

                    current_status = 1;
                    razmer.setText(Integer.toString(razmerPol[current_status]));
                    povorot.setText(Integer.toString(povorotMas[current_status]));
                } else if (current_status == 1) {
                    getSupportFragmentManager()
                            .beginTransaction().replace(R.id.frame, orqa).commit();
                    current_status = 2;
                    razmer.setText(Integer.toString(razmerPol[current_status]));
                    povorot.setText(Integer.toString(povorotMas[current_status]));

                } else if (current_status == 2) {
                    getSupportFragmentManager()
                            .beginTransaction().replace(R.id.frame, oldi).commit();
                    current_status = 0;
                    razmer.setText(Integer.toString(razmerPol[current_status]));
                    povorot.setText(Integer.toString(povorotMas[current_status]));


                }
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        //bu prosto initsalizovat qivoladi
        // tovar o`zgarganda fragmentdigi changeTovar(URI) funksiyasi chaqiriladi

        yon = new ItemFragment(R.drawable.futblkaikki, new ItemFragment.eventZOOM() {
            @Override
            public void EVZ(int t) {
                vibr.vibrate(30);
                razmer.setText(Integer.toString(t));
                razmerPol[current_status]=t;

            }

            @Override
            public void EVR(int t) {
                vibr.vibrate(30);
                povorot.setText(Integer.toString(t));
                povorotMas[current_status]=t;
            }
        });
        orqa = new ItemFragment(R.drawable.futblkaorqa, new ItemFragment.eventZOOM() {
            @Override
            public void EVZ(int t) {
                vibr.vibrate(30);
                razmer.setText(Integer.toString(t));
                razmerPol[current_status]=t;
            }

            @Override
            public void EVR(int t) {
                vibr.vibrate(30);
                povorot.setText(Integer.toString(t));
                povorotMas[current_status]=t;
            }
        });
    }

    float startAction_DownX = 0;
    float startAction_DownY = 0;
    float scoree = 0;
    float scoree1 = 0;
    boolean focRaz=false;
    boolean focRot=false;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float evX = event.getX();
        float evY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startAction_DownX = evX;
                startAction_DownY = evY;
                if (startAction_DownY + 20 > rotatC[1] && startAction_DownY < rotatC[1] + rotate_baland && startAction_DownX > rotatC[0] && startAction_DownX < rotatC[0] + rotate_eni) {
                focRot=true;
                    Log.d("HELlo","aaas");
                }
                else if (startAction_DownY + 20 > razmerC[1] && startAction_DownY < razmerC[1] + razmer_baland && startAction_DownX > razmerC[0] && startAction_DownX < razmerC[0] + razmer_eni) {
                focRaz=true;
                    Log.d("HELlo","sass");
                }

                scoree = startAction_DownX;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:


                break;
            case MotionEvent.ACTION_MOVE:
               if(focRot){
                    ItemFragment temp03 = (ItemFragment) getSupportFragmentManager().
                            findFragmentById(R.id.frame);

                    if (scoree + 50 < evX ) {
                        scoree = evX;
                        scoree += 50;
                        //  razmerPol[current_status]++;
                        // razmer.animate().scaleX(1.1f).scaleY(1.1f).setDuration(100).scaleY(0.9f).scaleX(0.9f).setDuration(100).start();
                        //razmer.setText(Integer.toString(razmerPol[current_status]));

                        temp03.rotationPlus();


                        Log.d("touchl", Integer.toString(rotatC[current_status]));
                    }
                    else if (scoree - 50 > evX ) {
                        scoree =evX ;
                        scoree -= 50;
                        //   razmerPol[current_status]--;
                        temp03.rotationMinus();

                        // razmer.setText(Integer.toString(razmerPol[current_status]));

                        Log.d("touchl", Integer.toString(rotatC[current_status]));

                    }
                    // Log.d("touchl",Float.toString(event.getX())+"x"+Float.toString(event.getY()));

                }

                if(focRaz){
                     ItemFragment temp03 = (ItemFragment) getSupportFragmentManager().
                            findFragmentById(R.id.frame);

                    if (scoree + 50 < evX && razmerPol[current_status] < 50) {
                        scoree = evX;
                       scoree += 50;
                      //  razmerPol[current_status]++;
                        // razmer.animate().scaleX(1.1f).scaleY(1.1f).setDuration(100).scaleY(0.9f).scaleX(0.9f).setDuration(100).start();
                        //razmer.setText(Integer.toString(razmerPol[current_status]));

                        temp03.plusSize();


                        Log.d("touchl", Integer.toString(razmerPol[current_status]));
                    }
                    else if (scoree - 50 > evX && razmerPol[current_status] > 0) {
                        scoree =evX ;
                        scoree -= 50;
                     //   razmerPol[current_status]--;
                        temp03.minusSize();

                       // razmer.setText(Integer.toString(razmerPol[current_status]));


                        Log.d("touchl", Integer.toString(razmerPol[current_status]));

                    }
                    // Log.d("touchl",Float.toString(event.getX())+"x"+Float.toString(event.getY()));

                }
                break;
            case MotionEvent.ACTION_UP:
                focRaz=false;
                focRot=false;
                scoree=0;
                break;
        }
        return true;

    }

    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        razmer.getLocationOnScreen(razmerC);

        razmer_baland = razmer.getHeight();
        razmer_eni = razmer.getWidth();

        povorot.getLocationOnScreen(rotatC);

        rotate_baland = povorot.getHeight();
        rotate_eni = povorot.getWidth();

        Log.d("touchhl", Integer.toString(razmerC[0]) + "xxx" + Integer.toString(razmerC[1]));
        Log.d("touchhl", Float.toString(razmer_eni) + "xxx" + Float.toString(razmer_baland));

        tovarRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        tovarBoyi = tovarRecycler.getHeight();
        Log.v("DATAA", Integer.toString(tovarBoyi));
        List<Integer> images = new ArrayList<>();
        images.add(1);
        images.add(1);
        images.add(1);
        images.add(1);
        images.add(1);
        images.add(1);
        SecondAdapter adapter = new SecondAdapter(this, images, tovarBoyi);
        tovarRecycler.setAdapter(adapter);

    }
}
