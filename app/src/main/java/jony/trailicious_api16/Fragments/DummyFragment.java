package jony.trailicious_api16.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import jony.trailicious_api16.R;

public class DummyFragment extends Fragment {
    //public static final String ARG_MENU_INDEX = "index";

    public DummyFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dummy_fragment, container, false);
        //int index = getArguments().getInt(ARG_MENU_INDEX);

        //String text = String.format("Trailicious. Index %s", index);
        String text = "Dummy Fragment";

        ((TextView) rootView.findViewById(R.id.textView)).setText(text);
        getActivity().setTitle("Trailicious");
        return rootView;
    }
}
