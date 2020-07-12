package ducthuan.com.lamdep.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import ducthuan.com.lamdep.Adapter.ChatAdapter;
import ducthuan.com.lamdep.Model.Chat;
import ducthuan.com.lamdep.Model.User;
import ducthuan.com.lamdep.R;

public class ChatActivity extends AppCompatActivity {

    ImageView imgBack;
    CircleImageView imgShop,imgOnline,imgOffline;
    TextView txtShop;
    ImageButton btnSend;
    EditText edMessage;
    RecyclerView rvMessage;

    ChatAdapter chatAdapter;
    ArrayList<Chat>chats;

    Intent intent;
    String userid = "";

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    //khai bao de khi thoat man hinh chat se huy trang thai
    ValueEventListener seenEventMessage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        intent = getIntent();
        if(intent.hasExtra("userid")){
            userid = intent.getStringExtra("userid");

            addControls();
            seenMessage();
            addEvents();
        }
    }

    private void addEvents() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = edMessage.getText().toString().trim();
                if(!msg.equals("")){
                    sendMessage(firebaseUser.getUid(),userid,msg);
                }
                edMessage.setText("");
            }
        });

    }

    private void sendMessage(String uid, final String userid, String msg) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        Map<String,Object> map = new HashMap<>();
        map.put("sender",uid);
        map.put("receiver",userid);
        map.put("message",msg);
        map.put("seen",false);
        databaseReference.push().setValue(map);

        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist").child(firebaseUser.getUid()).child(userid);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    chatRef.child("id").setValue(userid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    private void addControls() {
        imgBack = findViewById(R.id.imgBack);
        imgShop = findViewById(R.id.imgShop);
        txtShop = findViewById(R.id.txtShop);
        btnSend = findViewById(R.id.btnSend);
        edMessage = findViewById(R.id.edMessage);
        rvMessage = findViewById(R.id.rvMessage);
        imgOnline = findViewById(R.id.imgOnline);
        imgOffline = findViewById(R.id.imgOffline);

        rvMessage.setHasFixedSize(true);


        LinearLayoutManager layoutManager = new LinearLayoutManager(ChatActivity.this);
        layoutManager.setStackFromEnd(true);

        rvMessage.setLayoutManager(layoutManager);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);


                txtShop.setText(user.getName());
                if(user.getStatus().equals("online")){
                    imgOnline.setVisibility(View.VISIBLE);
                    imgOffline.setVisibility(View.GONE);
                }else {
                    imgOnline.setVisibility(View.GONE);
                    imgOffline.setVisibility(View.VISIBLE);
                }

                readMessage(firebaseUser.getUid(),userid,"default");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    private void readMessage(final String uid, final String userid, final String image) {

        chats = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chats.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if(chat.getSender().equals(uid) && chat.getReceiver().equals(userid) ||
                            chat.getReceiver().equals(uid) && chat.getSender().equals(userid)){
                        chats.add(chat);
                    }
                }

                chatAdapter = new ChatAdapter(ChatActivity.this,chats,image);
                rvMessage.setAdapter(chatAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void seenMessage() {

        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        seenEventMessage = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid)){
                        Map<String, Object>map = new HashMap<>();
                        map.put("seen",true);
                        dataSnapshot.getRef().updateChildren(map);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



    public void status(String status){
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        Map<String,Object>map = new HashMap<>();
        map.put("status",status);
        databaseReference.updateChildren(map);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        databaseReference.removeEventListener(seenEventMessage);
        status("offline");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        status("offline");
    }
}
