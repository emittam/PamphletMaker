package net.emittam.pamphletmaker.utils;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import net.emittam.pamphletmaker.R;

/**
 * Created by kato-h on 16/11/26.
 */

public class BindingUtils {
    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String url) {
        Picasso.with(view.getContext()).load(url).placeholder(R.drawable.noimage).into(view);
    }
}
