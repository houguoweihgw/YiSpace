package com.emdoor.yispace.ui.fragment;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emdoor.yispace.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AboutFragment extends Fragment {
    private Toolbar toolbar;
    private FloatingActionButton upload_fab;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("关于");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        toolbar.setTitle("关于");
        upload_fab = getActivity().findViewById(R.id.upload_button);
        upload_fab.setVisibility(View.VISIBLE);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false);
    }
}