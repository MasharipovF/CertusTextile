package masharipov.certustextile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
    List<RecyclerData> genderList, tovarData, styleData;
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

    RecyclerData initData;
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

        genderList = new ArrayList<>();
        if (maleList != null && maleList.size() != 0) {
            Log.v("DATAA", "MALE  = " + Integer.toString(maleList.size()));
            RecyclerData gData = new RecyclerData();
            gData.setGenderImageResourse(gArray[0]);
            genderList.add(gData);
            wholeList.add(maleList);
        }
        if (femaleList != null && femaleList.size() != 0) {
            Log.v("DATAA", "FEMALE  = " + Integer.toString(femaleList.size()));
            RecyclerData gData = new RecyclerData();
            gData.setGenderImageResourse(gArray[1]);
            genderList.add(gData);
            wholeList.add(femaleList);
        }
        if (boyList != null && boyList.size() != 0) {
            Log.v("DATAA", "BOY  = " + Integer.toString(boyList.size()));
            RecyclerData gData = new RecyclerData();
            gData.setGenderImageResourse(gArray[2]);
            genderList.add(gData);
            wholeList.add(boyList);
        }
        if (girlList != null && girlList.size() != 0) {
            Log.v("DATAA", "GIRL  = " + Integer.toString(girlList.size()));
            RecyclerData gData = new RecyclerData();
            gData.setGenderImageResourse(gArray[3]);
            genderList.add(gData);
            wholeList.add(girlList);
        }

        // nacalnie znacheniya
        if (initialData != null && initialData.size() != 0) {
            selectedItemID = initialData.get(0).getID();
            selectedItemCollar = initialData.get(0).getCollar();
        }

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

                Intent tview = new Intent(SecondActivity.this, Tview.class);
                startActivity(tview);

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
                    if (yon != null) {
                        getSupportFragmentManager()
                                .beginTransaction().replace(R.id.frame, yon).commit();

                        current_status = 1;
                        razmer.setText(Integer.toString(razmerPol[current_status]));
                        povorot.setText(Integer.toString(povorotMas[current_status]));
                    }
                } else if (current_status == 1) {
                    if (orqa != null) {
                        getSupportFragmentManager()
                                .beginTransaction().replace(R.id.frame, orqa).commit();
                        current_status = 2;
                        razmer.setText(Integer.toString(razmerPol[current_status]));
                        povorot.setText(Integer.toString(povorotMas[current_status]));
                    }

                } else if (current_status == 2) {
                    if (oldi != null) {
                        getSupportFragmentManager()
                                .beginTransaction().replace(R.id.frame, oldi).commit();
                        current_status = 0;
                        razmer.setText(Integer.toString(razmerPol[current_status]));
                        povorot.setText(Integer.toString(povorotMas[current_status]));
                    }

                }
            }
        });
    }

    boolean keyStart = true;

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

        tovarData = genderList;
        tovarRecyclerAdapter = new TovarRecyclerAdapter(this, genderList, tovarBoyi, new TovarRecyclerAdapter.clickListener() {
            @Override
            public void onItemClick(ImageView img, int position, List<RecyclerData> dataList) {
                if (!isGenderPicked) {
                    isGenderPicked = true;
                    currentGender = position;
                    tmpTovarList = getTovarData(wholeList.get(position));
                    tmpStyleList = getStyleData(wholeList.get(position), tmpTovarList.get(0).getID());

                    tovarRecyclerAdapter.changeList(tmpTovarList);
                    styleRecyclerAdapter.changeStyleList(tmpStyleList);

                    ifClickedImagePositionNotChangedDoNotChangeStyleList = 0;

                    setItemtoFragment(styleRecyclerAdapter.getList().get(0));
                  /*  selectedGender = genderNames[position];
                    tovarRecyclerAdapter.changeList(getTovarData(selectedGender));
                    styleRecyclerAdapter.changeStyleList(getStyleData(selectedItemID, selectedItemCollar, selectedGender));
                    if (initData != null)
                        setItemtoFrsgment(initData);*/
                    return;
                }
                if (position != ifClickedImagePositionNotChangedDoNotChangeStyleList) {
                    ifClickedImagePositionNotChangedDoNotChangeStyleList = position;
                    tmpStyleList = getStyleData(wholeList.get(currentGender), tmpTovarList.get(position).getID());
                    styleRecyclerAdapter.changeStyleList(tmpStyleList);
                    setItemtoFragment(styleRecyclerAdapter.getList().get(0));

                   /* selectedItemID = dataList.get(position).getID();
                    styleRecyclerAdapter.changeStyleList(getStyleData(selectedItemID, selectedItemCollar, selectedGender));
                    if (getStyleData(selectedItemID, selectedItemCollar, selectedGender).get(0) != null)
                        setItemtoFrsgment(getStyleData(selectedItemID, selectedItemCollar, selectedGender).get(0));*/
                }
                panelyoqa.animate().translationX(140).start();
                timerHand.postDelayed(backanim, 3000);
                current_status = 0;
            }
        });

        setVisibilityOfTovarArrow(genderList);


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
        int isPicked = 0;
        List<String> pickedIDs = new ArrayList<>();

        // TODO bazadan oliwi togirlaw kere
        for (int i = 0; i < list.size(); i++) {
            RecyclerData mItem = list.get(i);
            tmpID = mItem.getID();
            Log.v("DATAA", "TMPID  = " + mItem.getID());
            if (pickedIDs.size() == 0) {
                if (tmpID.equals(mItem.getID())) {
                    finalData.add(mItem);
                    pickedIDs.add(tmpID);
                }
            } else {
                for (int j = 0; j < pickedIDs.size(); j++) {
                    if (!tmpID.equals(pickedIDs.get(j)))
                        isPicked++;
                }
                if (isPicked == pickedIDs.size()) {
                    isPicked = 0;
                    finalData.add(mItem);
                    pickedIDs.add(tmpID);
                }
            }
        }
        tmpID = list.get(list.size()-1).getID();
        for (int j = 0; j < pickedIDs.size(); j++) {
            if (!tmpID.equals(pickedIDs.get(j)))
                isPicked++;
        }
        if (isPicked == pickedIDs.size()) {
            isPicked = 0;
            finalData.add(list.get(list.size()-1));
            pickedIDs.add(tmpID);
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

    public List<RecyclerData> getStyleData(List<RecyclerData> fromList, String searchID) {
        List<RecyclerData> finalData = new ArrayList<>();
        int j;
        for (j = 0; j < fromList.size(); j++) {
            if (searchID.equals(fromList.get(j).getID())) finalData.add(fromList.get(j));
        }
        styleData = finalData;
        setVisivilityOfStyleArrow(finalData);
        return finalData;
    }

    public List<RecyclerData> getStyleData(String id, String collar, String gender) {
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
        //styleRecyclerAdapter.changeStyleList(getStyleData(selectedItemID, selectedItemCollar, selectedGender));
    }

    @Override
    public void onBackPressed() {
        if (isGenderPicked) {
            isGenderPicked = false;
            tovarRecyclerAdapter.changeList(genderList);
            styleRecyclerAdapter.clearList();
            setVisibilityOfTovarArrow(genderList);
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

    public void setItemtoFragment(RecyclerData data) {
        oldiUri = data.getImageUri("front");
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
}
