package masharipov.certustextile.stickeradd;

import android.app.Activity;
import android.app.Dialog;
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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
import masharipov.certustextile.edit.RecyclerData;
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
    private onFabVisibilityChange fabChanded;
    public boolean isAlbum = true;

    public DraggableGridAdapter(List<StickerData> list, Context ctx, CoordinatorLayout layout, int vis) {
        context = ctx;
        stickerList = list;
        oldStickerList = list;
        coordinatorLayout = layout;
        visibility = vis;
        setHasStableIds(true);
    }

    public DraggableGridAdapter(List<StickerData> list, Context ctx, CoordinatorLayout layout, int vis, onFabVisibilityChange fabVisibilityChange) {
        context = ctx;
        stickerList = list;
        oldStickerList = list;
        coordinatorLayout = layout;
        visibility = vis;
        fabChanded = fabVisibilityChange;
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
                case R.id.gridImg:
                    listener.imgClick(getAdapterPosition(), tagTxt);
                    break;

            }
        }
    }

    public interface onItemClick {
        void delBtnClick(int position);

        void editBtnClick(int position);

        void imgClick(int position, TextView tag);
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

            @Override
            public void imgClick(final int position, final TextView tag) {
                if (isAlbum && position == stickerList.size() - 1) {
                    final Dialog dialog = new Dialog(context);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.password_prompt);
                    dialog.setCancelable(true);

                    final EditText albumName = (EditText) dialog.findViewById(R.id.editTextDialogUserInput);
                    Button okBtn = (Button) dialog.findViewById(R.id.dialogPos);
                    Button cancelBtn = (Button) dialog.findViewById(R.id.dialogNeg);
                    dialog.findViewById(R.id.changepass).setVisibility(View.GONE);
                    okBtn.setText("ДОБАВИТЬ");
                    // albumName.set
                    okBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (TextUtils.isEmpty(albumName.getText().toString())) {
                                albumName.setError("Название альбома не может быть пустым!");
                            } else {
                                tag.setText(albumName.getText().toString());
                                stickerList.get(position).setTAG(tag.getText().toString());
                                stickerList.get(position).setURI("fff"); // TODO URIni tanlash kerak, hozi test uchun prosto
                                notifyItemChanged(position);
                                StickerData mItem = new StickerData();
                                mItem.setTAG("Новый альбом");
                                mItem.setID(Long.toString(System.currentTimeMillis()));
                                stickerList.add(position + 1, mItem);
                                notifyItemInserted(position + 1);
                                // dlya perexoda v spisok stickerov v albome  showItemsOfAlbum();
                                dialog.dismiss();
                            }
                        }
                    });
                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            isAlbum = true;
                        }
                    });
                    dialog.show();
                    //isAlbum = false; //TODO AGAR albom qowilsa unga item qowiw kere
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(GridHolder holder, int position) {
        StickerData current = stickerList.get(position);
        // set text
        if (current.getURI() != null) {
            holder.stickerBtn.setVisibility(View.VISIBLE);
            Picasso.with(context).load(Uri.parse(current.getURI())).centerInside().resize(512, 512).into(holder.imgBtn);
        } else {
            holder.stickerBtn.setVisibility(View.GONE);
            Picasso.with(context).load(R.drawable.ic_action_new_picture).into(holder.imgBtn);
        }

        if (current.getTAG() != null) {
            holder.tagTxt.setText(current.getTAG());
        } else {
            holder.tagTxt.setText("lalala");
        }

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

    public boolean isAlbumShown() {
        return isAlbum;
    }


    public void showItemsOfAlbum() {
        fabChanded.setFABVisibility(View.VISIBLE);
    }

    public void insertItem(String path) {
        String uniqueID = Long.toString(System.currentTimeMillis());
        StickerData item = new StickerData();
        item.setID(uniqueID);
        item.setURI(path);
        stickerList.add(0, item);
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

    public interface onFabVisibilityChange {
        void setFABVisibility(int visibility);
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
        if (!isAlbum)
            return !disableDrag;
        else {
            return position != stickerList.size() - 1;
        }
    }

    @Override
    public ItemDraggableRange onGetItemDraggableRange(GridHolder holder, int position) {
        // no drag-sortable range specified
        if (!isAlbum)
            return null;
        else {
            return new ItemDraggableRange(0, stickerList.size() - 2);
        }
    }


}
