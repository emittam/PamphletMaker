package net.emittam.pamphletmaker;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.emittam.pamphletmaker.databinding.FragmentLeftPamphletViewerBinding;

/**
 * Created by kato-h on 16/11/26.
 */

public class LeftPamphletViewerFragment extends Fragment {

    private LeftPamphletViewerModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentLeftPamphletViewerBinding binding = FragmentLeftPamphletViewerBinding.inflate(inflater, container, false);
        binding.setLeftImageViewModel(mViewModel);
        return binding.getRoot();
    }

}
