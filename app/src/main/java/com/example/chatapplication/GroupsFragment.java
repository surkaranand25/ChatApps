package com.example.chatapplication;


import android.content.Intent;
import android.icu.text.Edits;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.telephony.mbms.MbmsErrors;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupsFragment extends Fragment {

    public   View groupfregmentview;
    private ListView listView;
    private ArrayAdapter<String>arrayAdapter;
    private ArrayList<String>listofgroup =new ArrayList<>();
    private DatabaseReference GroupRef;


    public GroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        groupfregmentview= inflater.inflate( R.layout.fragment_groups, container, false );

        GroupRef= FirebaseDatabase.getInstance().getReference().child("Groups");

        Initializefields();
        RetrieveAndDisplayGroup();
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
               String currentGroupName= parent.getItemAtPosition( position ).toString();
                Intent groupchatIntent=new Intent( getContext(),GroupChatActivity.class );
                groupchatIntent.putExtra( "GroupName",currentGroupName );
                startActivity( groupchatIntent );
            }
        } );

        return groupfregmentview;
    }



    private void Initializefields()
    {
        listView=(ListView)groupfregmentview.findViewById( R.id.list_View1 );
        arrayAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,listofgroup);
        listView.setAdapter(arrayAdapter);
    }



    private void RetrieveAndDisplayGroup()
    {
           GroupRef.addValueEventListener( new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot)
               {
                   Set<String> set=new HashSet<>( );

                   Iterator iterator = dataSnapshot.getChildren().iterator();
                   while (iterator.hasNext())
                   {
                       set.add(((DataSnapshot)iterator.next()).getKey());

                   }
                   listofgroup.clear();
                   listofgroup.addAll(set);
                   arrayAdapter.notifyDataSetChanged();

               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           } );
    }

}
