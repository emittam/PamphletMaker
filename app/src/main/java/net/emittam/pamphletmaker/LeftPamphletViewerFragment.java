package net.emittam.pamphletmaker;

import android.support.v4.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.emittam.pamphletmaker.databinding.FragmentLeftPamphletViewerBinding;
import net.emittam.pamphletmaker.databinding.FragmentRightPamphletViewerBinding;

/**
 * Created by kato-h on 16/11/26.
 */

public class LeftPamphletViewerFragment extends Fragment {

    public static final int PAGE_VIEW_IMAGE_COUNT = 2;

    private LeftPamphletViewerModel mViewModel;

    private int page;

    private static LeftPamphletViewerFragment mLeftFragment;
    private static LeftPamphletViewerFragment mRightFragment;

    public static LeftPamphletViewerFragment createInstance(int page) {
        if (mLeftFragment == null) {
            mLeftFragment = new LeftPamphletViewerFragment();
        }
        if (mRightFragment == null) {
            mRightFragment =  new LeftPamphletViewerFragment();
        }
        if (page % 2 == 0) {
            mLeftFragment.page = page;
            return mLeftFragment;
        } else {
            mRightFragment.page = page;
            return mRightFragment;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (page % 2 == 0) {
            FragmentLeftPamphletViewerBinding binding = FragmentLeftPamphletViewerBinding.inflate(inflater, container, false);
            binding.setLeftImageViewModel(mViewModel);
            return binding.getRoot();

        } else {
            FragmentRightPamphletViewerBinding binding = FragmentRightPamphletViewerBinding.inflate(inflater, container, false);
            binding.setLeftImageViewModel(mViewModel);
            return binding.getRoot();
        }
    }

    public void setViewModel(LeftPamphletViewerModel model) {
        mViewModel = model;
    }

}
