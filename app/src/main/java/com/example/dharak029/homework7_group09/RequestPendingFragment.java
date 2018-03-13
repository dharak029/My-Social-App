package com.example.dharak029.homework7_group09;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnRequestPendingFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RequestPendingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestPendingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    static class Friend{
        String friendName;
        String friendUID;
        String status;
    }

    private OnRequestPendingFragmentInteractionListener mListener;

    public RequestPendingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RequestPendingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RequestPendingFragment newInstance(String param1, String param2) {
        RequestPendingFragment fragment = new RequestPendingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    View view;
    @Override
    public void onPause() {
        super.onPause();
        mListener = (OnRequestPendingFragmentInteractionListener) getActivity();
        view = getView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request_pending, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = rootRef.child(mListener.getUIDPendingRequest());
        DatabaseReference requestRef = userRef.child("Requests");

        requestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Friend> arrayList = new ArrayList<Friend>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    AddNewFriendAdapter.Request rqst = dataSnapshot1.getValue(AddNewFriendAdapter.Request.class);
                    Friend friend = new Friend();
                    friend.friendName = rqst.name;
                    friend.friendUID = dataSnapshot1.getKey();
                    friend.status = rqst.status;
                    if(!dataSnapshot1.getKey().equals(mListener.getUIDPendingRequest())){
                        arrayList.add(friend);
                    }

                }
//                Log.d("seize", arrayList.size() + "");
                RecyclerView recyclerView=null;
                if(getView()!=null){
                    recyclerView  = (RecyclerView)getView().findViewById(R.id.recyclerRequestPending);
                }
                else{
                    recyclerView  = (RecyclerView)view.findViewById(R.id.recyclerRequestPending);
                }
               // RecyclerView recyclerView = (RecyclerView)getView().findViewById(R.id.recyclerRequestPending);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(mLayoutManager);
                PendingRequestAdapter pendingRequestAdapter = new PendingRequestAdapter(arrayList,mListener.getUIDPendingRequest(),mListener.getNamePendingRequest());
                recyclerView.setAdapter(pendingRequestAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mListener = (OnRequestPendingFragmentInteractionListener) context;
        }
        catch(ClassCastException e){

        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListener = (ManageFriends)getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

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
    public interface OnRequestPendingFragmentInteractionListener {
        // TODO: Update argument type and name
        //void onFragmentInteraction(Uri uri);
        String getUIDPendingRequest();
        String getNamePendingRequest();
    }
}
