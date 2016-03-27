package masharipov.certustextile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {

    private LayoutInflater inflater;
    private List<RecyclerData> database;
    private ArrayAdapter<String> spinAdapter;
    private Context context;
    private Intent pickerIntent;
    private Uri selectedImageURI = null;
    private int SELECT_PICTURE = 1, imageAddPosition = -1;
    private String imageID = null;


    public RecyclerAdapter(Context context, List<RecyclerData> list) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        database = list;
        String spinElements[] = {"XS", "S", "M", "L", "XL", "XXL"};
        ArrayList<String> spinList = new ArrayList<>();
        Collections.addAll(spinList, spinElements);
        spinAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, spinList);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.addclothes_row, parent, false);
        ViewHolder holder = new ViewHolder(view, new onItemClick() {
            @Override
            public void onImageAdd(ImageButton img, int position, String imgID) {
                pickerIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                imageAddPosition = position;
                imageID = imgID;
                Toast.makeText(context, imgID + " and position " + Integer.toString(position), Toast.LENGTH_SHORT).show();
                ((Activity) context).setResult(Activity.RESULT_OK, pickerIntent);
                ((Activity) context).startActivityForResult(pickerIntent, SELECT_PICTURE);
            }

            @Override
            public void onAddorRemove() {
                insertItem(new RecyclerData(), database.size()-1);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final RecyclerData current = database.get(position);
        if (current.styleUri != null)
            Picasso.with(context).load(current.styleUri).into(holder.style);
        if (current.frontUri != null)
            Picasso.with(context).load(current.frontUri).into(holder.front);
        if (current.backUri != null)
            Picasso.with(context).load(current.backUri).into(holder.back);
        if (current.sideUri != null)
            Picasso.with(context).load(current.sideUri).into(holder.side);
        holder.size_spin.setAdapter(spinAdapter);
    }

    @Override
    public int getItemCount() {
        return database.size();
    }

    public void deleteItem(int position) {
        database.remove(position);
        notifyItemRemoved(position);
    }

    public void insertItem(RecyclerData mItem, int position) {
        database.add(position, mItem);
        notifyItemInserted(position);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageURI = intent.getData();
                database.get(imageAddPosition).setImageUri(imageID, selectedImageURI);
                Toast.makeText(context, imageID + " " + Integer.toString(imageAddPosition), Toast.LENGTH_SHORT).show();
                notifyItemChanged(imageAddPosition);
                selectedImageURI = null;
                imageAddPosition = -1;
                imageID = null;
            }
        }
    }


    public interface onItemClick {
        void onImageAdd(ImageButton img, int position, String imgID);

        void onAddorRemove();
    }
}
