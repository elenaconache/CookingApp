package com.example.elena.shopeasy.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.elena.shopeasy.R;
import com.example.elena.shopeasy.adapters.CategoriesAdapter;
import com.example.elena.shopeasy.customViews.AnimatedExpandableListView;
import com.example.elena.shopeasy.model.Ingredient;
import com.example.elena.shopeasy.ui.MainActivity;
import com.example.elena.shopeasy.utils.PrefUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * interface
 * to handle interaction events.
 * Use the {@link CategoriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoriesFragment extends Fragment {
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

//    private String mParam1;
//    private String mParam2;
//
//    private OnFragmentInteractionListener mListener;

    private static ExpandableListAdapter listAdapter;
    private static AnimatedExpandableListView expListView;
    private static List<String> listDataHeader = new ArrayList<>();
    private static HashMap<String, Set<String>> listDataChild = new HashMap<>();

    private static TextView mEmptyView;

    public CategoriesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoriesFragment.
     */
    public static CategoriesFragment newInstance(String param1, String param2) {
        CategoriesFragment fragment = new CategoriesFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_categories, container,
                false);

        expListView = rootView.findViewById(R.id.expand_list_categories);
        mEmptyView = rootView.findViewById(R.id.text_empty_view_categories);

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group
                // expansion/collapse.
                if (expListView.isGroupExpanded(groupPosition)) {
                    expListView.collapseGroupWithAnimation(groupPosition);
                } else {
                    expListView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }

        });

//        PrefUtils prefUtils = new PrefUtils(getActivity());
//        Drawable normalDrawable = getResources().getDrawable(R.drawable.group_indicator);
//        Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);
//        DrawableCompat.setTint(wrapDrawable, prefUtils.getFontColor());
//        if (prefUtils.getFontColor()!=0){
//
//            expListView.setGroupIndicator(wrapDrawable);
//            expListView.setIndicatorBounds(0,0);
//           // expListView.
//           // expListView.setGroupIndicator(getResources().getDrawable(R.drawable.group_indicator).setTint(prefUtils.getFontColor()););
//        }else {
//            expListView.setGroupIndicator(normalDrawable);
//
//        }

   //     expListView.setGroupIndicator(
     //           getResources().getDrawable(R.drawable.ic_expand_more_black_24dp));
      //  expListView.setIndicatorBounds(5,10);
        return rootView;
    }

//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(Uri uri);
//    }

    public static void setupData(Context context){
        listDataHeader.clear();
        listDataHeader.add("Ingredients");
        listDataChild.clear();
        Set<String> ingredients = new HashSet<>();
        if (Ingredient.getmUsedIngredients()!=null){
            ingredients.addAll(Ingredient.getmUsedIngredients());
        }

        if (MainActivity.getIngredients()!=null){
            ingredients.addAll(MainActivity.getIngredients());
        }
        listDataChild.put("Ingredients",ingredients);

        listAdapter = new CategoriesAdapter(context, listDataHeader, listDataChild,
                ((Activity)context));

        // setting list adapter
        if (expListView!=null) {
            expListView.setAdapter(listAdapter);
            if (listDataChild.get("Ingredients").isEmpty()){
                mEmptyView.setVisibility(View.VISIBLE);
                expListView.setVisibility(View.GONE);
            }else{
                mEmptyView.setVisibility(View.GONE);
                expListView.setVisibility(View.VISIBLE);
            }
        }


    }
}
