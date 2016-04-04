package masharipov.certustextile;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;


public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageButton style, front, back, side, add;
    public Spinner size_spin;
    public onItemClick listener;

    public ViewHolder(View itemView, final onItemClick listen) {
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

        size_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listener.onSizeSpinnerSelect(size_spin.getSelectedItem().toString(), size_spin.getSelectedItemPosition(), getAdapterPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                listener.onAddorRemove(getAdapterPosition());
                break;
            default:
                break;
        }
    }

    public interface onItemClick {
        void onImageAdd(ImageButton img, int position, String imgID);

        void onSizeSpinnerSelect(String item, int itemPos,  int position);

        void onAddorRemove(int position);
    }
}
