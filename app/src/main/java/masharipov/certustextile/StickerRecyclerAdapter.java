package masharipov.certustextile;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import masharipov.certustextile.edit.RecyclerData;
import masharipov.certustextile.stickeradd.StickerData;

/**
 * Created by Farrukh on 14.04.2016.
 */
public class StickerRecyclerAdapter extends RecyclerView.Adapter<SecondViewHolder> implements View.OnClickListener {

    private Context context;
    private List<StickerData> stickerlist;
    private int height, tmpheight = -1;
    private clickListener listener;
    private RecyclerView.LayoutParams layoutParams;
    private ImageView imageView;


    public StickerRecyclerAdapter(Context c, List<StickerData> list, int h, clickListener l) {
        context = c;
        stickerlist = list;
        height = h;
        listener = l;
    }

    @Override
    public SecondViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        imageView = new ImageView(context);
        imageView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, height / 3));
        SecondViewHolder holder = new SecondViewHolder(imageView, context, new SecondViewHolder.onClick() {
            @Override
            public void onClick(ImageView img, int position) {
                listener.onItemClick(img, position, stickerlist.get(position).getURI());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(SecondViewHolder holder, int position) {
        layoutParams = (RecyclerView.LayoutParams) holder.imageView.getLayoutParams();
        layoutParams.height = height / 3;

        StickerData current = stickerlist.get(position);
        if (current.getURI() != null) {
            File getBit = new File(getPath(Uri.parse(current.getURI())));
            Bitmap bitTovar = BitmapFactory.decodeFile(getBit.getAbsolutePath());
            holder.imageView.setImageBitmap(bitTovar);
        }
        //Picasso.with(context).load(Uri.parse(current.getURI())).centerInside().resize(512, 512).into(holder.imageView);
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
        void onItemClick(ImageView img, int position, String strUri);
    }

    public void setStickerlist(List<StickerData> list) {
        stickerlist = list;
        notifyDataSetChanged();
    }

    public void setImageParams(int h) {
        height = h;
        notifyDataSetChanged();
    }

    public String getPath(Uri uri) {
        // just some safety built in
        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = ((AppCompatActivity) context).managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }
}
