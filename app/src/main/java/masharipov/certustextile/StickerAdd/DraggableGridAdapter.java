package masharipov.certustextile.stickeradd;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
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

import java.util.List;

import masharipov.certustextile.stickeradd.DraggableGridAdapter.GridHolder;
import masharipov.certustextile.R;


public class DraggableGridAdapter extends RecyclerView.Adapter<GridHolder>
        implements DraggableItemAdapter<GridHolder> {

    private Context context;
    private List<StickerData> stickerList, oldStickerList;
    private Intent imagePickerIntent;
    private int SELECT_PICTURE = 1, visibility, delButtonVisibility;
    private int databaseChangedFlag = 0;
    private CoordinatorLayout coordinatorLayout;
    public static boolean disableDrag = false;

    public DraggableGridAdapter(List<StickerData> list, Context ctx, CoordinatorLayout layout, int vis) {
        context = ctx;
        stickerList = list;
        oldStickerList = list;
        coordinatorLayout = layout;
        visibility = vis;
        setHasStableIds(true);
    }

    public static class GridHolder extends AbstractDraggableItemViewHolder implements View.OnClickListener {
        public FrameLayout mContainer;
        public ImageView imgBtn, stickerBtn;
        TextView tagTxt;
        private onItemClick listener;


        // VIEWHOLDER
        public GridHolder(View v, int visibility, onItemClick click) {
            super(v);
            listener = click;
            mContainer = (FrameLayout) v.findViewById(R.id.container);
            stickerBtn = (ImageButton) v.findViewById(R.id.gridDelBtn);
            imgBtn = (ImageView) v.findViewById(R.id.gridImg);
            tagTxt = (TextView) v.findViewById(R.id.gridItemTag);
            tagTxt.setVisibility(visibility);
            stickerBtn.setOnClickListener(this);
            imgBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.gridDelBtn:
                    listener.delBtnClick(getAdapterPosition());
                    break;

            }
        }
    }

    public interface onItemClick {
        void delBtnClick(int position);

        void editBtnClick(int position);

        // void imgClick(int position, ImageButton img);
    }


    @Override
    public GridHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.grid_item, parent, false);
        return new GridHolder(v, visibility, new onItemClick() {
            @Override
            public void delBtnClick(final int position) {
                removeItem(position);
            }

            @Override
            public void editBtnClick(int position) {
            }
        });
    }

    @Override
    public void onBindViewHolder(GridHolder holder, int position) {
        StickerData current = stickerList.get(position);
        // set text
        if (current.getURI() != null)
            Picasso.with(context).load(Uri.parse(current.getURI())).centerInside().resize(512, 512).into(holder.imgBtn);
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
    }

    public List<StickerData> getStickerList() {
        return stickerList;
    }

    public List<StickerData> getOldStickerList() {
        return oldStickerList;
    }

    public void insertItem(String path) {
        String uniqueID = Long.toString(System.currentTimeMillis());
        StickerData item = new StickerData();
        item.setID(uniqueID);
        item.setURI(path);
        stickerList.add(0,item);
        notifyItemInserted(0);
        databaseChangedFlag++;

        /*if (stickerList.size() == 0) {
            item.setID(uniqueID);
            item.setURI(path);
            stickerList.add(item);
            notifyItemInserted(0);
            databaseChangedFlag++;
        } else {
            item.setID(uniqueID);
            item.setURI(path);
            stickerList.add(item);
            notifyItemInserted(stickerList.size() - 1);
            databaseChangedFlag++;
        }*/
    }


    public void removeItem(final int position) {
        final StickerData item = stickerList.get(position);
        stickerList.remove(position);
        notifyItemRemoved(position);
        databaseChangedFlag++;

        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "Удалено", Snackbar.LENGTH_LONG)
                .setAction("Отменить", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        stickerList.add(position, item);
                        notifyItemInserted(position);
                        databaseChangedFlag--;
                    }
                });

        snackbar.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageURI = intent.getData();
                String path;

                path = selectedImageURI.toString();
                if (path != null) {
                    insertItem(path);
                }
            }
        }
    }

    public String getPath(Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

    public void pickImage() {
        imagePickerIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ((Activity) context).startActivityForResult(imagePickerIntent, SELECT_PICTURE);
    }

    public boolean isDatabaseChanged() {
        return databaseChangedFlag > 0;
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(stickerList.get(position).getID());
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return stickerList.size();
    }

    @Override
    public void onMoveItem(int fromPosition, int toPosition) {
        if (fromPosition == toPosition) {
            return;
        }
        Toast.makeText(context, "From " + Integer.toString(fromPosition) + " to " + Integer.toString(toPosition), Toast.LENGTH_SHORT).show();
        StickerData item = stickerList.remove(fromPosition);
        stickerList.add(toPosition, item);
        notifyItemMoved(fromPosition, toPosition);
        databaseChangedFlag++;
    }

    @Override
    public boolean onCheckCanStartDrag(GridHolder holder, int position, int x, int y) {
        return !disableDrag;
    }

    @Override
    public ItemDraggableRange onGetItemDraggableRange(GridHolder holder, int position) {
        // no drag-sortable range specified
        return null;
    }


}
