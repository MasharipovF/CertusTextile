package masharipov.certustextile.stickeradd;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
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
    private Intent imagePickerIntent;
    private int SELECT_PICTURE = 1, visibility;
    final int SECTION = 1, ITEM = 0;

    private int databaseChangedFlag = 0;
    private CoordinatorLayout coordinatorLayout;
    private int tmp = 0, clickedPos = -1;
    private boolean panelOpened = false;

    /// the new implementation
    private List<RecyclerData> loadingList, headerList, childList;
    private boolean isHeader = true;
    private CertusDatabase cDB;
    private Snackbar snackbar;
    private String currentClickedID;


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
                    listener.onImageClick(getAdapterPosition());
            }
        }
    }

    public interface onItemClick {
        void delBtnClick(int position);


        void onImageClick(int position);

    }


    public SectionDraggableGridAdapter(List<RecyclerData> list, Context ctx, int vis, CoordinatorLayout Clayout) {
        context = ctx;
        loadingList = list;
        headerList = list;
        visibility = vis;
        coordinatorLayout = Clayout;
        cDB = new CertusDatabase(context);

        int stID;
        if (!list.isEmpty())
            stID = list.get(list.size() - 1).stableID + 1;
        else stID = 0;
        childList = new ArrayList<>();
        String tableNames[] = {"Futbolka", "Mayka", "Polo"};
        for (String tableName : tableNames) {
            if (cDB.isTableEmpty(tableName)) continue;
            List<RecyclerData> data = cDB.getTovarFromDB(tableName);
            for (int i = 0; i < data.size(); i++) {
                RecyclerData mData = data.get(i);
                mData.stableID = stID++;
                childList.add(mData);
            }

        }
        Log.v("DATAA", "child number = " + Integer.toString(childList.size()));
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
            public void onImageClick(int position) {
                if (isHeader) {
                    currentClickedID = loadingList.get(position).getID();
                    String tablename = loadingList.get(position).getType();
                    setChildList(currentClickedID, position);
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(SectionGridHolder holder, int position) {
        RecyclerData current = loadingList.get(position);
        if (isHeader) {
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
                    holder.tagTxt.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(current.getTag()) && current.getTag() != null) {
                        holder.tagTxt.setText(current.getTag());
                    } else
                        holder.tagTxt.setText(Long.toString(Long.parseLong(current.getID()) % 10000));

                    if (current.getImageUri("front") != null)
                        Picasso.with(context).load(Uri.parse(current.getImageUri("front"))).centerInside().resize(512, 512).into(holder.imgBtn);
                    else
                        Picasso.with(context).load(R.drawable.ic_add_green_800_24dp).into(holder.imgBtn);


                    // set background resource (target view ID: container)
                    initDragState(holder);
                    break;
            }
        } else {
            holder.tagTxt.setVisibility(View.GONE);
            if (current.getImageUri("front") != null)
                Picasso.with(context).load(Uri.parse(current.getImageUri("front"))).centerInside().resize(512, 512).into(holder.imgBtn);
            else
                Picasso.with(context).load(R.drawable.ic_add_green_800_24dp).into(holder.imgBtn);

            // set background resource (target view ID: container)
            initDragState(holder);
        }
    }

    public void setChildList(String searchID, int position) {

        int stID = loadingList.get(position).stableID;
        headerList = loadingList;
        List<RecyclerData> finalData = new ArrayList<>();
        int count = 0;
        while (count < childList.size()) {
            RecyclerData mITem = childList.get(count);

            if (mITem.getID().equals(searchID)) {
                if (finalData.isEmpty()) {
                    mITem.stableID = stID;
                }
                mITem.setSection(0);
                finalData.add(mITem);
                childList.remove(count);
            } else
                count++;
        }

        isHeader = false;
        loadingList = finalData;
        notifyDataSetChanged();
    }

    public void initDragState(SectionGridHolder holder) {
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
    }

    public void updateChildList() {
        childList.addAll(0, loadingList);
    }

    public void setHeaderList() {
        //if (snackbar.isShown()) snackbar.dismiss();
        loadingList = headerList;
        notifyDataSetChanged();
    }

    public boolean getIsHeader() {
        return isHeader;
    }

    public void setIsHeader(boolean b) {
        isHeader = b;
    }

    public void changeHeaderCover() {
        for (int i = 0; i < headerList.size(); i++) {
            RecyclerData mItem = headerList.get(i);
            if (currentClickedID.equals(mItem.getID())) {
                mItem.setImageUri("front", loadingList.get(0).getImageUri("front"));
                break;
            }
        }

    }


    public int getPosition() {
        return clickedPos;
    }


    public List<RecyclerData> getCurrentList() {
        return loadingList;
    }

    public List<RecyclerData> getChildsList() {
        return childList;
    }

    public List<RecyclerData> getHeaderList() {
        return headerList;
    }

    private RecyclerData delitem, delsection, deletedHeaderItem;
    private int secpos, headerpos;
    private boolean isHeaderItemDeleted = false;

    public void removeItem(final int position) {

        snackbar = Snackbar
                .make(coordinatorLayout, "Товар удален", Snackbar.LENGTH_LONG);
        if (isHeader) {
            // esli udalim posledniy element kotoriy ne SECTION
            if (position == loadingList.size() - 1 && loadingList.get(position).getSection() == 0) {
                Log.v("DATAA", "posledniy ne section");
                delitem = loadingList.get(position);
                loadingList.remove(position);
                notifyItemRemoved(position);
                if (loadingList.get(position - 1).getSection() == 1) {
                    delsection = loadingList.get(position - 1);
                    secpos = position - 1;
                    loadingList.remove(position - 1);
                    notifyItemRemoved(position - 1);
                }
                databaseChangedFlag++;
                snackbar.setAction("Отменить", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (delsection != null) {
                            loadingList.add(secpos, delsection);
                            notifyItemInserted(secpos);
                            databaseChangedFlag--;
                            delsection = null;
                            secpos = -1;
                        }
                        loadingList.add(position, delitem);
                        notifyItemInserted(position);
                        databaseChangedFlag--;
                    }
                });
            } else {
                // udalyayem element v seredine
                Log.v("DATAA", "v seredine");

                delitem = loadingList.get(position);
                loadingList.remove(position);
                notifyItemRemoved(position);
                databaseChangedFlag++;
                if (loadingList.get(position).getSection() == 1) {
                    Log.v("TOVAR", "Middle of list LIST");
                    if (loadingList.get(position - 1).getSection() == 1) {
                        delsection = loadingList.get(position - 1);
                        secpos = position - 1;
                        loadingList.remove(position - 1);
                        notifyItemRemoved(position - 1);
                        databaseChangedFlag++;

                    }
                }
                snackbar.setAction("Отменить", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (delsection != null) {
                            loadingList.add(secpos, delsection);
                            notifyItemInserted(secpos);
                            databaseChangedFlag--;
                            delsection = null;
                            secpos = -1;
                        }
                        loadingList.add(position, delitem);
                        notifyItemInserted(position);
                        Log.v("DATAA", "DELETED ITEM URI + " + delitem.getImageUri("front"));
                        databaseChangedFlag--;
                    }
                });
            }
        } else { // if is not header
            final RecyclerData item = loadingList.remove(position);
            notifyItemRemoved(position);
            databaseChangedFlag++;

            RecyclerData hItem;
            if (loadingList.isEmpty()) {
                for (int i = 0; i < headerList.size(); i++) {
                    hItem = headerList.get(i);
                    if (item.getID().equals(hItem.getID())) {
                        deletedHeaderItem = headerList.remove(i);
                        headerpos = i;
                        isHeaderItemDeleted = true;
                        break;
                    }
                }
            }

            snackbar.setAction("Отменить", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadingList.add(position, item);
                    notifyItemInserted(position);
                    databaseChangedFlag--;
                    if (isHeaderItemDeleted) headerList.add(headerpos, deletedHeaderItem);

                    //TODO
                    if (position == 0) {
                        changeHeaderCover();
                    }
                }
            });

            //TODO
            if (position == 0 && loadingList.size() != 0) {
                changeHeaderCover();
            }
        }
        snackbar.show();
    }

    public boolean isDatabaseChanged() {
        return databaseChangedFlag > 0;
    }

    @Override
    public long getItemId(int position) {
        return loadingList.get(position).stableID;
    }

    @Override
    public int getItemViewType(int position) {
        return loadingList.get(position).getSection();
    }

    @Override
    public int getItemCount() {
        return loadingList.size();
    }

    @Override
    public void onMoveItem(int fromPosition, int toPosition) {
        if (fromPosition == toPosition) {
            return;
        }
        RecyclerData item = loadingList.remove(fromPosition);
        loadingList.add(toPosition, item);
        notifyItemMoved(fromPosition, toPosition);

        if (!isHeader)
            if (toPosition == 0) {
                changeHeaderCover();
            }


        /*RecyclerData rItem = new RecyclerData();
        if (!isHeader) {
            for (int i = 0; i < childList.size(); i++) {
                rItem = childList.get(i);
                if (item.getImageUri("front").equals(rItem.getImageUri("front")) && item.getID().equals(rItem.getID())) {
                    childList.remove(i);
                    break;
                }
            }
            childList.add(0, rItem);
        }*/

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
        RecyclerData item = loadingList.get(position);

        if (item.getSection() == 1) {
            throw new IllegalStateException("section item is expected");
        }

        while (position > 0) {
            RecyclerData prevItem = loadingList.get(position - 1);

            if (prevItem.getSection() == 1) {
                break;
            }

            position -= 1;
        }

        return position;
    }

    private int findLastSectionItem(int position) {
        RecyclerData item = loadingList.get(position);

        if (item.getSection() == 1) {
            throw new IllegalStateException("section item is expected");
        }

        final int lastIndex = getItemCount() - 1;

        while (position < lastIndex) {
            RecyclerData nextItem = loadingList.get(position + 1);

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

    /*public void closePanel() {

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
    }*/

    public List<RecyclerData> getTovarList() {
        return loadingList;
    }

}
