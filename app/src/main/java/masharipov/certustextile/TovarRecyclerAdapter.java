package masharipov.certustextile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import masharipov.certustextile.edit.RecyclerData;
import masharipov.certustextile.stickeradd.TovarData;

/**
 * Created by developer on 09.04.2016.
 */
public class TovarRecyclerAdapter extends RecyclerView.Adapter<SecondViewHolder> implements View.OnClickListener {

    private Context context;
    private List<RecyclerData> tovarList;
    int height;
    private clickListener listener;

    public TovarRecyclerAdapter(Context ctx, List<RecyclerData> list, int h, clickListener l) {
        context = ctx;
        tovarList = list;
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
                listener.onItemClick(img, position, tovarList);
            }
        });
        return holder;

    }

    @Override
    public void onBindViewHolder(SecondViewHolder holder, int position) {
        RecyclerData current = tovarList.get(position);

        if (current.getImageUri("front") != null)
            Picasso.with(context).load(Uri.parse(current.getImageUri("front"))).centerInside().resize(512, 512).into(holder.imageView);
        else if (current.getGenderImageResourse() != -1)
            Picasso.with(context).load(current.getGenderImageResourse()).into(holder.imageView);
        else
            Picasso.with(context).load(R.drawable.ic_add_green_800_24dp).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return tovarList.size();
    }

    @Override
    public void onClick(View v) {

    }

    public void changeList(List<RecyclerData> list) {
        tovarList = list;
        notifyDataSetChanged();
    }

    public interface clickListener {
        void onItemClick(ImageView img, int position, List<RecyclerData> dataList);
    }
}

