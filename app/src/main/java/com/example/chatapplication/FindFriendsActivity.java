package com.example.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class FindFriendsActivity extends AppCompatActivity {
//    private Toolbar mtoolbar;
//    private RecyclerView FindFriendRecyclerList;
//    private DatabaseReference UsersRef;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate( savedInstanceState );
//        setContentView( R.layout.activity_find_friends );
//
//
//        FindFriendRecyclerList = findViewById( R.id.find_friends_recycler_list );
//        FindFriendRecyclerList.setLayoutManager( new LinearLayoutManager( this ) );
//
//
//        mtoolbar = (Toolbar) findViewById( R.id.find_friends_toolbar );
//        setSupportActionBar( mtoolbar );
//        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
//        getSupportActionBar().setDisplayShowHomeEnabled( true );
//        getSupportActionBar().setTitle( "Find Friend" );
//
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        FirebaseRecyclerOptions<Contact>options=new FirebaseRecyclerOptions.Builder<Contact>()
//                .setQuery( UsersRef,Contact.class )
//                .build();
//
//        FirebaseRecyclerAdapter<Contact, FindFriendViewHolder> adapter =
//                new FirebaseRecyclerAdapter<Contact, FindFriendViewHolder>(options) {
//                    @Override
//                    protected void onBindViewHolder(@NonNull FindFriendViewHolder holder, int position, @NonNull Contact model)
//                    {
//                        holder.userName.setText( model.getName() );
//                        holder.userStatus.setText( model.getName() );
//                        Picasso.get().load(model.getImage()).into( holder.profileImage );
//
//                    }
//
//                    @NonNull
//                    @Override
//                    public FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//                        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.user_display_layout, parent, false );
//                   FindFriendViewHolder viewHolder=new FindFriendViewHolder( view );
//                   return viewHolder;
//                    }
//                };
//
//        FindFriendRecyclerList.setAdapter( adapter );
//        adapter.startListening();
//
//
//    }
//
//    public static class FindFriendViewHolder extends RecyclerView.ViewHolder
//    {
//
//        TextView userName,userStatus;
//        CircleImageView profileImage;
//
//        public FindFriendViewHolder(@NonNull View itemView) {
//            super( itemView );
//
//            userName=itemView.findViewById( R.id.user_profile_name );
//            userStatus=itemView.findViewById( R.id.user_Status );
//            profileImage=itemView.findViewById( R.id.user_profile_image );
//
//        }
//    }
}
