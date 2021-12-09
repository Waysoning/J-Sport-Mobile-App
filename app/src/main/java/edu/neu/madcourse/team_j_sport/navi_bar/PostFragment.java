package edu.neu.madcourse.team_j_sport.navi_bar;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import edu.neu.madcourse.team_j_sport.EventList.EventAdapter;
import edu.neu.madcourse.team_j_sport.EventList.ItemEvent;
import edu.neu.madcourse.team_j_sport.PostList.ItemPost;
import edu.neu.madcourse.team_j_sport.PostList.PostAdapter;
import edu.neu.madcourse.team_j_sport.R;

public class PostFragment extends Fragment {

    private final ArrayList<ItemPost> itemPosts = new ArrayList<>();

    View view;
    SharedPreferences sharedPreferences;

    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_post, container, false);
        sharedPreferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);
        initPostList();
        return view;
    }

    private void initPostList() {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef;

        myRef = database.getReference().child("Posts");

        myRef
                .orderByKey()
                .addChildEventListener(
                        new ChildEventListener() {
                            @Override
                            public void onChildAdded(
                                    @NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                HashMap hashMap = (HashMap) snapshot.getValue();
                                assert hashMap != null;
                                itemPosts.add(
                                        new ItemPost(
                                                "",
                                                Objects.requireNonNull(hashMap.get("title")).toString(),
                                                Objects.requireNonNull(hashMap.get("content")).toString(),
                                                snapshot.getKey()));
                                createRecyclerView();
                            }

                            @Override
                            public void onChildChanged(
                                    @NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

                            @Override
                            public void onChildMoved(
                                    @NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {}
                        });
    }

    private void createRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView recyclerView = view.findViewById(R.id.rv_post_list);
        recyclerView.setHasFixedSize(true);

        PostAdapter postAdapter = new PostAdapter(itemPosts, getActivity().getApplicationContext());

        recyclerView.setAdapter(postAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }
}