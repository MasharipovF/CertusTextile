package masharipov.certustextile.edit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import masharipov.certustextile.R;


public class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {

    private LayoutInflater inflater;
    private List<RecyclerData> database;
    private RecyclerData recyclerData;
    private ArrayAdapter<String> spinAdapter;
    private Context context;
    private Intent imagePickerIntent;
    private int SELECT_PICTURE = 1, imageAddPosition = -1;
    private String imageID = null;
    public int collarTag = -1;
    private RelativeLayout layout;

    public RecyclerAdapter(Context context, List<RecyclerData> list, RelativeLayout lay) {
        layout = lay;
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view = inflater.inflate(R.layout.addclothes_row, parent, false);
        ViewHolder holder = new ViewHolder(view, new ViewHolder.onItemClick() {
            @Override
            public void onImageAdd(ImageView img, int position, String imgID) {

                imagePickerIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                imageAddPosition = position;
                imageID = imgID;
                ((Activity) context).startActivityForResult(imagePickerIntent, SELECT_PICTURE);
            }

            @Override
            public void onSizeSpinnerSelect(String item, int itemPos, int position) {
                database.get(position).setSize(item);
                database.get(position).setSizePos(itemPos);
            }

            @Override
            public void onAddorRemove(int position) {
                recyclerData = database.get(position);
                if (recyclerData.isAddButton) {
                    if (isChildItemFull(position)) {
                        recyclerData.setAddID(R.drawable.ic_close_orange_100_24dp);
                        recyclerData.setAddText("Удалить");
                        notifyItemChanged(position);
                        recyclerData.isAddButton = false;
                        insertItem(new RecyclerData(), database.size());
                    } else
                        Toast.makeText(context, "Пожалуйста загрузите все данные!", Toast.LENGTH_SHORT).show();
                } else {
                    deleteItem(position);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RecyclerData current = database.get(position);
        if (current.styleUri != null)
            Picasso.with(context).load(Uri.parse(current.styleUri)).centerInside().resize(512, 512).into(holder.style);
        else
            Picasso.with(context).load(R.drawable.ic_action_new_picture).into(holder.style);

        if (current.frontUri != null)
            Picasso.with(context).load(Uri.parse(current.frontUri)).centerInside().resize(512, 512).into(holder.front);
        else
            Picasso.with(context).load(R.drawable.ic_action_new_picture).into(holder.front);
        if (current.backUri != null)
            Picasso.with(context).load(Uri.parse(current.backUri)).centerInside().resize(512, 512).into(holder.back);
        else
            Picasso.with(context).load(R.drawable.ic_action_new_picture).into(holder.back);
        if (current.sideUri != null)
            Picasso.with(context).load(Uri.parse(current.sideUri)).centerInside().resize(512, 512).into(holder.side);
        else
            Picasso.with(context).load(R.drawable.ic_action_new_picture).into(holder.side);
        Picasso.with(context).load(current.addID).into(holder.add);
        holder.addTxt.setText(current.addText);
        holder.size_spin.setAdapter(spinAdapter);
        holder.size_spin.setSelection(current.sizePos);
    }

    @Override
    public int getItemCount() {
        return database.size();
    }


    public boolean isChildItemFull(int position) {
        int counter = 0;
        recyclerData = database.get(position);
        String[] images = {recyclerData.styleUri, recyclerData.frontUri, recyclerData.backUri, recyclerData.sideUri};
        for (String item : images) {
            if (item != null) counter++;
        }
        return counter == 4;
    }

    public void deleteItem(final int position) {
        final RecyclerData tmpData = database.get(position);
        database.remove(position);
        notifyItemRemoved(position);

        Snackbar snackbar = Snackbar
                .make(layout, "Товар удален", Snackbar.LENGTH_LONG)
                .setAction("Отменить", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        database.add(position, tmpData);
                        notifyItemInserted(position);
                    }
                });

        snackbar.show();
    }

    public void insertItem(RecyclerData rdata, int position) {
        rdata.setCollar("collar" + Integer.toString(collarTag + 1));
        rdata.setType(database.get(position - 1).type);
        rdata.setTag(database.get(position - 1).tag);
        rdata.setGender(database.get(position - 1).gender);
        rdata.setSize("XS");
        rdata.setSizePos(0);
        database.add(position, rdata);
        notifyItemInserted(position);
    }

    public List<RecyclerData> getDatabase() {
        return database;
    }

    public void setDatabase(List<RecyclerData> data) {
        database = data;
        notifyDataSetChanged();
    }

    public void clearDatabase() {
        database.clear();
    }

    public int getCollarTag() {
        return collarTag;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageURI = intent.getData();
                String path;
                path = selectedImageURI.toString();
                database.get(imageAddPosition).setImageUri(imageID, path);
                notifyItemChanged(imageAddPosition);
            }
        }
    }

    public String getPath(Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

    public interface addListener {
        void onAddListener();
    }
}