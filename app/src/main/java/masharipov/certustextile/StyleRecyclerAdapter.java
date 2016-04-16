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

/**
 * Created by Farrukh on 14.04.2016.
 */
public class StyleRecyclerAdapter extends RecyclerView.Adapter<SecondViewHolder> implements View.OnClickListener {

    private Context context;
    private List<RecyclerData> styleList;
    private int height;
    private clickListener listener;
    private RecyclerView.LayoutParams layoutParams;


    public StyleRecyclerAdapter(Context c, List<RecyclerData> list, int h, clickListener l) {
        context = c;
        styleList = list;
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
        layoutParams = (RecyclerView.LayoutParams) holder.imageView.getLayoutParams();
        layoutParams.height = height / 3;
        RecyclerData current = styleList.get(position);
        if (current.getImageUri("style") != null)
            Picasso.with(context).load(Uri.parse(current.getImageUri("style"))).centerInside().resize(512, 512).into(holder.imageView);
        else
            Picasso.with(context).load(R.drawable.ic_action_new_picture).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return styleList.size();
    }

    @Override
    public void onClick(View v) {
    }

    public void changeStyleList(List<RecyclerData> list) {
        styleList = list;
        notifyDataSetChanged();
    }

    public interface clickListener {
        void onItemClick(ImageView img, int position);
    }

    public void setImageParams(int h) {
        height = h;
        notifyDataSetChanged();
    }
}
