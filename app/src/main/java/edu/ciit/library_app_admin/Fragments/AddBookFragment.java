package edu.ciit.library_app_admin.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Random;

import edu.ciit.library_app_admin.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddBookFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddBookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddBookFragment extends Fragment{

    private OnFragmentInteractionListener mListener;

    private String TAG = "FIRESTORE";
    private EditText bookTitle;
    private EditText bookGenre;
    private EditText bookDescription;
    private Button addBook;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //FireBase Databases
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Book_Shelf = db.collection("Book_Shelf");
    CollectionReference Genres = db.collection("Genres");
    //

    public AddBookFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddBookFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddBookFragment newInstance(String param1, String param2) {
        AddBookFragment fragment = new AddBookFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_item, container, false);
        bookTitle = v.findViewById(R.id.editTextBookTitle);
        bookGenre = v.findViewById(R.id.editTextBookGenre);
        bookDescription = v.findViewById(R.id.editTextBookDescription);
        Button button = v.findViewById(R.id.btnAddBook);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                final int bookId = random.nextInt(100);
                final String Genre = bookGenre.getText().toString();


                Genres.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            boolean isDuplicate = false;
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult())
                            {
                                if(documentSnapshot.getString("genre").equals(Genre))
                                {
                                    Log.w(TAG, "DuplicateFound");
                                    HashMap<String, Object> Book = new HashMap<>();
                                    Book.put("title", bookTitle.getText().toString());
                                    Book.put("genre", bookGenre.getText().toString());
                                    Book.put("description", bookDescription.getText().toString());
                                    Book.put("isBorrowed", false);
                                    Book.put("id", bookId);

                                    Book_Shelf.add(Book).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getContext(), "Book Added", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "FILE SAVED");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e(TAG, "FILE NOT SAVED", e.getCause());
                                        }
                                    });
                                    isDuplicate = true;
                                    break;
                                }
                            }
                            if(!isDuplicate)
                            {
                                Log.d(TAG, "No Duplicate Found");
                                HashMap<String, Object> Genre = new HashMap<>();
                                Genre.put("genre", bookGenre.getText().toString());

                                HashMap<String, Object> Book = new HashMap<>();
                                Book.put("title", bookTitle.getText().toString());
                                Book.put("genre", bookGenre.getText().toString());
                                Book.put("description", bookDescription.getText().toString());
                                Book.put("isBorrowed", false);
                                Book.put("id", bookId);

                                Genres.add(Genre).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d(TAG, "FILE SAVED");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e(TAG, "FILE NOT SAVED", e.getCause());
                                    }
                                });

                                Book_Shelf.add(Book).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(getContext(), "Book Added", Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "FILE SAVED");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e(TAG, "FILE NOT SAVED", e.getCause());
                                    }
                                });
                            }
                        }
                        else
                        {
                            Log.e(TAG, "Error Accessing Documents", task.getException());
                        }
                    }
                });
            }
        });
        return v;
    }


    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

}
