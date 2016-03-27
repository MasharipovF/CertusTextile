/*
 * Copyright 2013 David Schreiber
 *           2013 John Paul Nalog
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package masharipov.certustextile;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.util.ArrayList;
import coverflow.FancyCoverFlowAdapter;


public class CoverFlowAdapter extends FancyCoverFlowAdapter {

    // =============================================================================
    // Private members
    // =============================================================================

    private ArrayList<Integer> mData;
    private Context mContext;
    private int size;

    public CoverFlowAdapter(Context ctx, ArrayList<Integer> list, int param) {
        this.mData = list;
        this.mContext = ctx;
        size = convertDpToPixel(param, ctx);
    }

    public CoverFlowAdapter(Context ctx, ArrayList<Integer> list) {
        this.mData = list;
        this.mContext = ctx;
    }

    // =============================================================================
    // Supertype overrides
    // =============================================================================

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Integer getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getCoverFlowItem(final int i, View reuseableView, ViewGroup viewGroup) {

        ViewHolder holder;
        View convertView = reuseableView;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.coverflow_item, viewGroup, false);
            //   dlya izmeneniya razmera image v kode convertView.setLayoutParams(new FancyCoverFlow.LayoutParams(size, size));
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.coverflowimage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        /*final ImageView imageView;
        if (reuseableView != null) {
            imageView = (ImageView) reuseableView;
        } else {
            imageView = new ImageView(viewGroup.getContext());
            imageView.setLayoutParams(new FancyCoverFlow.LayoutParams(size, size));

        }*/
        holder.imageView.setImageBitmap(null);
        holder.imageView.setImageResource(mData.get(i));
        return convertView;
    }

    static class ViewHolder {
        public ImageView imageView;
    }

    public static int convertDpToPixel(int dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }
}
