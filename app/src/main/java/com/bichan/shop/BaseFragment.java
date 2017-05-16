package com.bichan.shop;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.bichan.shop.deps.DaggerDeps;
import com.bichan.shop.deps.Deps;
import com.bichan.shop.networking.NetworkModule;

import java.io.File;

/**
 * Created by cuong on 5/16/2017.
 */

public class BaseFragment extends Fragment {
    Deps deps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File cacheFile = new File(getActivity().getCacheDir(), "responses");
        deps = DaggerDeps.builder().networkModule(new NetworkModule(cacheFile)).build();
    }

    public Deps getDeps() {
        return deps;
    }
}
