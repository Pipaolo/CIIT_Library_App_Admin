package edu.ciit.library_app_admin.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

import edu.ciit.library_app_admin.Models.Books;
import edu.ciit.library_app_admin.R;

public class PendingBooksAdapter extends FirestoreRecyclerAdapter<Books, PendingBooksAdapter.PendingBooksHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private Context mContext;
    private CollectionReference Borrowed_Book;
    private CollectionReference Pending_Books;

    public PendingBooksAdapter(@NonNull FirestoreRecyclerOptions<Books> options,
                               Context context,
                               CollectionReference Pending_Books,
                               CollectionReference Borrowed_Books) {
        super(options);
        this.mContext = context;
        this.Borrowed_Book = Borrowed_Books;
        this.Pending_Books = Pending_Books;
    }

    @Override
    protected void onBindViewHolder(@NonNull PendingBooksHolder holder, int position, @NonNull Books model) {
        holder.StudentName.setText(model.getName());
        holder.StudentEmail.setText(model.getEmail());
        holder.StudentSection.setText(model.getSection());
        holder.BookTitle.setText(model.getTitle());
        holder.BookGenre.setText(model.getGenre());
    }

    @NonNull
    @Override
    public PendingBooksHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {

        final View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items_requested_books, viewGroup, false);

        v.findViewById(R.id.imageViewCheckButton).setOnClickListener(new View.OnClickListener() {
            TextView studentName = v.findViewById(R.id.studentName);

            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Borrow Accept", Toast.LENGTH_SHORT).show();

        Pending_Books.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(final QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots)
                {
                    final Books books = queryDocumentSnapshot.toObject(Books.class);

                    if(books.getName().equals(studentName.getText().toString()))
                    {
                        Log.d("FIRESTORE", "Book Found");
                        HashMap<String,Object> Data = new HashMap<>();
                        Data.put("title", books.getTitle());
                        Data.put("genre", books.getGenre());
                        Data.put("description", books.getDescription());
                        Data.put("id", books.getId());
                        Data.put("name", books.getName());
                        Data.put("email", books.getEmail());
                        Data.put("section",books.getSection());
                        Data.put("isBorrowed", true);

                        Borrowed_Book.add(Data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Pending_Books.document(queryDocumentSnapshot.getId()).delete();
                                Toast.makeText(mContext, books.getTitle() + " Accepted", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(mContext, books.getTitle() + "Error Saving", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    Log.e("FIRESTORE", "Book Not Found");
                }
            }
            });
            }
        });

        v.findViewById(R.id.imageViewDenyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Borrow Denied", Toast.LENGTH_SHORT).show();
            }
        });

        return new PendingBooksHolder(v);
    }

    class PendingBooksHolder extends RecyclerView.ViewHolder {

        ImageView BookPicture;
        TextView BookTitle;
        TextView BookGenre;
        TextView StudentName;
        TextView StudentEmail;
        TextView StudentSection;


        public PendingBooksHolder(@NonNull View itemView) {
            super(itemView);
            BookTitle = itemView.findViewById(R.id.bookTitle);
            BookGenre = itemView.findViewById(R.id.bookGenre);
            StudentName = itemView.findViewById(R.id.studentName);
            StudentEmail = itemView.findViewById(R.id.studentEmail);
            StudentSection = itemView.findViewById(R.id.studentSection);
        }
    }
}
