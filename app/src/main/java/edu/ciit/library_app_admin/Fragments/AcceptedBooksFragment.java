package edu.ciit.library_app_admin.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

import edu.ciit.library_app_admin.Adapters.BookAdapterTemplate;
import edu.ciit.library_app_admin.Models.Books;
import edu.ciit.library_app_admin.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AcceptedBooksFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AcceptedBooksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AcceptedBooksFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private String TAG = "FIRESTORE";

    private OnFragmentInteractionListener mListener;

    //FireBase Stuff
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Book_Borrowed = db.collection("Borrowed_Books");
    //

    RecyclerView mRecyclerView;
    BookAdapterTemplate mAdapter;

    public AcceptedBooksFragment() {
        // Required empty public constructor
    }

    public static AcceptedBooksFragment newInstance(String param1, String param2) {
        AcceptedBooksFragment fragment = new AcceptedBooksFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_accepted_books,container, false);
        mRecyclerView = v.findViewById(R.id.recyclerView_BookAccepted);

        Book_Borrowed.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen Failed", e);
                } else {
                    Log.d(TAG, "Listen Success!");
                }
            }
        });

        Query query = Book_Borrowed.orderBy("title", Query.Direction.ASCENDING);
        setUpRecyclerView(query);

        return v;
    }

    private void setUpRecyclerView(Query query)
    {
        FirestoreRecyclerOptions<Books> options = new FirestoreRecyclerOptions.Builder<Books>()
                .setQuery(query, Books.class).build();

        mAdapter = new BookAdapterTemplate(options, getContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
