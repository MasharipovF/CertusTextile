package masharipov.certustextile;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.IntegerRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import masharipov.certustextile.edit.RecyclerData;
import masharipov.certustextile.stickeradd.StickerData;
import masharipov.certustextile.tpriew.Tview;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout panelyoqa;
    Handler timerHand;
    Runnable backanim;
    int current_status, old_status;
    TextView razmer, povorot;
    ItemFragment oldi, yon, orqa;
    float razmer_baland = 0, razmer_eni = 0;
    float change_baland = 0, change_eni = 0;
    float rotate_baland = 0, rotate_eni = 0;
    int razmerC[] = {0, 0};
    int rotatC[] = {0, 0};
    int changfeC[] = {0, 0};
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
    List<RecyclerData> genderPickerList, tovarData, styleData;
    List<RecyclerData> maleList = null, femaleList = null, boyList = null, girlList = null;
    List<List<RecyclerData>> wholeList;
    LinearLayoutManager tovarLayoutManager, styleLayoutManager, stickerLayoutManager;
    List<StickerData> stickerData;
    boolean isGenderPicked = false;

    String[] genderNames = {"male", "female", "boy", "girl"};
    Integer[] gArray = {R.drawable.gendermuj, R.drawable.genderjen, R.drawable.genderdetmuj, R.drawable.genderdetjen};

    CertusDatabase cDB;
    String tableName;
    String selectedItemID, selectedItemCollar, selectedGender;
    int ifClickedImagePositionNotChangedDoNotChangeStyleList = -1, ifStickerPositionIsTheSameDoNotChange = -1;
    List<RecyclerData> initialData; // polnaya baza po vibrannoy categorii tovara
    List<RecyclerData> tmpTovarList, tmpStyleList;
    String selectedTovarID;
    List<RecyclerData> selectedGenderList;
    ImageView sidechange;
    RecyclerData initData;
    // yoqa panel
    Integer[] collarChooser = {R.id.yoqa1, R.id.yoqa2, R.id.yoqa3, R.id.yoqa4};
    String[] collarName = {"collar1", "collar2", "collar3", "collar4"};
    int count;

    // recycler uchun
    int tovarBoyi, styleBoyi, stickerBoyi, strelkaBoyi;
    ProgressDialog pd;

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
        Log.v("DATAA", "SIZE OF DATA = " + Integer.toString(initialData.size()));
        wholeList = new ArrayList<>();


        // razbirayem bazu po POLU
        for (RecyclerData mData : initialData) {
            switch (mData.getGender()) {
                case "male":
                    if (maleList == null)
                        maleList = new ArrayList<>();
                    maleList.add(mData);
                    break;
                case "female":
                    if (femaleList == null)
                        femaleList = new ArrayList<>();
                    femaleList.add(mData);
                    break;
                case "boy":
                    if (boyList == null)
                        boyList = new ArrayList<>();
                    boyList.add(mData);
                    break;
                case "girl":
                    if (girlList == null)
                        girlList = new ArrayList<>();
                    girlList.add(mData);
                    break;
            }
        }

        genderPickerList = new ArrayList<>();
        if (maleList != null && maleList.size() != 0) {
            Log.v("DATAA", "MALE  = " + Integer.toString(maleList.size()));
            RecyclerData gData = new RecyclerData();
            gData.setGenderImageResourse(gArray[0]);
            genderPickerList.add(gData);
            wholeList.add(maleList);
        }
        if (femaleList != null && femaleList.size() != 0) {
            Log.v("DATAA", "FEMALE  = " + Integer.toString(femaleList.size()));
            RecyclerData gData = new RecyclerData();
            gData.setGenderImageResourse(gArray[1]);
            genderPickerList.add(gData);
            wholeList.add(femaleList);
        }
        if (boyList != null && boyList.size() != 0) {
            Log.v("DATAA", "BOY  = " + Integer.toString(boyList.size()));
            RecyclerData gData = new RecyclerData();
            gData.setGenderImageResourse(gArray[2]);
            genderPickerList.add(gData);
            wholeList.add(boyList);
        }
        if (girlList != null && girlList.size() != 0) {
            Log.v("DATAA", "GIRL  = " + Integer.toString(girlList.size()));
            RecyclerData gData = new RecyclerData();
            gData.setGenderImageResourse(gArray[3]);
            genderPickerList.add(gData);
            wholeList.add(girlList);
        }

        // nacalnie znacheniya
       /* if (initialData != null && initialData.size() != 0) {
            selectedItemID = initialData.get(0).getID();
            selectedItemCollar = initialData.get(0).getCollar();
        }*/

        //snachala nujno vibrat pol, poetomu v recycler gruzim pol

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
        sidechange = (ImageView) findViewById(R.id.changeside);
        povorot = (TextView) findViewById(R.id.povorot);
        timerHand = new Handler();
        findViewById(R.id.shot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (current_status) {
                    case 0:
                        oldi = null;
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
                            @Override
                            public void nextFrag() {
                                vibr.vibrate(30);
                                nextFragment();

                            }

                            @Override
                            public void prevFrag() {
                                vibr.vibrate(30);
                                prevFragment();

                            }

                        });
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame, oldi, "YON")
                                .commit();
                        break;

                    case 1:

                        yon = null;
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
                            @Override
                            public void nextFrag() {
                                vibr.vibrate(30);
                                nextFragment();

                            }

                            @Override
                            public void prevFrag() {
                                vibr.vibrate(30);
                                prevFragment();


                            }
                        });

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame, yon, "YON")
                                .commit();
                        break;

                    case 2:
                        orqa = null;
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
                            @Override
                            public void nextFrag() {
                                vibr.vibrate(30);

                                nextFragment();

                            }

                            @Override
                            public void prevFrag() {
                                vibr.vibrate(30);
                                prevFragment();


                            }

                        });
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame, orqa, "ORQA")
                                .commit();
                        break;

                }
            }
        });

        findViewById(R.id.glaz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (current_status){
                    case 0:
                        oldi.setTEG("oldi");
                        oldi.artcache();

                        break;
                    case 1:
                        yon.setTEG("yon");
                        yon.artcache();
                        break;
                    case 2:
                        orqa.setTEG("orqa");
                        orqa.artcache();
                        break;

                }
                pd = new ProgressDialog(SecondActivity.this);

                pd.setMessage("Пожалуйста подождите!");
                pd.show();
                Thread closeActivity = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);

                            Intent tview = new Intent(SecondActivity.this, Tview.class);
                            tview.putExtra("oldi",oldi.isCached());
                            tview.putExtra("yon",yon.isCached());
                            tview.putExtra("orqa",orqa.isCached());
                            tview.putExtra("tag",TAG);

                            if (!(oldi.getUriS() ==null)){
                                tview.putExtra("firstS",oldi.getUriS().toString());
                            }
                            else tview.putExtra("firstS","1");
                            if (!(yon.getUriS() ==null)){
                                tview.putExtra("secS",yon.getUriS().toString());
                            }
                            else tview.putExtra("secS","1");
                            if (!(orqa.getUriS() ==null)){
                                tview.putExtra("thirS",orqa.getUriS().toString());
                            }
                            else tview.putExtra("thirS","1");
                            if (!oldi.isCached()){
                                tview.putExtra("oldiUri",oldi.getUriT().toString());
                                Log.d("putt",oldi.getUriT().toString());
                            }
                            if (!yon.isCached()){
                                tview.putExtra("yonUri",yon.getUriT().toString());
                                Log.d("putt",yon.getUriT().toString());
                            }
                            if (!orqa.isCached()){
                                tview.putExtra("orqaUri",orqa.getUriT().toString());
                            }
                            pd.dismiss();

                            startActivity(tview);

                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                    }
                });
                closeActivity.start();
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
                nextFragment();
            }
        });
        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(lastFileModified( Environment.getExternalStorageDirectory().toString() + "/Certus" + "/Saved/")), "image/*");
                startActivity(intent);

            }
        });
    }

    boolean keyStart = true;
    public static File lastFileModified(String dir) {
        File fl = new File(dir);
        File[] files = fl.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isFile();
            }
        });
        long lastMod = Long.MIN_VALUE;
        File choice = null;
        for (File file : files) {
            if (file.lastModified() > lastMod) {
                choice = file;
                lastMod = file.lastModified();
            }
        }
        return choice;
    }
    @Override
    public void onStart() {
        super.onStart();
        //bu prosto initsalizovat qivoladi
        // tovar o`zgarganda fragmentdigi changeTovar(URI) funksiyasi chaqiriladi


    }

    float startAction_DownX = 0;
    float startAction_DownY = 0;
    float scoree = 0;
    float scoree1 = 0;
    boolean focRaz = false;
    boolean focRot = false;
    boolean focchan = false;

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
                } else if (startAction_DownY > changfeC[1] && startAction_DownY < changfeC[1] + change_baland && startAction_DownX > changfeC[0] && startAction_DownX < changfeC[0] + change_eni)
                { Log.d("touchl",scoree+"---"+evX+"  DOT");
                    focchan = true;
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
                if (focchan) {
                    Log.d("touchl",scoree+"---"+evX+"  move");
                    if (scoree +220 < evX) {
                        scoree = evX;

                        vibr.vibrate(30);//  razmerPol[current_status]++;
                        // razmer.animate().scaleX(1.1f).scaleY(1.1f).setDuration(100).scaleY(0.9f).scaleX(0.9f).setDuration(100).start();
                        //razmer.setText(Integer.toString(razmerPol[current_status]));
                        nextFragment();
                        Log.d("touchl",scoree+"---"+evX+"  NEXT");

                    } else if (scoree - 220 > evX) {
                        scoree = evX;

                        vibr.vibrate(30);
                        //   razmerPol[current_status]--;
                        Log.d("touchl",scoree+"---"+evX+"  PREV");

                        // razmer.setText(Integer.toString(razmerPol[current_status]));

                        prevFragment();

                    }


                }
                break;
            case MotionEvent.ACTION_UP:
                focRaz = false;
                focRot = false;
                focchan = false;
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


            sidechange.getLocationOnScreen(changfeC);
            change_baland=sidechange.getHeight();
            change_eni=sidechange.getWidth();

            povorot.getLocationOnScreen(rotatC);

            rotate_baland = povorot.getHeight();
            rotate_eni = povorot.getWidth();

            Log.d("touchhl", Integer.toString(razmerC[0]) + "xxx" + Integer.toString(razmerC[1]));
            Log.d("touchhl", Float.toString(razmer_eni) + "xxx" + Float.toString(razmer_baland));


            strelkaBoyi = tovarArrow.getHeight();

            // dlya togo chtobi v recycler pomeshalis 3 elementa
            tovarBoyi = tovarRecycler.getHeight();
            initTovarRecycler();

            styleBoyi = styleRecycler.getHeight();
            initStyleRecycler();

            stickerBoyi = stickerrecycler.getHeight();
            initStickerRecycler();
            keyfirst = false;
        }
    }

    private int currentGender;

    private void initTovarRecycler() {
        tovarArrow.setVisibility(View.GONE);

        tovarData = genderPickerList;
        tovarRecyclerAdapter = new TovarRecyclerAdapter(this, genderPickerList, tovarBoyi, new TovarRecyclerAdapter.clickListener() {
            @Override
            public void onItemClick(ImageView img, int position, List<RecyclerData> dataList) {
                if (!isGenderPicked) {
                    isGenderPicked = true;
                    currentGender = position;
                    selectedGenderList = wholeList.get(position);
                    tmpTovarList = getTovarData(selectedGenderList);
                    selectedTovarID = tmpTovarList.get(0).getID();
                    //tmpStyleList = getStyleData(selectedGenderList, selectedTovarID, null);
                    tmpStyleList = getSyledataWithfirstCollar(selectedGenderList, selectedTovarID);

                    tovarRecyclerAdapter.changeList(tmpTovarList);
                    styleRecyclerAdapter.changeStyleList(tmpStyleList);

                    ifClickedImagePositionNotChangedDoNotChangeStyleList = 0;

                    setItemtoFragment(styleRecyclerAdapter.getList().get(0));
                  /*  selectedGender = genderNames[position];
                    tovarRecyclerAdapter.changeList(getTovarData(selectedGender));
                    styleRecyclerAdapter.changeStyleList(getStyleData(selectedItemID, selectedItemCollar, selectedGender));
                    if (initData != null)
                        setItemtoFrsgment(initData);*/
                } else {
                    if (position != ifClickedImagePositionNotChangedDoNotChangeStyleList) {
                        ifClickedImagePositionNotChangedDoNotChangeStyleList = position;
                        // tmpStyleList = getStyleData(wholeList.get(currentGender), tmpTovarList.get(position).getID(), null);
                        selectedTovarID = tmpTovarList.get(position).getID();
                        tmpStyleList = getSyledataWithfirstCollar(selectedGenderList, selectedTovarID);
                        styleRecyclerAdapter.changeStyleList(tmpStyleList);
                        setItemtoFragment(styleRecyclerAdapter.getList().get(0));

                   /* selectedItemID = dataList.get(position).getID();
                    styleRecyclerAdapter.changeStyleList(getStyleData(selectedItemID, selectedItemCollar, selectedGender));
                    if (getStyleData(selectedItemID, selectedItemCollar, selectedGender).get(0) != null)
                        setItemtoFrsgment(getStyleData(selectedItemID, selectedItemCollar, selectedGender).get(0));*/
                    }
                }
                panelyoqa.animate().translationX(140).start();

                timerHand.removeCallbacks(backanim);
                timerHand.postDelayed(backanim, 2000);
                current_status = 0;
            }
        });

        setVisibilityOfTovarArrow(genderPickerList);


        tovarLayoutManager = new LinearLayoutManager(getApplicationContext());
        tovarRecycler.setLayoutManager(tovarLayoutManager);
        tovarRecycler.setAdapter(tovarRecyclerAdapter);
    }

    String oldiUri;
    String orqaUri;
    String yonUri;

    private void initStyleRecycler() {
        styleArrow.setVisibility(View.GONE);

        List<RecyclerData> data = new ArrayList<>();
        styleRecyclerAdapter = new StyleRecyclerAdapter(this, data, styleBoyi, new StyleRecyclerAdapter.clickListener() {
            @Override
            public void onItemClick(ImageView img, int position, RecyclerData tanlanganTovar) {
                setItemtoFragment(tanlanganTovar);
                // some code when style item clicked
            }
        });
        styleLayoutManager = new LinearLayoutManager(getApplicationContext());
        styleRecycler.setLayoutManager(styleLayoutManager);
        styleRecycler.setAdapter(styleRecyclerAdapter);
    }

    private void initStickerRecycler() {
        stickerArrow.setVisibility(View.GONE);
        List<StickerData> list = new ArrayList<>();
        list = cDB.getStickersFromDB();
        stickerData = list;
        Log.v("DATAA", "SIZE OF STICKER ITEMS == " + list.size());
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
        setVisibilityOfStickerArrow(list);
        stickerLayoutManager = new LinearLayoutManager(getApplicationContext());
        stickerrecycler.setLayoutManager(stickerLayoutManager);
        stickerrecycler.setAdapter(stickerRecyclerAdapter);
    }

    boolean somekey = false;

    public List<RecyclerData> getTovarData(List<RecyclerData> list) {
        List<RecyclerData> finalData = new ArrayList<>();
        String tmpID;
        int counter, i, k;
        // TODO bazadan oliwi togirlaw kere
        for (i = 0; i < list.size(); i++) {
            RecyclerData mItem = list.get(i);
            tmpID = mItem.getID();
            if (finalData.isEmpty()) {
                finalData.add(mItem);
            } else {
                counter = 0;
                for (k = 0; k < finalData.size(); k++) {
                    RecyclerData fItem = finalData.get(k);
                    if (!fItem.getID().equals(tmpID)) {
                        counter++;
                    }
                }
                if (counter == finalData.size()) {
                    finalData.add(mItem);
                    counter = 0;
                }
            }
        }


        /*while (counter < list.size()) {
            tmpID = list.get(counter).getID();
            for (int j = 0; j < list.size(); j++) {
                RecyclerData mItem = list.get(j);
                if (tmpID.equals(mItem.getID())) {
                    if (!isPicked)
                        finalData.add(mItem);
                    isPicked = true;
                    list.remove(j);
                } else {
                    counter++;
                    isPicked = false;
                }
            }
        }*/

        /*for (RecyclerData mItem : list) {
            if (!mItem.getID().equals(tmpID)) {
                tmpID = mItem.getID();
                finalData.add(mItem);
            }
        }*/
        Log.v("DATAA", "TOVAR FINAL DATA SIZE = " + Integer.toString(finalData.size()));
        Log.v("DATAA", "LISTSIZE = " + Integer.toString(list.size()));
        tovarData = finalData;
        setVisibilityOfTovarArrow(finalData);
        return finalData;
    }

    public List<RecyclerData> getStyleDataWithChosenCollar(List<RecyclerData> fromList, String searchID, String collarType) {
        List<RecyclerData> finalData = new ArrayList<>();
        int j;
        for (j = 0; j < fromList.size(); j++) {
            if (searchID.equals(fromList.get(j).getID()) && collarType.equals(fromList.get(j).getCollar()))
                finalData.add(fromList.get(j));
        }
        styleData = finalData;
        setVisivilityOfStyleArrow(finalData);

        styleRecyclerAdapter.changeStyleList(finalData);
        setItemtoFragment(styleRecyclerAdapter.getList().get(0));
        return finalData;
    }

    public List<RecyclerData> getSyledataWithfirstCollar(List<RecyclerData> fromList, String searchID) {
        String collar = null;

        List<RecyclerData> finalData = new ArrayList<>();
        List<RecyclerData> tmpData = new ArrayList<>();
        int j;
        for (j = 0; j < fromList.size(); j++) {
            if (searchID.equals(fromList.get(j).getID()))
                tmpData.add(fromList.get(j));
        }

        // skrivaem vorotniki kotorix net
        int c1 = 1, c2 = 1, c3 = 1, c4 = 1;
        for (int i = 0; i < tmpData.size(); i++) {
            RecyclerData mItem = tmpData.get(i);
            switch (mItem.getCollar()) {
                case "collar1":
                    if (collar == null) collar = "collar1";
                    c1 = 0;
                    break;
                case "collar2":
                    if (collar == null) collar = "collar2";
                    c2 = 0;
                    break;
                case "collar3":
                    if (collar == null) collar = "collar3";
                    c3 = 0;
                    break;
                case "collar4":
                    if (collar == null) collar = "collar4";
                    c4 = 0;
                    break;
                default:
                    break;
            }
        }
        hideCollars(c1, c2, c3, c4);
        /////////////////////////////////////
        for (j = 0; j < tmpData.size(); j++) {
            if (searchID.equals(tmpData.get(j).getID()) && collar.equals(tmpData.get(j).getCollar()))
                finalData.add(tmpData.get(j));
        }
        styleData = finalData;
        setVisivilityOfStyleArrow(finalData);
        return finalData;
    }

    public void hideCollars(int cl, int c2, int c3, int c4) {
        if (cl == 1)
            collar1.setVisibility(View.GONE);
        else collar1.setVisibility(View.VISIBLE);

        if (c2 == 1)
            collar2.setVisibility(View.GONE);
        else collar2.setVisibility(View.VISIBLE);

        if (c3 == 1)
            collar3.setVisibility(View.GONE);
        else collar3.setVisibility(View.VISIBLE);

        if (c4 == 1)
            collar4.setVisibility(View.GONE);
        else collar4.setVisibility(View.VISIBLE);
    }

    /*public List<RecyclerData> getStyleData(String id, String collar, String gender) {
        List<RecyclerData> finalData = new ArrayList<>();
        String tmpURI = null; // chtobi brat po odnomu tovaru iz odinakovix stiley (esli sprosyat sdelaem) poka ne rabotaet
        if (initialData.size() != 0) {
            for (int j = 0; j < initialData.size(); j++) {
                if (!somekey) {
                    initData = initialData.get(j);
                    somekey = true;
                }
                if (initialData.get(j).getID().equals(id) && initialData.get(j).getCollar().equals(collar) && initialData.get(j).getGender().equals(gender)) {
                    initialData.get(j).setType(tableName);
                    finalData.add(initialData.get(j));
                }
            }
        }
        Log.v("SECONDACTIVITY", "SIZE OF STYLE ITEMS == " + finalData.size());
        setVisivilityOfStyleArrow(finalData);
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
        setVisibilityOfTovarArrow(finalData);
        Log.v("DATAA", "tovat boyi after gender choose = " + Integer.toString(tovarBoyi));

        tovarData = finalData;
        return finalData;
    }*/


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
                getStyleDataWithChosenCollar(selectedGenderList, selectedTovarID, "collar1");
                timerHand.removeCallbacks(backanim);
                timerHand.postDelayed(backanim,2000);
                break;
            case R.id.yoqa2:
                getStyleDataWithChosenCollar(selectedGenderList, selectedTovarID, "collar2");
                timerHand.removeCallbacks(backanim);
                timerHand.postDelayed(backanim,2000);
                break;
            case R.id.yoqa3:
                getStyleDataWithChosenCollar(selectedGenderList, selectedTovarID, "collar3");
                timerHand.removeCallbacks(backanim);
                timerHand.postDelayed(backanim,2000);
                break;
            case R.id.yoqa4:
                getStyleDataWithChosenCollar(selectedGenderList, selectedTovarID, "collar4");
                timerHand.removeCallbacks(backanim);
                timerHand.postDelayed(backanim,2000);
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
        //styleRecyclerAdapter.changeStyleList(getStyleData(selectedItemID, selectedItemCollar, selectedGender));
    }

    @Override
    public void onBackPressed() {
        if (isGenderPicked) {

            /**
             * buyoga fragmenti remove qiliw i rukavodstvani chiqarish kere
             * */
            android.support.v4.app.Fragment temp0 = getSupportFragmentManager().
                    findFragmentById(R.id.frame);
            if(temp0!=null){
                getSupportFragmentManager()
                        .beginTransaction().remove(temp0).commit();
                orqa=null;
                oldi=null;
                yon=null;

            }

            isGenderPicked = false;
            tovarRecyclerAdapter.changeList(genderPickerList);
            styleRecyclerAdapter.clearList();
            setVisibilityOfTovarArrow(genderPickerList);
            selectedGenderList = null;
            selectedTovarID = null;
            hideCollars(0, 0, 0, 0);
        } else
            super.onBackPressed();
    }

    public void setVisibilityOfTovarArrow(List<RecyclerData> list) {
        if (list.size() > 3) {
            tovarArrow.setVisibility(View.VISIBLE);
            tovarRecyclerAdapter.setImageParams(tovarBoyi);
        } else {
            tovarArrow.setVisibility(View.GONE);
            tovarRecyclerAdapter.setImageParams(tovarBoyi + strelkaBoyi);
        }
    }

    public void setVisivilityOfStyleArrow(List<RecyclerData> list) {
        if (list.size() > 3) {
            styleArrow.setVisibility(View.VISIBLE);
            styleRecyclerAdapter.setImageParams(styleBoyi);
        } else {
            styleArrow.setVisibility(View.GONE);
            styleRecyclerAdapter.setImageParams(styleBoyi + strelkaBoyi);
        }
    }

    public void setVisibilityOfStickerArrow(List<StickerData> list) {
        int height;
        if (list.size() > 3) {
            stickerArrow.setVisibility(View.VISIBLE);
            height = stickerBoyi;
        } else {
            stickerArrow.setVisibility(View.GONE);
            height = stickerBoyi + strelkaBoyi;
        }
        stickerRecyclerAdapter.setImageParams(height);
        Log.v("DATAA", "total height = " + Integer.toString(height) + ";  height of strelka arrow = " + Integer.toString(strelkaBoyi) + ";  height of sticker item = " + Integer.toString(stickerBoyi));

    }
    String TAG="";
    public void setItemtoFragment(RecyclerData data) {
        oldiUri = data.getImageUri("front");
        TAG=data.getTag();
        Log.d("taga",TAG+"s");
        orqaUri = data.getImageUri("back");
        yonUri = data.getImageUri("side");
        oldi = null;
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
            @Override
            public void nextFrag() {
                vibr.vibrate(30);
                nextFragment();

            }

            @Override
            public void prevFrag() {
                vibr.vibrate(30);
                prevFragment();

            }
        });
        yon = null;
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
            @Override
            public void nextFrag() {
                vibr.vibrate(30);
                nextFragment();

            }

            @Override
            public void prevFrag() {
                vibr.vibrate(30);
                prevFragment();

            }
        });
        orqa = null;
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

            @Override
            public void nextFrag() {
                vibr.vibrate(30);
                nextFragment();

            }

            @Override
            public void prevFrag() {
                vibr.vibrate(30);
                prevFragment();

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
    }


    private void nextFragment(){
        if (current_status == 0) {
            if (yon != null) {
                oldi.setTEG("oldi");
                //oldi.setcache();
                getSupportFragmentManager()
                        .beginTransaction().replace(R.id.frame, yon).commit();

                current_status = 1;
                razmer.setText(Integer.toString(razmerPol[current_status]));
                povorot.setText(Integer.toString(povorotMas[current_status]));
            }
        } else if (current_status == 1) {
            if (orqa != null) {
                yon.setTEG("yon");
                //  yon.setcache();
                getSupportFragmentManager()
                        .beginTransaction().replace(R.id.frame, orqa).commit();
                current_status = 2;
                razmer.setText(Integer.toString(razmerPol[current_status]));
                povorot.setText(Integer.toString(povorotMas[current_status]));
            }

        } else if (current_status == 2) {
            if (oldi != null) {
                orqa.setTEG("orqa");
                // orqa.setcache();
                getSupportFragmentManager()
                        .beginTransaction().replace(R.id.frame, oldi).commit();
                current_status = 0;
                razmer.setText(Integer.toString(razmerPol[current_status]));
                povorot.setText(Integer.toString(povorotMas[current_status]));
            }

        }
    }

    private void prevFragment(){
        if (current_status == 0) {
            if (yon != null) {
                oldi.setTEG("oldi");
                //oldi.setcache();
                getSupportFragmentManager()
                        .beginTransaction().replace(R.id.frame, orqa).commit();

                current_status = 2;
                razmer.setText(Integer.toString(razmerPol[current_status]));
                povorot.setText(Integer.toString(povorotMas[current_status]));
            }
        } else if (current_status == 1) {
            if (orqa != null) {
                yon.setTEG("yon");
                //  yon.setcache();
                getSupportFragmentManager()
                        .beginTransaction().replace(R.id.frame, oldi).commit();
                current_status = 0;
                razmer.setText(Integer.toString(razmerPol[current_status]));
                povorot.setText(Integer.toString(povorotMas[current_status]));
            }

        } else if (current_status == 2) {
            if (oldi != null) {
                orqa.setTEG("orqa");
                // orqa.setcache();
                getSupportFragmentManager()
                        .beginTransaction().replace(R.id.frame, yon).commit();
                current_status = 1;
                razmer.setText(Integer.toString(razmerPol[current_status]));
                povorot.setText(Integer.toString(povorotMas[current_status]));
            }

        }
    }
}
