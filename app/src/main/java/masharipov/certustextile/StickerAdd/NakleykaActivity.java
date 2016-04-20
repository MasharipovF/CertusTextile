package masharipov.certustextile.stickeradd;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import masharipov.certustextile.CertusDatabase;
import masharipov.certustextile.R;
import masharipov.certustextile.edit.RecyclerData;

public class NakleykaActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private DraggableGridAdapter stickerAdapter, slideshowAdapter;
    private SectionDraggableGridAdapter tovarAdapter;
    private RecyclerView.Adapter mWrappedAdapter, mAdapter;
    private RecyclerViewDragDropManager mRecyclerViewDragDropManager;
    private List<StickerData> stickerList, slideshowList;
    private List<RecyclerData> tovarList;
    private List<List<RecyclerData>> goodsList;
    private int numOfItemsInRow = 6;
    private String extras;
    private CardView bottomLayout;
    private FloatingActionButton fab;
    private CertusDatabase cDB;
    private ImageView backgroundImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_nakleyka);

        Intent intent = getIntent();
        extras = intent.getStringExtra("TYPE");
        cDB = new CertusDatabase(this);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        bottomLayout = (CardView) findViewById(R.id.layout);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        // chtobi spryatat nijnuyu panel
        bottomLayout.setTranslationY(800);
        bottomLayout.setVisibility(View.GONE);

        //init recyclerview
        mRecyclerView = (RecyclerView) findViewById(R.id.gridLayout);
        mLayoutManager = new GridLayoutManager(this, numOfItemsInRow);

        // drag & drop
        mRecyclerViewDragDropManager = new RecyclerViewDragDropManager();
        mRecyclerViewDragDropManager.setDraggingItemShadowDrawable((NinePatchDrawable) ContextCompat.getDrawable(this, R.drawable.material_shadow_z3));
        // Start dragging after long press
        mRecyclerViewDragDropManager.setInitiateOnLongPress(true);
        mRecyclerViewDragDropManager.setInitiateOnMove(false);
        mRecyclerViewDragDropManager.setLongPressTimeout(750);

        // agar sticker tanlansa
        switch (extras) {
            case "STICKER":
                initStickerAdapter();
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stickerAdapter.pickImage();
                    }
                });

                break;
            case "GOODS":
                initTovarAdapter();
                break;
            case "SLIDESHOW":
                initSlideshowAdapter();
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        slideshowAdapter.pickImage();
                    }
                });

                break;
        }

        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mWrappedAdapter);  // requires *wrapped* adapter
        mRecyclerView.setItemAnimator(animator);

        // additional decorations
        //noinspection StatementWithEmptyBody
        if (supportsViewElevation()) {
            // Lollipop or later has native drop shadow feature. ItemShadowDecorator is not required.
        } else {
            mRecyclerView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) ContextCompat.getDrawable(this, R.drawable.material_shadow_z1)));
        }
        mRecyclerViewDragDropManager.attachRecyclerView(mRecyclerView);

    }

    @Override
    public void onDestroy() {
        if (mRecyclerViewDragDropManager != null) {
            mRecyclerViewDragDropManager.release();
            mRecyclerViewDragDropManager = null;
        }

        if (mRecyclerView != null) {
            mRecyclerView.setItemAnimator(null);
            mRecyclerView.setAdapter(null);
            mRecyclerView = null;
        }

        if (mWrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(mWrappedAdapter);
            mWrappedAdapter = null;
        }
        mAdapter = null;
        mLayoutManager = null;

        super.onDestroy();
    }


    private boolean supportsViewElevation() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (extras) {
            case "STICKER":
                stickerAdapter.onActivityResult(requestCode, resultCode, data);
                break;
            case "SLIDESHOW":
                slideshowAdapter.onActivityResult(requestCode, resultCode, data);
                break;
        }

    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Сохранить изменения?");
        adb.setMessage("Сохранить внесенные изменения в базу данных?");
        adb.setNegativeButton("НЕТ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        switch (extras) {
            ///////////////////////////////////////////////////////////////////
            case "STICKER":
                if (!stickerAdapter.isDatabaseChanged()) {
                    super.onBackPressed();
                    CertusDatabase certusDatabase = new CertusDatabase(stickerAdapter.getStickerList(), getApplicationContext());
                    certusDatabase.saveStickersToDB();
                    return;
                }
                adb.setPositiveButton("ДА", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CertusDatabase certusDatabase = new CertusDatabase(stickerAdapter.getStickerList(), getApplicationContext());
                        certusDatabase.saveStickersToDB();
                        finish();
                    }
                });
                adb.setNeutralButton("ОТМЕНА", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                adb.setIcon(android.R.drawable.ic_dialog_info);
                adb.create();
                adb.show();

                break;
            ////////////////////////////////////////////////////////
            case "SLIDESHOW":
                if (!slideshowAdapter.isDatabaseChanged()) {
                    super.onBackPressed();
                    CertusDatabase certusDatabase = new CertusDatabase(slideshowAdapter.getStickerList(), getApplicationContext());
                    certusDatabase.saveSlideshowItemsToDB();
                    return;
                }


                adb.setPositiveButton("ДА", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CertusDatabase certusDatabase = new CertusDatabase(slideshowAdapter.getStickerList(), getApplicationContext());
                        certusDatabase.saveSlideshowItemsToDB();
                        finish();
                    }
                });
                adb.setNeutralButton("ОТМЕНА", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                adb.setIcon(android.R.drawable.ic_dialog_info);
                adb.create();
                adb.show();
                break;
            ////////////////////////////////////////////////
            case "GOODS":
                if (!tovarAdapter.isDatabaseChanged()) {
                    super.onBackPressed();
                    return;
                }
                adb.setPositiveButton("ДА", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (tovarAdapter.getTovarList() == null || tovarAdapter.getTovarList().size() == 0) {
                            CertusDatabase certusDatabase = new CertusDatabase(getApplicationContext());
                            certusDatabase.clearDB();
                            finish();
                            return;
                        }
                        List<RecyclerData> items = tovarAdapter.getTovarList();
                        List<RecyclerData> finalItems = new ArrayList<>();
                        String curTableName = null;
                        for (int i = 0; i < items.size(); i++) {
                            RecyclerData tmpData = items.get(i);
                            if (tmpData.getSection() == 1) {
                                if (!tmpData.getType().equals(curTableName)) {
                                    if (curTableName != null) {
                                        CertusDatabase certusDatabase = new CertusDatabase(getApplicationContext(), finalItems);
                                        certusDatabase.saveGoodsToDB(curTableName, true);
                                        Log.v("SAVEDB", curTableName + "  saved");
                                    }
                                    curTableName = tmpData.getType();
                                    finalItems.clear();
                                    continue;
                                }
                            }
                            finalItems.add(tmpData);
                        }
                        Log.v("SAVEDB", curTableName + "  savdfsdfsded");
                        CertusDatabase certusDatabase = new CertusDatabase(getApplicationContext(), finalItems);
                        certusDatabase.saveGoodsToDB(curTableName, true);
                        finish();
                    }
                });
                adb.setNeutralButton("ОТМЕНА", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                adb.setIcon(android.R.drawable.ic_dialog_info);
                adb.create();
                adb.show();



                /*if (tovarAdapter.isPanelOpen()) {
                    Log.v("DATAA", "PANEL OPEN");
                    bottomLayout.animate().translationYBy(400);
                    tovarAdapter.closePanel();
                    return;
                }
                if (tovarAdapter.isDatabaseChanged()) {

                    List<RecyclerData> mainList = tovarAdapter.getTovarList();
                    if (mainList.size() == 0) {
                        CertusDatabase certusDatabase = new CertusDatabase(getApplicationContext());
                        certusDatabase.clearDB();
                        return;
                    }


                    List<RecyclerData> futbolka = tovarAdapter.getFutbolkaList();
                    List<RecyclerData> mayka = tovarAdapter.getMaykaList();
                    List<RecyclerData> polo = tovarAdapter.getPoloList();
                    List<RecyclerData> finalList = new ArrayList<>();
                    String tableNames[] = {"Futbolka", "Mayka", "Polo"};
                    String currentTable = null;
                    String tmpID = null;

                    for (int i = 0; i < mainList.size(); i++) {
                        RecyclerData tmpData = mainList.get(i);

                        //esli item = section
                        if (!tmpData.getType().equals(currentTable)) {
                            CertusDatabase certusDatabase = new CertusDatabase(getApplicationContext(), finalList);
                            if (currentTable != null)
                                certusDatabase.saveGoodsToDB(currentTable, true);

                            currentTable = tmpData.getType();
                            Log.v("SAVEDB", "TableName == " + currentTable + "    number of items" + Integer.toString(finalList.size()));
                            finalList.clear();
                            continue;
                        }

                        switch (currentTable) {
                            case "Futbolka":
                                tmpID = tmpData.getID();
                                Log.v("SAVEDB", "futbolka size =  " + Integer.toString(futbolka.size()));
                                for (int k = 0; k < futbolka.size(); k++) {
                                    if (futbolka.get(k).getID().equals(tmpID)) {
                                        finalList.add(futbolka.get(k));
                                        futbolka.remove(k);
                                    }
                                }
                                Log.v("SAVEDB", "TableName == " + currentTable + "    number of items" + Integer.toString(finalList.size()));

                                break;
                            case "Mayka":

                                break;
                            case "Polo":

                                break;
                        }

                    }

                    CertusDatabase certusDatabase = new CertusDatabase(getApplicationContext(), finalList);
                    if (currentTable != null)
                        certusDatabase.saveGoodsToDB(currentTable, true);

                    super.onBackPressed();
                    return;
                }*/
                break;
        }
    }

    private boolean initTovarAdapter() {
        // dobavlenie tovarov v osnovnoy recycler
        String tableNames[] = {"Futbolka", "Mayka", "Polo"};
        String tableNamesRus[] = {"Футболка", "Майка", "Поло"};
        tovarList = new ArrayList<>();

        int stID = 0; // stable ID chtobi content ne dublirovalsya
        for (String tableName : tableNames) {
            if (cDB.isTableEmpty(tableName)) continue;

            RecyclerData sectionData = new RecyclerData();
            sectionData.setType(tableName);
            sectionData.setSection(1);
            sectionData.stableID = stID++;
            tovarList.add(sectionData);
            List<RecyclerData> data = cDB.getTovarFromDB(tableName);
            for (int i = 0; i < data.size(); i++) {
                RecyclerData mData = data.get(i);
                mData.stableID = stID++;
                tovarList.add(mData);
            }

            /*List<RecyclerData> items = cDB.getTovarFromDB(tableName);
            List<RecyclerData> finalItems = new ArrayList<>();
            String tmpID = null;
            Log.v("TOVAR", tableName + " size  == " + Integer.toString(items.size()));
            for (int j = 0; j < items.size(); j++) {
                if (!items.get(j).getID().equals(tmpID)) {
                    items.get(j).setType(tableName);
                    finalItems.add(items.get(j));
                    tmpID = items.get(j).getID();
                }
            }
            tovarList.addAll(finalItems);*/
        }

        // dlya otobrajeniya headerov
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (tovarList.get(position).getSection() == 1)
                    return numOfItemsInRow;
                else
                    return 1;
            }
        });

        // childAdapter
        // TODO bottom layout
        bottomLayout.setVisibility(View.GONE);
        RecyclerView drawerRecycler = (RecyclerView) findViewById(R.id.list);
        GridLayoutManager drawerLayoutManager = new GridLayoutManager(this, numOfItemsInRow);
        drawerRecycler.setLayoutManager(drawerLayoutManager);
        SectionChildDraggableGridAdapter drawerGridAdapter = new SectionChildDraggableGridAdapter(this, View.VISIBLE, bottomLayout, tovarAdapter);
        drawerRecycler.setAdapter(drawerGridAdapter);


        // glavniy adapter
        tovarAdapter = new SectionDraggableGridAdapter(tovarList, this, View.VISIBLE, (CoordinatorLayout) findViewById(R.id.coordinatorLayout), bottomLayout, drawerGridAdapter);
        mWrappedAdapter = mRecyclerViewDragDropManager.createWrappedAdapter(tovarAdapter);
        fab.setVisibility(View.GONE);

        return tovarList.size() > 0;
    }

    private boolean initStickerAdapter() {
        stickerList = cDB.getStickersFromDB();
        stickerAdapter = new DraggableGridAdapter(stickerList, this, (CoordinatorLayout) findViewById(R.id.coordinatorLayout), View.VISIBLE);
        mAdapter = stickerAdapter;
        mWrappedAdapter = mRecyclerViewDragDropManager.createWrappedAdapter(stickerAdapter);
        fab.setVisibility(View.VISIBLE);
        bottomLayout.setVisibility(View.GONE);

        return stickerList.size() > 0;
    }

    private boolean initSlideshowAdapter() {
        slideshowList = cDB.getSlideshowItemsFromDB();
        Log.v("DATAA", "SLIDESHOW SIZE == " + Integer.toString(slideshowList.size()));
        slideshowAdapter = new DraggableGridAdapter(slideshowList, this, (CoordinatorLayout) findViewById(R.id.coordinatorLayout), View.GONE);
        mWrappedAdapter = slideshowAdapter;
        mWrappedAdapter = mRecyclerViewDragDropManager.createWrappedAdapter(slideshowAdapter);
        fab.setVisibility(View.VISIBLE);
        bottomLayout.setVisibility(View.GONE);

        return slideshowList.size() > 0;
    }

}