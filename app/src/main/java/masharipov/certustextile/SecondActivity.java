package masharipov.certustextile;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

import masharipov.certustextile.edit.RecyclerData;
import masharipov.certustextile.stickeradd.StickerData;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout panelyoqa;
    Handler timerHand;
    Runnable backanim;
    int current_status, old_status;
    TextView razmer, povorot;
    ItemFragment oldi, yon, orqa;
    float razmer_baland = 0, razmer_eni = 0;
    float rotate_baland = 0, rotate_eni = 0;
    int razmerC[] = {0, 0};
    int rotatC[] = {0, 0};
    float povorotX = 0, povorotY = 0;
    int razmerPol[] = {15, 15, 15};
    int povorotMas[] = {0, 0, 0, 0};
    Vibrator vibr;
    RecyclerView tovarRecycler, styleRecycler, stickerrecycler;
    TovarRecyclerAdapter tovarRecyclerAdapter;
    ImageView tovarArrow, stickerArrow, styleArrow;
    StyleRecyclerAdapter styleRecyclerAdapter;
    StickerRecyclerAdapter stickerRecyclerAdapter;
    List<RecyclerData> genderPicker;
    boolean isGenderPicked = false;
    String[] genderNames = {"male", "female", "boy", "girl"};
    CertusDatabase cDB;
    String tableName;
    String selectedItemID, selectedItemCollar, selectedGender;
    int ifClickedImagePositionNotChangedDoNotChangeStyleList = -1, ifStickerPositionIsTheSameDoNotChange = -1;
    List<RecyclerData> initialData; // polnaya baza po vibrannoy categorii tovara

    // yoqa panel
    Integer[] collarChooser = {R.id.yoqa1, R.id.yoqa2, R.id.yoqa3, R.id.yoqa4};
    String[] collarName = {"collar1", "collar2", "collar3", "collar4"};
    int count;

    // recycler uchun
    int tovarBoyi, styleBoyi, stickerBoyi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_second);

        Intent intent = getIntent();
        tableName = intent.getStringExtra("TABLENAME");

        cDB = new CertusDatabase(this);
        initialData = cDB.getTovarFromDB(tableName);

        // nacalnie znacheniya
        if (initialData != null && initialData.size() != 0) {
            selectedItemID = initialData.get(0).getID();
            selectedItemCollar = initialData.get(0).getCollar();
        }

        //snachala nujno vibrat pol, poetomu v recycler gruzim pol
        genderPicker = new ArrayList<>();
        Integer[] gArray = {R.drawable.gendermuj, R.drawable.genderjen, R.drawable.genderdetmuj, R.drawable.genderdetjen};
        for (Integer aGArray : gArray) {
            RecyclerData gData = new RecyclerData();
            gData.setGenderImageResourse(aGArray);
            genderPicker.add(gData);
        }
        Log.v("ddd", "ddd");

        // tovar recycler
        tovarRecycler = (RecyclerView) findViewById(R.id.tovarRecycler);
        tovarArrow = (ImageView) findViewById(R.id.tovarPastStrelka);
        tovarArrow.setVisibility(View.GONE);
        styleRecycler = (RecyclerView) findViewById(R.id.styleRecycler);
        stickerrecycler = (RecyclerView) findViewById(R.id.stickerRecycler);

        // tovar redaktirovanie
        vibr = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        panelyoqa = (LinearLayout) findViewById(R.id.panelanim);
        current_status = 0;
        razmer = (TextView) findViewById(R.id.razmerpolzunok);
        povorot = (TextView) findViewById(R.id.povorot);
        timerHand = new Handler();
        oldi = new ItemFragment(R.drawable.expanat, new ItemFragment.eventZOOM() {
            @Override
            public void EVZ(int t) {
                vibr.vibrate(30);
                razmer.setText(Integer.toString(t));
                razmerPol[current_status] = t;
            }

            @Override
            public void EVR(int t) {
                vibr.vibrate(30);
                povorot.setText(Integer.toString(t));
                povorotMas[current_status] = t;
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
                        oldi.changeSticker(R.drawable.nakleka);
                        break;
                    case 1:
                        yon.changeSticker(R.drawable.naka2);

                        break;
                    case 2:
                        orqa.changeSticker(R.drawable.naka1);
                        break;
                }

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

        findViewById(R.id.yoqa1).setOnClickListener(this);
        findViewById(R.id.yoqa2).setOnClickListener(this);
        findViewById(R.id.yoqa3).setOnClickListener(this);
        findViewById(R.id.yoqa4).setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        //bu prosto initsalizovat qivoladi
        // tovar o`zgarganda fragmentdigi changeTovar(URI) funksiyasi chaqiriladi

        yon = new ItemFragment(R.drawable.expanat, new ItemFragment.eventZOOM() {
            @Override
            public void EVZ(int t) {
                vibr.vibrate(30);
                razmer.setText(Integer.toString(t));
                razmerPol[current_status] = t;

            }

            @Override
            public void EVR(int t) {
                vibr.vibrate(30);
                povorot.setText(Integer.toString(t));
                povorotMas[current_status] = t;
            }
        });
        orqa = new ItemFragment(R.drawable.expanatorqa, new ItemFragment.eventZOOM() {
            @Override
            public void EVZ(int t) {
                vibr.vibrate(30);
                razmer.setText(Integer.toString(t));
                razmerPol[current_status] = t;
            }

            @Override
            public void EVR(int t) {
                vibr.vibrate(30);
                povorot.setText(Integer.toString(t));
                povorotMas[current_status] = t;
            }
        });
    }

    float startAction_DownX = 0;
    float startAction_DownY = 0;
    float scoree = 0;
    float scoree1 = 0;
    boolean focRaz = false;
    boolean focRot = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float evX = event.getX();
        float evY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startAction_DownX = evX;
                startAction_DownY = evY;
                if (startAction_DownY + 20 > rotatC[1] && startAction_DownY < rotatC[1] + rotate_baland && startAction_DownX > rotatC[0] && startAction_DownX < rotatC[0] + rotate_eni) {
                    focRot = true;
                    Log.d("HELlo", "aaas");
                } else if (startAction_DownY + 20 > razmerC[1] && startAction_DownY < razmerC[1] + razmer_baland && startAction_DownX > razmerC[0] && startAction_DownX < razmerC[0] + razmer_eni) {
                    focRaz = true;
                    Log.d("HELlo", "sass");
                }
                scoree = startAction_DownX;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if (focRot) {
                    ItemFragment temp03 = (ItemFragment) getSupportFragmentManager().
                            findFragmentById(R.id.frame);

                    if (scoree + 50 < evX) {
                        scoree = evX;
                        scoree += 50;
                        //  razmerPol[current_status]++;
                        // razmer.animate().scaleX(1.1f).scaleY(1.1f).setDuration(100).scaleY(0.9f).scaleX(0.9f).setDuration(100).start();
                        //razmer.setText(Integer.toString(razmerPol[current_status]));

                        temp03.rotationPlus();

                    } else if (scoree - 50 > evX) {
                        scoree = evX;
                        scoree -= 50;
                        //   razmerPol[current_status]--;
                        temp03.rotationMinus();

                        // razmer.setText(Integer.toString(razmerPol[current_status]));

                    }
                    // Log.d("touchl",Float.toString(event.getX())+"x"+Float.toString(event.getY()));

                }

                if (focRaz) {
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
                    } else if (scoree - 50 > evX && razmerPol[current_status] > 0) {
                        scoree = evX;
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
                focRaz = false;
                focRot = false;
                scoree = 0;
                break;
        }
        return true;

    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        razmer.getLocationOnScreen(razmerC);

        razmer_baland = razmer.getHeight();
        razmer_eni = razmer.getWidth();

        povorot.getLocationOnScreen(rotatC);

        rotate_baland = povorot.getHeight();
        rotate_eni = povorot.getWidth();

        Log.d("touchhl", Integer.toString(razmerC[0]) + "xxx" + Integer.toString(razmerC[1]));
        Log.d("touchhl", Float.toString(razmer_eni) + "xxx" + Float.toString(razmer_baland));


        // dlya togo chtobi v recycler pomeshalis 3 elementa
        tovarBoyi = tovarRecycler.getHeight();
        initTovarRecycler();

        styleBoyi = styleRecycler.getHeight();
        initStyleRecycler();

        stickerBoyi = stickerrecycler.getHeight();
        initStickerRecycler();
    }

    private void initTovarRecycler() {
        tovarRecyclerAdapter = new TovarRecyclerAdapter(this, genderPicker, tovarBoyi, new TovarRecyclerAdapter.clickListener() {
            @Override
            public void onItemClick(ImageView img, int position, List<RecyclerData> dataList) {
                if (!isGenderPicked) {
                    selectedGender = genderNames[position];
                    tovarRecyclerAdapter.changeList(getTovarData(selectedGender));
                    styleRecyclerAdapter.changeStyleList(getStyleData(selectedItemID, selectedItemCollar, selectedGender));
                    isGenderPicked = true;
                    return;
                }
                if (position != ifClickedImagePositionNotChangedDoNotChangeStyleList) {
                    selectedItemID = dataList.get(position).getID();
                    styleRecyclerAdapter.changeStyleList(getStyleData(selectedItemID, selectedItemCollar, selectedGender));
                    ifClickedImagePositionNotChangedDoNotChangeStyleList = position;
                }
                panelyoqa.animate().translationX(140).start();
                timerHand.postDelayed(backanim, 3000);
            }
        });
        tovarRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        tovarRecycler.setAdapter(tovarRecyclerAdapter);
    }

    private void initStyleRecycler() {
        List<RecyclerData> data = new ArrayList<>();
        styleRecyclerAdapter = new StyleRecyclerAdapter(this, data, styleBoyi, new StyleRecyclerAdapter.clickListener() {
            @Override
            public void onItemClick(ImageView img, int position) {
                // some code when style item clicked
            }
        });
        styleRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        styleRecycler.setAdapter(styleRecyclerAdapter);
    }

    private void initStickerRecycler() {
        List<StickerData> list = new ArrayList<>();
        list = cDB.getStickersFromDB();
        Log.v("SECONDACTIVITY", "SIZE OF STICKER ITEMS == " + list.size());
        stickerRecyclerAdapter = new StickerRecyclerAdapter(this, list, stickerBoyi, new StickerRecyclerAdapter.clickListener() {
            @Override
            public void onItemClick(ImageView img, int position) {
                if (ifStickerPositionIsTheSameDoNotChange != position) {
                    Log.v("SECONDACTIVITY", "STICKERITEM AT  " + Integer.toString(position) + " CLICKED");
                    switch (current_status) {
                        case 0:
                            oldi.changeSticker(R.drawable.nakleka);
                            break;
                        case 1:
                            yon.changeSticker(R.drawable.naka2);

                            break;
                        case 2:
                            orqa.changeSticker(R.drawable.naka1);
                            break;
                    }
                    ifStickerPositionIsTheSameDoNotChange = position;
                    old_status = current_status;
                }
            }
        });
        stickerrecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        stickerrecycler.setAdapter(stickerRecyclerAdapter);
    }

    public List<RecyclerData> getStyleData(String id, String collar, String gender) {
        List<RecyclerData> finalData = new ArrayList<>();
        String tmpURI = null; // chtobi brat po odnomu tovaru iz odinakovix stiley (esli sprosyat sdelaem) poka ne rabotaet
        for (int j = 0; j < initialData.size(); j++) {
            if (initialData.get(j).getID().equals(id) && initialData.get(j).getCollar().equals(collar) && initialData.get(j).getGender().equals(gender)) {
                initialData.get(j).setType(tableName);
                finalData.add(initialData.get(j));
            }
        }
        Log.v("SECONDACTIVITY", "SIZE OF STYLE ITEMS == " + finalData.size());
        return finalData;
    }

    public List<RecyclerData> getTovarData(String gender) {
        List<RecyclerData> finalData = new ArrayList<>();
        String tmpID = initialData.get(0).getID();
        int genderFound = 0;
        for (int j = 0; j < initialData.size(); j++) {
            if (initialData.get(j).getID().equals(tmpID) && initialData.get(j).getGender().equals(gender) && genderFound == 0) {
                initialData.get(j).setType(tableName);
                finalData.add(initialData.get(j));
                genderFound++;
            } else {
                if (!initialData.get(j).getID().equals(tmpID)) {
                    genderFound = 0;
                    tmpID = initialData.get(j).getID();

                }
                if (initialData.get(j).getID().equals(tmpID) && initialData.get(j).getGender().equals(gender) && genderFound == 0) {
                    initialData.get(j).setType(tableName);
                    finalData.add(initialData.get(j));
                    genderFound++;
                }
            }
        }
        return finalData;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yoqa1:
                selectedItemCollar = "collar1";
                break;
            case R.id.yoqa2:
                selectedItemCollar = "collar2";
                break;
            case R.id.yoqa3:
                selectedItemCollar = "collar3";
                break;
            case R.id.yoqa4:
                selectedItemCollar = "collar4";
                break;
        }
        styleRecyclerAdapter.changeStyleList(getStyleData(selectedItemID, selectedItemCollar, selectedGender));
    }

    @Override
    public void onBackPressed() {
        if (isGenderPicked) {
            isGenderPicked = false;
            tovarRecyclerAdapter.changeList(genderPicker);
            styleRecyclerAdapter.changeStyleList(new ArrayList<RecyclerData>());
        } else
            super.onBackPressed();
    }
}
