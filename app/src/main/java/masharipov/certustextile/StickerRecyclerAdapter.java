package masharipov.certustextile;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import masharipov.certustextile.edit.RecyclerData;
import masharipov.certustextile.stickeradd.StickerData;

/**
 * Created by Farrukh on 14.04.2016.
 */
public class StickerRecyclerAdapter extends RecyclerView.Adapter<SecondViewHolder> implements View.OnClickListener {

    private Context context;
    private List<StickerData> stickerlist;
    private int height;
    private clickListener listener;

    public StickerRecyclerAdapter(Context c, List<StickerData> list, int h, clickListener l) {
        context = c;
        stickerlist = list;
        height = h;
        listener = l;
    }

    @Override
    public SecondViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, height / 3));
        SecondViewHolder holder = new SecondViewHolder(imageView, context, new SecondViewHolder.onClick() {
            @Override
            public void onClick(ImageView img, int position) {
                listener.onItemClick(img, position);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(SecondViewHolder holder, int position) {
        StickerData current = stickerlist.get(position);
        if (current.getURI() != null)
            Picasso.with(context).load(Uri.parse(current.getURI())).centerInside().resize(512, 512).into(holder.imageView);
        else
            Picasso.with(context).load(R.drawable.ic_add_green_800_24dp).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return stickerlist.size();
    }

    @Override
    public void onClick(View v) {

    }

    public interface clickListener {
        void onItemClick(ImageView img, int position);
    }
}
