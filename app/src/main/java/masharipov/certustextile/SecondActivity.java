package masharipov.certustextile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.IntegerRes;
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

    ImageView tovarArrow, stickerArrow, styleArrow;
    ImageView collar1, collar2, collar3, collar4;
    Integer[] futbolkaCollar = {R.drawable.kruglivorot, R.drawable.shirokiyvorot, R.drawable.vorotpugi, R.drawable.vvorot};
    Integer[] maykaCollar = {R.drawable.mayka_krugliy, R.drawable.mayka_lodachka, R.drawable.mayka_vobrazniy};
    Integer[] poloCollar = {R.drawable.polo_stoykayoqa, R.drawable.poloyoqa, R.drawable.poloyoqa3};


    RecyclerView tovarRecycler, styleRecycler, stickerrecycler;
    TovarRecyclerAdapter tovarRecyclerAdapter;
    StyleRecyclerAdapter styleRecyclerAdapter;
    StickerRecyclerAdapter stickerRecyclerAdapter;
    List<RecyclerData> genderPicker, tovarData, styleData;
    LinearLayoutManager tovarLayoutManager, styleLayoutManager, stickerLayoutManager;
    List<StickerData> stickerData;

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
    int tovarBoyi, styleBoyi, stickerBoyi, strelkaBoyi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_second);

        //vibrannaya kategoriya
        Intent intent = getIntent();
        tableName = intent.getStringExtra("TABLENAME");

        // izvlekaem data s vibrannoy kategorii
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


        // tovar recycler
        tovarRecycler = (RecyclerView) findViewById(R.id.tovarRecycler);
        styleRecycler = (RecyclerView) findViewById(R.id.styleRecycler);
        stickerrecycler = (RecyclerView) findViewById(R.id.stickerRecycler);

        tovarArrow = (ImageView) findViewById(R.id.tovarPastStrelka);
        styleArrow = (ImageView) findViewById(R.id.stylePastStrelka);
        stickerArrow = (ImageView) findViewById(R.id.stickerPastStrelka);

        tovarArrow.setOnClickListener(this);
        styleArrow.setOnClickListener(this);
        stickerArrow.setOnClickListener(this);

        //vorotniki
        collar1 = (ImageView) findViewById(R.id.yoqa1);
        collar2 = (ImageView) findViewById(R.id.yoqa2);
        collar3 = (ImageView) findViewById(R.id.yoqa3);
        collar4 = (ImageView) findViewById(R.id.yoqa4);

        collar1.setOnClickListener(this);
        collar2.setOnClickListener(this);
        collar3.setOnClickListener(this);
        collar4.setOnClickListener(this);

        if (tableName.equals("Mayka") || tableName.equals("Polo")) {
            collar4.setVisibility(View.GONE);
        }

        switch (tableName) {
            case "Futbolka":
                setCollarImages(futbolkaCollar);
                break;
            case "Mayka":
                setCollarImages(maykaCollar);
                break;
            case "Polo":
                setCollarImages(poloCollar);
                break;
        }


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
    }
    boolean keyStart=true;
    @Override
    public void onStart() {
        super.onStart();
        //bu prosto initsalizovat qivoladi
        // tovar o`zgarganda fragmentdigi changeTovar(URI) funksiyasi chaqiriladi
        if (keyStart){
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
        keyStart=false;}

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

    boolean keyfirst = true;

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (keyfirst) {
            razmer.getLocationOnScreen(razmerC);

            razmer_baland = razmer.getHeight();
            razmer_eni = razmer.getWidth();

            povorot.getLocationOnScreen(rotatC);

            rotate_baland = povorot.getHeight();
            rotate_eni = povorot.getWidth();

            Log.d("touchhl", Integer.toString(razmerC[0]) + "xxx" + Integer.toString(razmerC[1]));
            Log.d("touchhl", Float.toString(razmer_eni) + "xxx" + Float.toString(razmer_baland));


            strelkaBoyi = tovarArrow.getHeight();

            // dlya togo chtobi v recycler pomeshalis 3 elementa
            tovarBoyi = tovarRecycler.getHeight();
            Log.v("DATAA", "Init tovat boyi = " + Integer.toString(tovarBoyi));
            initTovarRecycler();

            styleBoyi = styleRecycler.getHeight();
            initStyleRecycler();

            stickerBoyi = stickerrecycler.getHeight();
            initStickerRecycler();
            keyfirst = false;
        }
    }

    private void initTovarRecycler() {
        tovarData = genderPicker;

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
        if (genderPicker.size() > 3) {
            tovarArrow.setVisibility(View.VISIBLE);
            tovarRecyclerAdapter.setImageParams(tovarBoyi);
        } else {
            tovarArrow.setVisibility(View.GONE);
            tovarRecyclerAdapter.setImageParams(tovarBoyi + strelkaBoyi);
        }

        tovarLayoutManager = new LinearLayoutManager(getApplicationContext());
        tovarRecycler.setLayoutManager(tovarLayoutManager);
        tovarRecycler.setAdapter(tovarRecyclerAdapter);
    }

    private void initStyleRecycler() {
        styleArrow.setVisibility(View.GONE);
        List<RecyclerData> data = new ArrayList<>();
        styleRecyclerAdapter = new StyleRecyclerAdapter(this, data, styleBoyi, new StyleRecyclerAdapter.clickListener() {
            @Override
            public void onItemClick(ImageView img, int position, RecyclerData tanlanganTovar) {
                String oldiUri = tanlanganTovar.getImageUri("front");
                String orqaUri = tanlanganTovar.getImageUri("back");
                String yonUri = tanlanganTovar.getImageUri("side");
                oldi=null;
                oldi = new ItemFragment(Uri.parse(oldiUri), new ItemFragment.eventZOOM() {
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
                yon=null;
                yon = new ItemFragment(Uri.parse(yonUri), new ItemFragment.eventZOOM() {
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
                orqa=null;
                orqa = new ItemFragment(Uri.parse(orqaUri), new ItemFragment.eventZOOM() {
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

                switch (current_status) {
                    case 0:

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame, oldi, "OLDI")
                            .commit();
                    break;

                    case 1:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame, yon, "YON")
                                .commit();
                        break;

                    case 2:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame, orqa, "ORQA")
                                .commit();
                        break;

                }
                // some code when style item clicked
            }
        });
        styleLayoutManager = new LinearLayoutManager(getApplicationContext());
        styleRecycler.setLayoutManager(styleLayoutManager);
        styleRecycler.setAdapter(styleRecyclerAdapter);
    }

    private void initStickerRecycler() {
        List<StickerData> list = new ArrayList<>();
        list = cDB.getStickersFromDB();
        stickerData = list;
        Log.v("SECONDACTIVITY", "SIZE OF STICKER ITEMS == " + list.size());
        stickerRecyclerAdapter = new StickerRecyclerAdapter(this, list, stickerBoyi, new StickerRecyclerAdapter.clickListener() {
            @Override
            public void onItemClick(ImageView img, int position, String str) {

                Log.v("SECONDACTIVITY", str);
                switch (current_status) {
                    case 0:
                        oldi.changeStickerUri(str);
                        break;
                    case 1:
                        yon.changeStickerUri(str);
                        break;
                    case 2:
                        orqa.changeStickerUri(str);
                        break;
                }


            }
        });
        if (list.size() > 3) {
            stickerArrow.setVisibility(View.VISIBLE);
            stickerRecyclerAdapter.setImageParams(stickerBoyi);
        } else {
            stickerArrow.setVisibility(View.GONE);
            stickerRecyclerAdapter.setImageParams(stickerBoyi + strelkaBoyi);
        }
        stickerLayoutManager = new LinearLayoutManager(getApplicationContext());
        stickerrecycler.setLayoutManager(stickerLayoutManager);
        stickerrecycler.setAdapter(stickerRecyclerAdapter);
    }

    public List<RecyclerData> getStyleData(String id, String collar, String gender) {
        List<RecyclerData> finalData = new ArrayList<>();
        String tmpURI = null; // chtobi brat po odnomu tovaru iz odinakovix stiley (esli sprosyat sdelaem) poka ne rabotaet
        if (initialData.size() != 0) {
            for (int j = 0; j < initialData.size(); j++) {
                if (initialData.get(j).getID().equals(id) && initialData.get(j).getCollar().equals(collar) && initialData.get(j).getGender().equals(gender)) {
                    initialData.get(j).setType(tableName);
                    finalData.add(initialData.get(j));
                }
            }
        }
        Log.v("SECONDACTIVITY", "SIZE OF STYLE ITEMS == " + finalData.size());
        if (finalData.size() > 3) {
            styleArrow.setVisibility(View.VISIBLE);
            styleRecyclerAdapter.setImageParams(styleBoyi);
        } else {
            styleArrow.setVisibility(View.GONE);
            styleRecyclerAdapter.setImageParams(styleBoyi + strelkaBoyi);
        }
        styleData = finalData;
        return finalData;
    }

    public List<RecyclerData> getTovarData(String gender) {
        List<RecyclerData> finalData = new ArrayList<>();
        if (initialData.size() != 0) {
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
        }
        if (finalData.size() > 3) {
            tovarArrow.setVisibility(View.VISIBLE);
            tovarRecyclerAdapter.setImageParams(tovarBoyi);

        } else {
            tovarArrow.setVisibility(View.GONE);
            tovarRecyclerAdapter.setImageParams(tovarBoyi + strelkaBoyi);
        }
        Log.v("DATAA", "tovat boyi after gender choose = " + Integer.toString(tovarBoyi));

        tovarData = finalData;
        return finalData;
    }


    public void setCollarImages(Integer[] images) {
        collar1.setImageResource(images[0]);
        collar2.setImageResource(images[1]);
        collar3.setImageResource(images[2]);
        if (images.length == 4)
            collar4.setImageResource(images[3]);
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
            case R.id.tovarPastStrelka:
                if (tovarLayoutManager.findLastCompletelyVisibleItemPosition() != tovarData.size() - 1) {
                    tovarRecycler.scrollToPosition(tovarLayoutManager.findLastCompletelyVisibleItemPosition() + 1);
                }
                break;
            case R.id.stylePastStrelka:
                if (styleLayoutManager.findLastCompletelyVisibleItemPosition() != styleData.size() - 1) {
                    styleRecycler.scrollToPosition(styleLayoutManager.findLastCompletelyVisibleItemPosition() + 1);
                }
                break;
            case R.id.stickerPastStrelka:
                if (stickerLayoutManager.findLastCompletelyVisibleItemPosition() != stickerData.size() - 1) {
                    stickerrecycler.scrollToPosition(stickerLayoutManager.findLastCompletelyVisibleItemPosition() + 1);
                }
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
            if (genderPicker.size() > 3) {
                tovarArrow.setVisibility(View.VISIBLE);
                tovarRecyclerAdapter.setImageParams(tovarBoyi);
            } else {
                tovarArrow.setVisibility(View.GONE);
                tovarRecyclerAdapter.setImageParams(tovarBoyi + strelkaBoyi);
            }
        } else
            super.onBackPressed();
    }
}
