package net.emittam.pamphletmaker;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

/**
 * Created by kato-h on 16/11/26.
 */

public class LeftPamphletViewerModel extends BaseObservable{

    private String mLargeImageUrl;

    private String mSmallImageUrl;

    private String mText;


    @Bindable
    public String getLargeImageUrl() {
        return mLargeImageUrl;
    }

    public void setLargeImageUrl(String mLargeImageUrl) {
        this.mLargeImageUrl = mLargeImageUrl;
        notifyPropertyChanged(BR.largeImageUrl);
    }

    @Bindable
    public String getSmallImageUrl() {
        return mSmallImageUrl;
    }

    public void setSmallImageUrl(String mSmallImageUrl) {
        this.mSmallImageUrl = mSmallImageUrl;
        notifyPropertyChanged(BR.smallImageUrl);
    }

    @Bindable
    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
        notifyPropertyChanged(BR.text);
    }


    public boolean add(@NonNull String url) {
        if (mLargeImageUrl == null) {
            mLargeImageUrl = url;
            notifyPropertyChanged(BR.largeImageUrl);
            return true;
        } else if (mSmallImageUrl == null) {
            mSmallImageUrl = url;
            notifyPropertyChanged(BR.smallImageUrl);
            return true;
        }
        return false;
    }
}
