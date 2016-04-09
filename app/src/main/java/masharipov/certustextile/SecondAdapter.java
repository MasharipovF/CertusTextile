package masharipov.certustextile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by developer on 09.04.2016.
 */
public class SecondAdapter extends RecyclerView.Adapter<SecondViewHolder> {

    private Context context;
    private List<Integer> images;
    int width, height;

    public SecondAdapter(Context ctx, List<Integer> list, int h) {
        context = ctx;
        images = list;
        height = h;
    }

    @Override
    public SecondViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, height/3));
        SecondViewHolder holder = new SecondViewHolder(imageView, context, height);
        return holder;

    }

    @Override
    public void onBindViewHolder(SecondViewHolder holder, int position) {
        holder.imageView.setImageResource(R.drawable.futblka);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}
