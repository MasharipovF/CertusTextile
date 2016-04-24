package masharipov.certustextile.stickeradd;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import masharipov.certustextile.CertusDatabase;
import masharipov.certustextile.R;
import masharipov.certustextile.edit.RecyclerData;


public class SectionDraggableGridAdapter extends RecyclerView.Adapter<SectionDraggableGridAdapter.SectionGridHolder>
        implements DraggableItemAdapter<SectionDraggableGridAdapter.SectionGridHolder> {

    private Context context;
    private List<RecyclerData> tovarList, oldTovarList;
    private List<RecyclerData> futbolkaList, maykaList, poloList;
    private List<RecyclerData> childItemList;
    private Intent imagePickerIntent;
    private int SELECT_PICTURE = 1, visibility;
    private SectionChildDraggableGridAdapter drawerAdapter;
    final int SECTION = 1, ITEM = 0;

    private int databaseChangedFlag = 0;
    private CoordinatorLayout coordinatorLayout;
    private CardView recyclerLayout;
    private int tmp = 0, clickedPos = -1;
    private boolean panelOpened = false;
    private CertusDatabase cDB;


    public static class SectionGridHolder extends AbstractDraggableItemViewHolder implements View.OnClickListener {
        public FrameLayout mContainer;
        public ImageView imgBtn, delBtn;
        private onItemClick listener;
        private TextView sectionText, tagTxt;

        // VIEWHOLDER
        public SectionGridHolder(View v, int viewType, int visibility, onItemClick click) {
            super(v);
            switch (viewType) {
                case 1:
                    sectionText = (TextView) v.findViewById(R.id.sectionText);
                    break;
                case 0:
                    listener = click;
                    mContainer = (FrameLayout) v.findViewById(R.id.container);
                    delBtn = (ImageButton) v.findViewById(R.id.gridDelBtn);
                    imgBtn = (ImageView) v.findViewById(R.id.gridImg);
                    tagTxt = (TextView) v.findViewById(R.id.gridItemTag);
                    tagTxt.setVisibility(visibility);
                    delBtn.setOnClickListener(this);
                    imgBtn.setOnClickListener(this);
                    break;

            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.gridDelBtn:
                    listener.delBtnClick(getAdapterPosition());
                    break;
                case R.id.gridImg:
                    listener.onImageClick(delBtn, getAdapterPosition());
            }
        }
    }

    public interface onItemClick {
        void delBtnClick(int position);

        void editBtnClick(int position);

        void onImageClick(ImageView img, int position);

    }


    public SectionDraggableGridAdapter(List<RecyclerData> list, Context ctx, int vis, CoordinatorLayout Clayout, CardView Llayout, SectionChildDraggableGridAdapter adapter) {
        context = ctx;
        tovarList = list;
        // oldTovarList = list;
        visibility = vis;
        coordinatorLayout = Clayout;
        recyclerLayout = Llayout;
        drawerAdapter = adapter;

        futbolkaList = new ArrayList<>();
        maykaList = new ArrayList<>();
        poloList = new ArrayList<>();
        cDB = new CertusDatabase(context);

        setHasStableIds(true);
    }

    @Override
    public SectionGridHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v;
        switch (viewType) {
            case SECTION:
                v = inflater.inflate(R.layout.grid_item_section, parent, false);
                break;
            case ITEM:
                v = inflater.inflate(R.layout.grid_item, parent, false);
                break;
            default:
                throw new IllegalStateException("Unexpected viewType (= " + viewType + ")");
        }
        return new SectionGridHolder(v, viewType, visibility, new onItemClick() {
            @Override
            public void delBtnClick(final int position) {
                removeItem(position);
            }

            @Override
            public void editBtnClick(int position) {
            }

            String searchID = null;

            @Override
            public void onImageClick(ImageView img, int position) {
              /*  if (panelOpened) {
                    Log.v("DATAA", "panel opened");
                    return;
                }
                childItemList = new ArrayList<>();
                clickedPos = position;
                switch (tovarList.get(position).getType()) {
                    case "Futbolka":
                        if (futbolkaList.size() != 0 && tovarList.get(position).getID().equals(searchID)) {
                            Log.v("DATAA", "fUTBOLKA NOT EMPTY");

                            // search for items with ID
                            int count = 0;
                            while (count < futbolkaList.size()) {
                                Log.v("DATAA", "LISt SIZE == " + Integer.toString(futbolkaList.size()));
                                RecyclerData tmpData = futbolkaList.get(count);
                                if (tmpData.getID().equals(searchID)) {
                                    childItemList.add(tmpData);
                                    futbolkaList.remove(count);
                                } else {
                                    count++;
                                }
                            }

                        } else {
                            List<RecyclerData> drawerData = cDB.getTovarFromDB(tovarList.get(position).getType());
                            String tempId = tovarList.get(position).getID();
                            searchID = tempId;
                            for (int j = 0; j < drawerData.size(); j++) {
                                if (tempId.equals(drawerData.get(j).getID())) {
                                    childItemList.add(drawerData.get(j));
                                }
                            }

                        }

                        break;
                    case "Mayka":

                        if (maykaList.size() != 0) {

                            // search for items with ID
                            int count = 0;
                            while (count < maykaList.size()) {
                                Log.v("DATAA", "LISt SIZE == " + Integer.toString(maykaList.size()));
                                RecyclerData tmpData = maykaList.get(count);
                                if (tmpData.getID().equals(searchID)) {
                                    childItemList.add(tmpData);
                                    maykaList.remove(count);
                                } else {
                                    count++;
                                }
                            }

                        } else {
                            Log.v("DATAA", "LISt empty ");


                            List<RecyclerData> drawerData = cDB.getTovarFromDB(tovarList.get(position).getType());
                            String tempId = tovarList.get(position).getID();
                            for (int j = 0; j < drawerData.size(); j++) {
                                if (tempId.equals(drawerData.get(j).getID())) {
                                    childItemList.add(drawerData.get(j));
                                }
                            }

                        }

                        break;
                    case "Polo":

                        if (poloList.size() != 0) {

                            // search for items with ID
                            int count = 0;
                            while (count < poloList.size()) {
                                Log.v("DATAA", "LISt SIZE == " + Integer.toString(poloList.size()));
                                RecyclerData tmpData = poloList.get(count);
                                if (tmpData.getID().equals(searchID)) {
                                    childItemList.add(tmpData);
                                    poloList.remove(count);
                                } else {
                                    count++;
                                }
                            }

                        } else {
                            Log.v("DATAA", "LISt empty ");

                            List<RecyclerData> drawerData = cDB.getTovarFromDB(tovarList.get(position).getType());
                            String tempId = tovarList.get(position).getID();
                            for (int j = 0; j < drawerData.size(); j++) {
                                if (tempId.equals(drawerData.get(j).getID())) {
                                    childItemList.add(drawerData.get(j));
                                }
                            }

                        }

                        break;
                }
                Log.v("TOVAR", "FINAL DATA SIZE == " + Integer.toString(childItemList.size()));
                drawerAdapter.setDataBase(childItemList);
                recyclerLayout.animate().translationYBy(-400);
                panelOpened = true;*/
            }
        });
    }

    @Override
    public void onBindViewHolder(SectionGridHolder holder, int position) {
        RecyclerData current = tovarList.get(position);
        switch (current.getSection()) {
            case SECTION:
                if (TextUtils.isEmpty(current.getType()) || current.getType() != null) {
                    String categoryName = null;
                    switch (current.getType()) {
                        case "Futbolka":
                            categoryName = "Футболки";
                            break;
                        case "Mayka":
                            categoryName = "Майки";
                            break;
                        case "Polo":
                            categoryName = "Поло";
                            break;
                    }
                    holder.sectionText.setText(categoryName);
                } else holder.sectionText.setText("");
                break;
            case ITEM:
                if (!TextUtils.isEmpty(current.getTag()) && current.getTag() != null) {
                    holder.tagTxt.setText(current.getTag());
                } else holder.tagTxt.setText(Long.toString(Long.parseLong(current.getID())%10000));

                if (current.getImageUri("front") != null)
                    Picasso.with(context).load(Uri.parse(current.getImageUri("front"))).centerInside().resize(512, 512).into(holder.imgBtn);
                else
                    Picasso.with(context).load(R.drawable.ic_add_green_800_24dp).into(holder.imgBtn);


                // set background resource (target view ID: container)
                final int dragState = holder.getDragStateFlags();

                if (((dragState & DraggableItemConstants.STATE_FLAG_IS_UPDATED) != 0)) {
                    int bgResId;

                    if ((dragState & DraggableItemConstants.STATE_FLAG_IS_ACTIVE) != 0) {
                        bgResId = R.drawable.bg_item_dragging_active_state;

                        // need to clear drawable state here to get correct appearance of the dragging item.
                        DrawableUtils.clearState(holder.mContainer.getForeground());
                    } else if ((dragState & DraggableItemConstants.STATE_FLAG_DRAGGING) != 0) {
                        bgResId = R.drawable.bg_item_dragging_state;
                    } else {
                        bgResId = R.drawable.bg_item_normal_state;
                    }
                    holder.mContainer.setBackgroundResource(bgResId);
                }
                break;
        }
    }

    public int getPosition() {
        return clickedPos;
    }


    public List<RecyclerData> getStickerList() {
        return tovarList;
    }

    public List<RecyclerData> getOldStickerList() {
        return oldTovarList;
    }

    private RecyclerData delitem, delsection;
    private int secpos;

    public void removeItem(final int position) {

       /* AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setMessage("Вы дейсвтительно хотите удалить этот товар?");
        adb.setNegativeButton("НЕТ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        adb.setPositiveButton("ДА", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (position == tovarList.size() - 1 && tovarList.get(position).getSection() == 0) {
                    Log.v("DATAA", "posledniy ne section");
                    delitem = tovarList.get(position);
                    tovarList.remove(position);
                    notifyItemRemoved(position);
                    if (tovarList.get(position - 1).getSection() == 1) {
                        delsection = tovarList.get(position - 1);
                        secpos = position - 1;
                        tovarList.remove(position - 1);
                        notifyItemRemoved(position - 1);
                    }
                    databaseChangedFlag++;
                } else {
                    // udalyayem element v seredine
                    Log.v("DATAA", "v seredine");

                    delitem = tovarList.get(position);
                    tovarList.remove(position);
                    notifyItemRemoved(position);
                    databaseChangedFlag++;
                    if (tovarList.get(position).getSection() == 1) {
                        Log.v("TOVAR", "Middle of list LIST");
                        if (tovarList.get(position - 1).getSection() == 1) {
                            delsection = tovarList.get(position - 1);
                            secpos = position - 1;
                            tovarList.remove(position - 1);
                            notifyItemRemoved(position - 1);
                            databaseChangedFlag++;

                        }
                    }
                }

            }
        });
        adb.create();
        adb.show();*/

        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "Товар удален", Snackbar.LENGTH_LONG);

        // esli udalim posledniy element kotoriy ne SECTION
        if (position == tovarList.size() - 1 && tovarList.get(position).getSection() == 0) {
            Log.v("DATAA", "posledniy ne section");
            delitem = tovarList.get(position);
            tovarList.remove(position);
            notifyItemRemoved(position);
            if (tovarList.get(position - 1).getSection() == 1) {
                delsection = tovarList.get(position - 1);
                secpos = position - 1;
                tovarList.remove(position - 1);
                notifyItemRemoved(position - 1);
            }
            databaseChangedFlag++;
            snackbar.setAction("Отменить", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (delsection != null) {
                        tovarList.add(secpos, delsection);
                        notifyItemInserted(secpos);
                        databaseChangedFlag--;
                        delsection = null;
                        secpos = -1;
                    }
                    tovarList.add(position, delitem);
                    notifyItemInserted(position);
                    databaseChangedFlag--;
                }
            });
        } else {
            // udalyayem element v seredine
            Log.v("DATAA", "v seredine");

            delitem = tovarList.get(position);
            tovarList.remove(position);
            notifyItemRemoved(position);
            databaseChangedFlag++;
            if (tovarList.get(position).getSection() == 1) {
                Log.v("TOVAR", "Middle of list LIST");
                if (tovarList.get(position - 1).getSection() == 1) {
                    delsection = tovarList.get(position - 1);
                    secpos = position - 1;
                    tovarList.remove(position - 1);
                    notifyItemRemoved(position - 1);
                    databaseChangedFlag++;

                }
            }
            snackbar.setAction("Отменить", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (delsection != null) {
                        tovarList.add(secpos, delsection);
                        notifyItemInserted(secpos);
                        databaseChangedFlag--;
                        delsection = null;
                        secpos = -1;
                    }
                    tovarList.add(position, delitem);
                    notifyItemInserted(position);
                    Log.v("DATAA", "DELETED ITEM URI + " + delitem.getImageUri("front"));
                    databaseChangedFlag--;
                }
            });
        }
        snackbar.show();
    }

    public boolean isDatabaseChanged() {
        return databaseChangedFlag > 0;
    }

    public List<RecyclerData> getFutbolkaList() {
        return futbolkaList;
    }

    public List<RecyclerData> getMaykaList() {
        return maykaList;
    }

    public List<RecyclerData> getPoloList() {
        return poloList;
    }

    @Override
    public long getItemId(int position) {
        return tovarList.get(position).stableID;
    }

    @Override
    public int getItemViewType(int position) {
        return tovarList.get(position).getSection();
    }

    @Override
    public int getItemCount() {
        return tovarList.size();
    }

    @Override
    public void onMoveItem(int fromPosition, int toPosition) {
        if (fromPosition == toPosition) {
            return;
        }
        Toast.makeText(context, "From " + Integer.toString(fromPosition) + " to " + Integer.toString(toPosition), Toast.LENGTH_SHORT).show();
        RecyclerData item = tovarList.remove(fromPosition);
        tovarList.add(toPosition, item);
        notifyItemMoved(fromPosition, toPosition);
        databaseChangedFlag++;
    }

    @Override
    public boolean onCheckCanStartDrag(SectionGridHolder holder, int position, int x, int y) {
        // x, y --- relative from the itemView's top-left

        // return false if the item is a section header
        return holder.getItemViewType() != SECTION;
    }

    @Override
    public ItemDraggableRange onGetItemDraggableRange(SectionGridHolder holder, int position) {
        final int start = findFirstSectionItem(position);
        final int end = findLastSectionItem(position);

        return new ItemDraggableRange(start, end);
    }

    private int findFirstSectionItem(int position) {
        RecyclerData item = tovarList.get(position);

        if (item.getSection() == 1) {
            throw new IllegalStateException("section item is expected");
        }

        while (position > 0) {
            RecyclerData prevItem = tovarList.get(position - 1);

            if (prevItem.getSection() == 1) {
                break;
            }

            position -= 1;
        }

        return position;
    }

    private int findLastSectionItem(int position) {
        RecyclerData item = tovarList.get(position);

        if (item.getSection() == 1) {
            throw new IllegalStateException("section item is expected");
        }

        final int lastIndex = getItemCount() - 1;

        while (position < lastIndex) {
            RecyclerData nextItem = tovarList.get(position + 1);

            if (nextItem.getSection() == 1) {
                break;
            }

            position += 1;
        }

        return position;
    }

    public boolean isPanelOpen() {
        Log.v("DATAA", "isPanelOpen == " + panelOpened);
        return panelOpened;
    }

    public void closePanel() {

        // initialized when backbutton pressed
        if (tovarList.size() != 0) {
            if (drawerAdapter.getDatabase().size() == 0) {
                removeItem(clickedPos);
                panelOpened = false;
                return;
            }

            switch (tovarList.get(clickedPos).getType()) {
                case "Futbolka":
                    futbolkaList.addAll(drawerAdapter.getDatabase());
                    break;
                case "Mayka":
                    maykaList.addAll(drawerAdapter.getDatabase());
                    break;
                case "Polo":
                    poloList.addAll(drawerAdapter.getDatabase());
                    break;
            }
        }

        if (drawerAdapter.snackbar.isShownOrQueued()) {
            drawerAdapter.snackbar.dismiss();
        }
        panelOpened = false;
    }

    public List<RecyclerData> getTovarList() {
        return tovarList;
    }

}
