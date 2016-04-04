package masharipov.certustextile;

import android.content.Context;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Random;

/**
 * Created by developer on 01.04.2016.
 */
public class ItemFragment extends Fragment{
    ImageView tovar;
    Context This;
    //Buni kiyinchali bazadan rasmlani PATH ni oganda iwaltamiz
    //  hozircha bu bizga keremas
    String upload_image_tovar;
    //Buni kiyinchali bazadan rasmlani PATH ni oganda iwaltamiz
    //  hozircha bu bizga keremas
    String upload_image_sticker;
    int t;
    FrameLayout frameSt;
    int voqtinchali_resurs;
    public ItemFragment(){

    }
    public  ItemFragment(int res){
        voqtinchali_resurs=res;
       /* Toast.makeText(getActivity(),"Fragment create",Toast.LENGTH_SHORT).show();*/
        Random a=new Random();
        t=a.nextInt(100);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View fragment_item=inflater.inflate(R.layout.fragment_itemfragmentr,container,false);
        This=getActivity();
        Toast.makeText(This,Integer.toString(t),Toast.LENGTH_SHORT).show();
        tovar =(ImageView) fragment_item.findViewById(R.id.tovar);
        frameSt =(FrameLayout) fragment_item.findViewById(R.id.frameStick);
        Picasso.with(This).load(voqtinchali_resurs).into(tovar);
        return fragment_item;
    }

    //Bu funksiya boshqa bir tovar tanlanganda o`ziga yengi tovari PATH ni qabul qilib
    //      tovari korinishini o`zgartiradi

    public boolean changeTovar(String PathT){
        upload_image_tovar=PathT;
        Picasso.with(This).load(upload_image_tovar).into(tovar);
        return true;
    }

    public boolean changeSticker(String PathS){
        upload_image_sticker=PathS;

        /*
        *
        *
        * */

        // return true esli sobitiya proizowlo false esli vozniklo owibka
        return true;
    }

}
