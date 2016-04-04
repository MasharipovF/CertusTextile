package masharipov.certustextile;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Spinner;


public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageButton style, front, back, side, add;
    public Spinner size_spin;
    public RecyclerAdapter.onItemClick listener;

    public ViewHolder(View itemView, RecyclerAdapter.onItemClick listen) {
        super(itemView);
        listener = listen;
        style = (ImageButton) itemView.findViewById(R.id.addstyle);
        front = (ImageButton) itemView.findViewById(R.id.addfront);
        back = (ImageButton) itemView.findViewById(R.id.addback);
        side = (ImageButton) itemView.findViewById(R.id.addside);
        add = (ImageButton) itemView.findViewById(R.id.add);
        size_spin = (Spinner) itemView.findViewById(R.id.size_spin);

        style.setOnClickListener(this);
        front.setOnClickListener(this);
        back.setOnClickListener(this);
        side.setOnClickListener(this);
        add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addstyle:
                listener.onImageAdd(style, getAdapterPosition(), "style");
                break;
            case R.id.addfront:
                listener.onImageAdd(front, getAdapterPosition(), "front");
                break;
            case R.id.addback:
                listener.onImageAdd(back, getAdapterPosition(), "back");
                break;
            case R.id.addside:
                listener.onImageAdd(side, getAdapterPosition(), "side");
                break;
            case R.id.add:
                listener.onAddorRemove();
                break;
            default:
                break;
        }
    }
}
