package masharipov.certustextile.stickeradd;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.support.annotation.IntegerRes;
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
import android.widget.LinearLayout;

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
    private DraggableGridAdapter myItemAdapter;
    private SectionDraggableGridAdapter tovarAdapter;
    private RecyclerView.Adapter mWrappedAdapter, mAdapter;
    private RecyclerViewDragDropManager mRecyclerViewDragDropManager;
    private List<StickerData> stickerList;
    private List<TovarData> tovarList;
    private List<List<RecyclerData>> goodsList;
    private int numOfItemsInRow = 3;
    private String extras;
    private CardView bottomLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_nakleyka);

        Intent intent = getIntent();
        extras = intent.getStringExtra("TYPE");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        bottomLayout = (CardView) findViewById(R.id.layout);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.widthPixels;
        Log.v("TOVAR", Integer.toString(height));
        bottomLayout.setTranslationY(800);

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
        if (extras.equals("STICKER")) {
            CertusDatabase cDB = new CertusDatabase(this);
            stickerList = cDB.getStickersFromDB();
            myItemAdapter = new DraggableGridAdapter(stickerList, this, View.GONE, View.VISIBLE, (CoordinatorLayout) findViewById(R.id.coordinatorLayout));
            mAdapter = myItemAdapter;
            mWrappedAdapter = mRecyclerViewDragDropManager.createWrappedAdapter(myItemAdapter);
            fab.setVisibility(View.VISIBLE);
            bottomLayout.setVisibility(View.GONE);
        } else if (extras.equals("GOODS")) {  // agar tovar tanlansa

            // dobavlenie tovarov v osnovnoy recycler
            CertusDatabase cDB = new CertusDatabase(this);
            String tableNames[] = {"Futbolka", "Mayka", "Polo"};
            tovarList = new ArrayList<>();

            // chtobi otobrajal odin tovar po odnomu ID
            for (String tableName : tableNames) {
                if (cDB.isTableEmpty(tableName)) continue;

                TovarData sectionData = new TovarData();
                sectionData.setSectionText(tableName);
                sectionData.setType(1);
                tovarList.add(sectionData);

                List<TovarData> items = cDB.getGoodsFromDB(tableName);
                List<TovarData> finalItems = new ArrayList<>();
                String tmpID = null;
                Log.v("TOVAR", tableName + " size  == " + Integer.toString(items.size()));
                for (int j = 0; j < items.size(); j++) {
                    if (!items.get(j).getID().equals(tmpID)) {
                        items.get(j).setSectionText(tableName);
                        finalItems.add(items.get(j));
                        tmpID = items.get(j).getID();
                    }
                }
                tovarList.addAll(finalItems);
            }

            // dlya otobrajeniya headerov
            mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (tovarList.get(position).getType() == 1)
                        return numOfItemsInRow;
                    else
                        return 1;
                }
            });

            // dlya pokazivaniya vsex tovarov vibrannoy kategorii
            bottomLayout.setVisibility(View.VISIBLE);
            RecyclerView drawerRecycler = (RecyclerView) findViewById(R.id.list);
            GridLayoutManager drawerLayoutManager = new GridLayoutManager(this, numOfItemsInRow);
            drawerRecycler.setLayoutManager(drawerLayoutManager);
            DrawerGridAdapter drawerGridAdapter = new DrawerGridAdapter(this, View.VISIBLE, bottomLayout, tovarAdapter);
            drawerRecycler.setAdapter(drawerGridAdapter);


            // glavniy adapter
            tovarAdapter = new SectionDraggableGridAdapter(tovarList, this, View.VISIBLE, (CoordinatorLayout) findViewById(R.id.coordinatorLayout), bottomLayout, drawerGridAdapter);
            mWrappedAdapter = mRecyclerViewDragDropManager.createWrappedAdapter(tovarAdapter);
            fab.setVisibility(View.GONE);


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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myItemAdapter.pickImage();
            }
        });
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
        myItemAdapter.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onBackPressed() {
        if (extras.equals("GOODS")) {
            if (tovarAdapter.isPanelOpen()) {
                bottomLayout.animate().translationYBy(400);
                tovarAdapter.setPanelOpened(false);
                return;
            }
            super.onBackPressed();
        } else {
            if (!myItemAdapter.isDatabaseChanged()) {
                super.onBackPressed();
                CertusDatabase certusDatabase = new CertusDatabase(myItemAdapter.getStickerList(), getApplicationContext());
                certusDatabase.saveStickersToDB();
                return;
            }
            if (myItemAdapter.getStickerList() == null || myItemAdapter.getStickerList().size() == 0) {
                super.onBackPressed();
                return;
            }
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("Сохранить изменения?");
            adb.setMessage("Сохранить внесенные изменения в базу данных?");
            adb.setNegativeButton("НЕТ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            adb.setPositiveButton("ДА", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CertusDatabase certusDatabase = new CertusDatabase(myItemAdapter.getStickerList(), getApplicationContext());
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
        }

    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

}
