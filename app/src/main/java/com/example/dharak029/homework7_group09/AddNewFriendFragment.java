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
 * {@link OnAddNewFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddNewFriendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNewFriendFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnAddNewFragmentInteractionListener mListener;

    View view;

    public AddNewFriendFragment() {
        // Required empty public constructor
    }


    @Override
    public void onPause() {
        super.onPause();
        mListener = (OnAddNewFragmentInteractionListener)getActivity();
        view = getView();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddNewFriendFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddNewFriendFragment newInstance(String param1, String param2) {
        AddNewFriendFragment fragment = new AddNewFriendFragment();
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


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListener = (OnAddNewFragmentInteractionListener) getContext();
    }

    @Override
    public void onStart() {
        super.onStart();
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference userRef = rootRef.child(mListener.getAddNewFriendUID());
        final DatabaseReference friendsRef = userRef.child("Friends");
        final DatabaseReference requestRef = userRef.child("Requests");

        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<FriendsFragment.Friend> arrayList = new ArrayList<FriendsFragment.Friend>();
                for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    User user = dataSnapshot1.getValue(User.class);
                    final FriendsFragment.Friend friend = new FriendsFragment.Friend();
                    friend.friendName = user.getFname()+" "+user.getLname();
                    friend.frindUID = dataSnapshot1.getKey();
                    Log.d("demo",mListener.toString());

                    if(!dataSnapshot1.getKey().equals(mListener.getAddNewFriendUID())){
                        arrayList.add(friend);
                    }
                }

                friendsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (final DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                            for(FriendsFragment.Friend friend : arrayList){
                                if(dataSnapshot2.getKey().equals(friend.frindUID)){
                                    arrayList.remove(friend);
                                }
                            }
                        }

                        requestRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (final DataSnapshot dataSnapshot3 : dataSnapshot.getChildren()) {
                                    for(FriendsFragment.Friend friend : arrayList){
                                        if(dataSnapshot3.getKey().equals(friend.frindUID)){
                                            arrayList.remove(friend);
                                        }
                                    }
                                }

                                RecyclerView recyclerView=null;
                                if(getView()!=null){
                                    recyclerView  = (RecyclerView)getView().findViewById(R.id.recyclerAddNewFriends);
                                }
                                else{
                                    recyclerView  = (RecyclerView)view.findViewById(R.id.recyclerAddNewFriends);
                                }

                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                recyclerView.setLayoutManager(mLayoutManager);
                                AddNewFriendAdapter addfriendsAdapter = new AddNewFriendAdapter(arrayList,mListener.getAddNewFriendUID(),mListener.getName());
                                recyclerView.setAdapter(addfriendsAdapter);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                //Log.d("uidList2",mListener.getFriends().size()+"");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
           // mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mListener = (OnAddNewFragmentInteractionListener) context;
        }
        catch(ClassCastException e){

        }
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
    public interface OnAddNewFragmentInteractionListener {
        // TODO: Update argument type and name
        //void onFragmentInteraction(Uri uri);
        String getAddNewFriendUID();
        String getName();
    }
}
