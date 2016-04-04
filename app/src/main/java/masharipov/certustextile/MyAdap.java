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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.util.ArrayList;


public class MyAdap extends coverflow.CoverFlowAdapter {

    // =============================================================================
    // Private members
    // =============================================================================

    private ArrayList<Integer> mData;
    private Context mContext;
    private int size;
    ArrayList<Bitmap> A1;
    boolean dataChanged;

    public MyAdap(Context ctx, ArrayList<Integer> list) {
        this.mData = list;
        this.mContext = ctx;
        A1=new ArrayList<>();
       A1.add(BitmapFactory.decodeResource(mContext.getResources(),
                mData.get(0)));
        A1.add(BitmapFactory.decodeResource(mContext.getResources(),
                mData.get(1)));
        A1.add(BitmapFactory.decodeResource(mContext.getResources(),
                mData.get(2)));


    }

    // =============================================================================
    // Supertype overrides
    // =============================================================================

    public int getCount() {
        return mData.size();
    }

    @Override
    public Bitmap getImage(int position) {
        return A1.get(position);
    }


    public void changeBitmap() {
        dataChanged = true;

        notifyDataSetChanged();
    }

}
