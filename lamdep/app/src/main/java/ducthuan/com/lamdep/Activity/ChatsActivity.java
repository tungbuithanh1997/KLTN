package ducthuan.com.lamdep.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ducthuan.com.lamdep.Adapter.UserAdapter;
import ducthuan.com.lamdep.Model.Chatlist;
import ducthuan.com.lamdep.Model.User;
import ducthuan.com.lamdep.R;

public class ChatsActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView rvUser;
    UserAdapter userAdapter;
    ArrayList<User> users;
    ArrayList<Chatlist> chatlists;

    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        addControls();
        addEvents();

    }

    private void addEvents() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_white_24dp);

        rvUser = findViewById(R.id.rvUser);

        users = new ArrayList<>();
        rvUser.setHasFixedSize(true);
        rvUser.setLayoutManager(new LinearLayoutManager(ChatsActivity.this));

        chatlists = new ArrayList<>();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chatlist").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatlists.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chatlist chatlist = dataSnapshot.getValue(Chatlist.class);
                    chatlists.add(chatlist);
                }

                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void chatList() {

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    User user = dataSnapshot.getValue(User.class);
                    for(Chatlist chatlist:chatlists){
                        if(chatlist.getId().equals(user.getId())){
                            users.add(user);
                        }
                    }
                }
                userAdapter = new UserAdapter(ChatsActivity.this,users,true,firebaseUser);
                rvUser.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
