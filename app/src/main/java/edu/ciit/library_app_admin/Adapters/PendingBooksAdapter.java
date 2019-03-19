package edu.ciit.library_app_admin.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import edu.ciit.library_app_admin.Models.PendingBooks;
import edu.ciit.library_app_admin.R;

public class PendingBooksAdapter extends FirestoreRecyclerAdapter<PendingBooks, PendingBooksAdapter.PendingBooksHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private Context mContext;

    public PendingBooksAdapter(@NonNull FirestoreRecyclerOptions<PendingBooks> options, Context context) {
        super(options);
        this.mContext = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull PendingBooksHolder holder, int position, @NonNull PendingBooks model) {
        holder.StudentName.setText(model.getName());
        holder.StudentEmail.setText(model.getEmail());
        holder.StudentSection.setText(model.getSection());
        holder.BookTitle.setText(model.getTitle());
        holder.BookGenre.setText(model.getGenre());
    }

    @NonNull
    @Override
    public PendingBooksHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items_requested_books, viewGroup, false);
        v.findViewById(R.id.imageViewCheckButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Borrow Accept", Toast.LENGTH_SHORT).show();
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
