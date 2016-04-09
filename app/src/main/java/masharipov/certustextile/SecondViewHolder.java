package masharipov.certustextile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by developer on 09.04.2016.
 */
public class SecondViewHolder extends RecyclerView.ViewHolder {

    public ImageView imageView;
    private Context context;

    public SecondViewHolder(View itemView, Context ctx, int height) {
        super(itemView);
        context = ctx;
        /*
        imageView = new ImageView(context);
        imageView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, height/3));
        imageView.setImageResource(R.drawable.futblka);*/
        imageView = (ImageView) itemView;

    }
}
