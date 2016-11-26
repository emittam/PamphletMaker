package net.emittam.pamphletmaker;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

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
        notifyChange();
    }

    @Bindable
    public String getSmallImageUrl() {
        return mSmallImageUrl;
    }

    public void setSmallImageUrl(String mSmallImageUrl) {
        this.mSmallImageUrl = mSmallImageUrl;
        notifyChange();
    }

    @Bindable
    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
        notifyChange();
    }
}
