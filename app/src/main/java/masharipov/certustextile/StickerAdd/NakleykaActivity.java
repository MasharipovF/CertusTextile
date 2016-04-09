package masharipov.certustextile.stickeradd;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.NinePatchDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
    private RecyclerView.LayoutManager mLayoutManager;
    private DraggableGridAdapter myItemAdapter;
    private RecyclerView.Adapter mWrappedAdapter, mAdapter;
    private RecyclerViewDragDropManager mRecyclerViewDragDropManager;
    private List<GridItem> stickerList;
    private List<List<RecyclerData>> goodsList;
    private int numOfItemsInRow = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nakleyka);

        Intent intent = getIntent();
        String extras = intent.getStringExtra("TYPE");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        //init recyclerview
        mRecyclerView = (RecyclerView) findViewById(R.id.gridLayout);
        mLayoutManager = new GridLayoutManager(this, numOfItemsInRow, GridLayoutManager.VERTICAL, false);

        // drag & drop
        mRecyclerViewDragDropManager = new RecyclerViewDragDropManager();
        mRecyclerViewDragDropManager.setDraggingItemShadowDrawable((NinePatchDrawable) ContextCompat.getDrawable(this, R.drawable.material_shadow_z3));
        // Start dragging after long press
        mRecyclerViewDragDropManager.setInitiateOnLongPress(true);
        mRecyclerViewDragDropManager.setInitiateOnMove(false);
        mRecyclerViewDragDropManager.setLongPressTimeout(750);

        //adapter
        CertusDatabase cDB = new CertusDatabase(this);
        if (extras.equals("STICKER")) {
            stickerList = cDB.getStickersFromDB();
            myItemAdapter = new DraggableGridAdapter(stickerList, this, View.GONE);
            mAdapter = myItemAdapter;
            mWrappedAdapter = mRecyclerViewDragDropManager.createWrappedAdapter(myItemAdapter);
            fab.setVisibility(View.VISIBLE);
        } else if (extras.equals("GOODS")) {
            // some code to display
            stickerList = cDB.getStickersFromDB();
            myItemAdapter = new DraggableGridAdapter(stickerList, this, View.VISIBLE);
            mWrappedAdapter = mRecyclerViewDragDropManager.createWrappedAdapter(myItemAdapter);
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
    public void onDestroy(){
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
