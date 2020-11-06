package com.learning.ppjoke.ui.sofa;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.learning.libnavannotation.FragmentDestination;
import com.learning.ppjoke.R;

@FragmentDestination(pageUrl = "main/tabs/sofa")
public class SofaFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("SofaFragment","进入OnCreateView");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sofa, container, false);
    }
}