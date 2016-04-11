package masharipov.certustextile.stickeradd;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
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

import java.util.List;

import masharipov.certustextile.R;


public class SectionDraggableGridAdapter extends RecyclerView.Adapter<SectionDraggableGridAdapter.SectionGridHolder>
        implements DraggableItemAdapter<SectionDraggableGridAdapter.SectionGridHolder> {

    private Context context;
    private List<TovarData> tovarList, oldTovarList;
    private Intent imagePickerIntent;
    private int SELECT_PICTURE = 1, editButtonVisibility;
    final int SECTION = 1, ITEM = 0;
    private boolean databaseChangedFlag = false;


    public static class SectionGridHolder extends AbstractDraggableItemViewHolder implements View.OnClickListener {
        public FrameLayout mContainer;
        public ImageView imgBtn, stickerBtn, editBtn;
        private onItemClick listener;
        private TextView sectionText;

        // VIEWHOLDER
        public SectionGridHolder(View v, int viewType, onItemClick click) {
            super(v);
            switch (viewType) {
                case 1:
                    sectionText = (TextView) v.findViewById(R.id.sectionText);
                    break;
                case 0:
                    listener = click;
                    mContainer = (FrameLayout) v.findViewById(R.id.container);
                    stickerBtn = (ImageButton) v.findViewById(R.id.gridDelBtn);
                    editBtn = (ImageButton) v.findViewById(R.id.gridEditBtn);
                    imgBtn = (ImageView) v.findViewById(R.id.gridImg);
                    editBtn.setVisibility(View.VISIBLE);
                    stickerBtn.setOnClickListener(this);
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
                case R.id.gridEditBtn:
                    listener.editBtnClick(getAdapterPosition());
                    break;
            }
        }
    }

    public interface onItemClick {
        void delBtnClick(int position);

        void editBtnClick(int position);

        // void imgClick(int position, ImageButton img);
    }


    public SectionDraggableGridAdapter(List<TovarData> list, Context ctx, int editVisibility) {
        context = ctx;
        tovarList = list;
        oldTovarList = list;
        editButtonVisibility = editVisibility;
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
        return new SectionGridHolder(v, viewType, new onItemClick() {
            @Override
            public void delBtnClick(final int position) {
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
            public void editBtnClick(int position) {

            }
        });
    }

    @Override
    public void onBindViewHolder(SectionGridHolder holder, int position) {
        TovarData current = tovarList.get(position);
        switch (current.getType()) {
            case SECTION:
                holder.sectionText.setText(current.getSectionText());
                break;
            case ITEM:
                if (current.getFRONT() != null)
                    Picasso.with(context).load(Uri.parse(current.getFRONT())).centerInside().resize(512, 512).into(holder.imgBtn);
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

    public List<TovarData> getStickerList() {
        return tovarList;
    }

    public List<TovarData> getOldStickerList() {
        return oldTovarList;
    }

    public void removeItem(int position) {

        // esli udalim posledniy element kotoriy ne SECTION
        if (position == tovarList.size() - 1 && tovarList.get(position).getType() == 0) {
            tovarList.remove(position);
            notifyItemRemoved(position);
            if (tovarList.get(position - 1).getType() == 1) {
                tovarList.remove(position - 1);
                notifyItemRemoved(position - 1);
            }
            databaseChangedFlag = true;
            return;
        }


        tovarList.remove(position);
        notifyItemRemoved(position);
        databaseChangedFlag = true;

        if (tovarList.get(position).getType() == 1) {
            Log.v("TOVAR", "Middle of list LIST");
            if (tovarList.get(position - 1).getType() == 1) {
                tovarList.remove(position - 1);
                notifyItemRemoved(position - 1);
                databaseChangedFlag = true;

            }
        }


        /*if (position == 0 && tovarList.get(position).getType() == 1) {
            Log.v("TOVAR", "Beginning of LIST");
            if (tovarList.get(position + 1).getType() == 1) {
                tovarList.remove(position);
                notifyItemRemoved(position);
            }
        } else if (position == tovarList.size() - 1 && tovarList.get(position).getType() == 1) {
            Log.v("TOVAR", "END of LIST");
            if (tovarList.get(position - 1).getType() == 1) {
                tovarList.remove(position);
                notifyItemRemoved(position);
            }
        } else if (tovarList.get(position).getType() == 1) {
            Log.v("TOVAR", "Middle of list LIST");
            if (tovarList.get(position - 1).getType() == 1 || tovarList.get(position + 1).getType() == 1) {
                tovarList.remove(position - 1);
                notifyItemRemoved(position - 1);
            }}
        // esli i sleduyuwiy i predidiuwiy element SECTION to udali tekuwiy SECTION
       /* if (tovarList.get(position).getType() == 1) {
            Log.v("TOVAR", "is Section  " + Integer.toString(position));

        }*/

    }

    public boolean isDatabaseChanged() {
        return databaseChangedFlag;
    }

    @Override
    public long getItemId(int position) {
        return tovarList.get(position).idiwka;
    }

    @Override
    public int getItemViewType(int position) {
        return tovarList.get(position).getType();
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
        TovarData item = tovarList.remove(fromPosition);
        tovarList.add(toPosition, item);
        notifyItemMoved(fromPosition, toPosition);
        databaseChangedFlag = true;
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
        TovarData item = tovarList.get(position);

        if (item.isSection == 1) {
            throw new IllegalStateException("section item is expected");
        }

        while (position > 0) {
            TovarData prevItem = tovarList.get(position - 1);

            if (prevItem.isSection == 1) {
                break;
            }

            position -= 1;
        }

        return position;
    }

    private int findLastSectionItem(int position) {
        TovarData item = tovarList.get(position);

        if (item.isSection == 1) {
            throw new IllegalStateException("section item is expected");
        }

        final int lastIndex = getItemCount() - 1;

        while (position < lastIndex) {
            TovarData nextItem = tovarList.get(position + 1);

            if (nextItem.isSection == 1) {
                break;
            }

            position += 1;
        }

        return position;
    }

}
