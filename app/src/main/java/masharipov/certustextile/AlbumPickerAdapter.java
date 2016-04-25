package masharipov.certustextile;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import masharipov.certustextile.stickeradd.StickerData;

/**
 * Created by Farrukh on 24.04.2016.
 */
public class AlbumPickerAdapter extends RecyclerView.Adapter<AlbumPickerAdapter.Albumholder> {

    private List<StickerData> loadingList, albumList, stickerList, oldStickerList;
    Context context;
    int visibility;
    public boolean isAlbum = true;
    public String currentAlbumTag;
    public albumListener albumListener;

    public AlbumPickerAdapter(Context c, List<StickerData> list, albumListener listener) {
        context = c;
        albumListener = listener;
        albumList = new ArrayList<>();
        stickerList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isAlbum() == 1) albumList.add(list.get(i));
            else stickerList.add(list.get(i));
        }
        loadingList = albumList;
        Log.v("DATAA", "ALBUM = " + Integer.toString(albumList.size()) + "  STICKER = " + Integer.toString(stickerList.size()));
    }

    @Override
    public Albumholder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        final View v = inflater.inflate(R.layout.grid_item, parent, false);
        return new Albumholder(v, new onItemClick() {
            @Override
            public void imgClick(int position, TextView tag) {
                if (isAlbum) {
                    currentAlbumTag = loadingList.get(position).getTAG();
                    setStickerData(currentAlbumTag);
                    isAlbum = false;
                    albumListener.onAlbumClicked();
                } else {
                    albumListener.onStickerPicked(loadingList.get(position));
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(Albumholder holder, int position) {
        StickerData current = loadingList.get(position);
        // set text

        if (isAlbum) {
            holder.tagTxt.setVisibility(View.VISIBLE);
            if (current.getTAG() != null) {
                holder.tagTxt.setText(current.getTAG());
            } else {
                holder.tagTxt.setText("lalala");
            }
        } else {
            holder.tagTxt.setVisibility(View.GONE);
        }

        if (current.getURI() != null) {
            Picasso.with(context).load(Uri.parse(current.getURI())).centerInside().resize(512, 512).into(holder.imgBtn);
        }
    }

    @Override
    public int getItemCount() {
        return loadingList.size();
    }


    public interface albumListener {
        void onStickerPicked(StickerData sticker);
        void onAlbumClicked();
    }

    public List<StickerData> getList() {
        return loadingList;
    }

    public void setList(List<StickerData> mList) {
        loadingList = mList;
        notifyDataSetChanged();
    }

    public void setAlbumData() {
        loadingList = albumList;
        notifyDataSetChanged();
    }

    public List<StickerData> setStickerData(String sortTag) {
        albumList = loadingList;

        List<StickerData> finalData = new ArrayList<>();
        int count = 0;
        for (StickerData item : stickerList) {
            if (item.getTAG().equals(sortTag))
                finalData.add(item);
        }

        loadingList = finalData;
        notifyDataSetChanged();
        return finalData;
    }


    public class Albumholder extends RecyclerView.ViewHolder {


        public FrameLayout mContainer;
        public ImageView imgBtn, delBtn;
        TextView tagTxt;
        private onItemClick listener;


        // VIEWHOLDER
        public Albumholder(View v, onItemClick click) {
            super(v);
            listener = click;
            mContainer = (FrameLayout) v.findViewById(R.id.container);
            delBtn = (ImageButton) v.findViewById(R.id.gridDelBtn);
            delBtn.setVisibility(View.GONE);
            imgBtn = (ImageView) v.findViewById(R.id.gridImg);
            tagTxt = (TextView) v.findViewById(R.id.gridItemTag);
            imgBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.imgClick(getAdapterPosition(), tagTxt);
                }
            });
        }


    }


        /*public Albumholder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.albumImg);
            txt = (TextView) itemView.findViewById(R.id.albumtxt);
            check = (CheckBox) itemView.findViewById(R.id.albumCheck);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.get(getAdapterPosition()).getIsChecked() == 0) {
                        list.get(getAdapterPosition()).setChecked(1);
                        check.setChecked(true);
                    } else {
                        list.get(getAdapterPosition()).setChecked(0);
                        check.setChecked(false);
                    }
                }
            });
        }*/

    public interface onItemClick {
        void imgClick(int position, TextView tag);

    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(loadingList.get(position).getID());
    }

}
