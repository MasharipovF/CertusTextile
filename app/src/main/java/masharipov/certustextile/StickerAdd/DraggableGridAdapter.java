package masharipov.certustextile.StickerAdd;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

import masharipov.certustextile.StickerAdd.DraggableGridAdapter.GridHolder;
import masharipov.certustextile.R;

public class DraggableGridAdapter extends RecyclerView.Adapter<GridHolder>
        implements DraggableItemAdapter<GridHolder> {

    private Context context;
    private List<GridItem> stickerList;
    private Intent imagePickerIntent;
    private int imageAddPosition = -1, SELECT_PICTURE = 1;


    public static class GridHolder extends AbstractDraggableItemViewHolder implements View.OnClickListener {
        public FrameLayout mContainer;
        public ImageButton imgBtn, btn;
        private onItemClick listener;

        public GridHolder(View v, onItemClick click) {
            super(v);
            listener = click;
            mContainer = (FrameLayout) v.findViewById(R.id.container);
            btn = (ImageButton) v.findViewById(R.id.gridBtn);
            imgBtn = (ImageButton) v.findViewById(R.id.gridImg);
            btn.setOnClickListener(this);
            imgBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.gridBtn:
                    listener.btnClick(getAdapterPosition());
                    break;
                case R.id.gridImg:
                    listener.imgClick(getAdapterPosition(), imgBtn);
                    break;
            }


        }
    }

    public interface onItemClick {
        void btnClick(int position);

        void imgClick(int position, ImageButton img);
    }


    public DraggableGridAdapter(List<GridItem> list, Context ctx) {
        context = ctx;
        stickerList = list;
        setHasStableIds(true);
    }

    @Override
    public GridHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.grid_item, parent, false);
        return new GridHolder(v, new onItemClick() {
            @Override
            public void btnClick(final int position) {
                AlertDialog.Builder adb = new AlertDialog.Builder(context);
                adb.setTitle("Удаление");
                adb.setMessage("Хотите удалить эту наклейку");
                adb.setIcon(android.R.drawable.ic_dialog_info);
                adb.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                adb.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeItem(position);
                    }
                });
                adb.create();
                adb.show();
            }

            @Override
            public void imgClick(int position, ImageButton img) {
                imagePickerIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                imageAddPosition = position;
                ((Activity) context).startActivityForResult(imagePickerIntent, SELECT_PICTURE);
            }
        });
    }

    @Override
    public void onBindViewHolder(GridHolder holder, int position) {
        GridItem current = stickerList.get(position);
        // set text
        if (!current.delButtonVisible)
            holder.btn.setVisibility(View.GONE);
        else
            holder.btn.setVisibility(View.VISIBLE);

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

    public void insertItem(int position) {
        GridItem item = new GridItem();
        item.setID(stickerList.get(position-1).getID()+1);
        stickerList.add(position, item);
        notifyItemInserted(position);
    }


    public void removeItem(int position) {
        stickerList.remove(position);
        notifyItemRemoved(position);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageURI = intent.getData();
                String path;

                if (Build.VERSION.SDK_INT > 21) path = getPath(selectedImageURI);
                else path = selectedImageURI.toString();
                stickerList.get(stickerList.size() - 1).setURI(path);
                stickerList.get(stickerList.size() - 1).delButtonVisible = true;
                notifyItemChanged(imageAddPosition);
                if (path != null)
                    insertItem(stickerList.size());
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


    @Override
    public long getItemId(int position) {
        return stickerList.get(position).getID();
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
        GridItem item = stickerList.remove(fromPosition);
        stickerList.add(toPosition, item);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public boolean onCheckCanStartDrag(GridHolder holder, int position, int x, int y) {
        return true;
    }

    @Override
    public ItemDraggableRange onGetItemDraggableRange(GridHolder holder, int position) {
        // no drag-sortable range specified
        return null;
    }


}
