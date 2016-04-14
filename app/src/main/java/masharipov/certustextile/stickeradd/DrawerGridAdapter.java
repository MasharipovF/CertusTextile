package masharipov.certustextile.stickeradd;

import android.content.Context;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import masharipov.certustextile.CertusDatabase;
import masharipov.certustextile.R;


public class DrawerGridAdapter extends RecyclerView.Adapter<DrawerGridAdapter.DrawerHolder> {

    private Context context;
    private List<TovarData> tovarDataList;
    private int editButtonVisibility;
    private int databaseChangedFlag = 0;
    private LinearLayout linearLayout;
    private List<TovarData> drawerData;
    private SectionDraggableGridAdapter tovarDataAdapter;


    public static class DrawerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public FrameLayout mContainer;
        public ImageView imgBtn, stickerBtn, editBtn;
        private onItemClick listener;

        // VIEWHOLDER
        public DrawerHolder(View v, int visibility, onItemClick click) {
            super(v);
            listener = click;
            mContainer = (FrameLayout) v.findViewById(R.id.container);
            stickerBtn = (ImageButton) v.findViewById(R.id.gridDelBtn);
            editBtn = (ImageButton) v.findViewById(R.id.gridEditBtn);
            imgBtn = (ImageView) v.findViewById(R.id.gridImg);
            editBtn.setVisibility(visibility);
            stickerBtn.setOnClickListener(this);
            imgBtn.setOnClickListener(this);
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
                case R.id.gridImg:
                    listener.onImageClicked(getAdapterPosition());
                    break;
            }
        }
    }

    public interface onItemClick {
        void delBtnClick(int position);

        void editBtnClick(int position);

        void onImageClicked(int position);

        // void imgClick(int position, ImageButton img);
    }


    public DrawerGridAdapter(Context ctx, int editVisibility, LinearLayout layout, SectionDraggableGridAdapter adapter) {
        context = ctx;
        tovarDataAdapter = adapter;
        drawerData = new ArrayList<>();
        editButtonVisibility = editVisibility;
        linearLayout = layout;
        setHasStableIds(true);
    }

    @Override
    public DrawerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.grid_item, parent, false);
        return new DrawerHolder(v, editButtonVisibility, new onItemClick() {
            @Override
            public void delBtnClick(final int position) {
                Log.v("TOVAR", "ITEM REMOVED");
                removeItem(position);
            }

            @Override
            public void editBtnClick(int position) {
            }

            @Override
            public void onImageClicked(int position) {
            }
        });
    }

    @Override
    public void onBindViewHolder(DrawerHolder holder, int position) {
        TovarData current = drawerData.get(position);
        // set text
        if (current.getFRONT() != null)
            Picasso.with(context).load(Uri.parse(current.getFRONT())).centerInside().resize(512, 512).into(holder.imgBtn);
        else
            Picasso.with(context).load(R.drawable.ic_add_green_800_24dp).into(holder.imgBtn);
    }


    public void setDataBase(List<TovarData> data) {
        drawerData = data;
        notifyDataSetChanged();
    }

    public void clearDatabase() {
        drawerData.clear();
        notifyDataSetChanged();
    }

    public void removeItem(final int position) {
        final TovarData item = drawerData.get(position);
        drawerData.remove(position);
        notifyItemRemoved(position);
        databaseChangedFlag++;

        Snackbar snackbar = Snackbar
                .make(linearLayout, "Товар удален", Snackbar.LENGTH_LONG)
                .setAction("Отменить", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        drawerData.add(position, item);
                        notifyItemInserted(position);
                        databaseChangedFlag--;
                    }
                });

        snackbar.show();
    }

    public boolean isDatabaseChanged() {
        return databaseChangedFlag > 0;
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(drawerData.get(position).getID());
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return drawerData.size();
    }
}
