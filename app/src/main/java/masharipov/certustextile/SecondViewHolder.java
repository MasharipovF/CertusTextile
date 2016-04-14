package masharipov.certustextile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by developer on 09.04.2016.
 */
public class SecondViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView imageView;
    private Context context;
    private onClick listener;

    public SecondViewHolder(View itemView, Context ctx, final onClick listen) {
        super(itemView);
        listener = listen;
        context = ctx;
        imageView = (ImageView) itemView;
        imageView.setOnClickListener(this);
        imageView.setClickable(true);
    }

    @Override
    public void onClick(View v) {
        listener.onClick(imageView, getAdapterPosition());
    }

    public interface onClick {
        void onClick(ImageView img, int position);
    }
}
