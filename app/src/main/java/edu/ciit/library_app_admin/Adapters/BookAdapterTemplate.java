package edu.ciit.library_app_admin.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import edu.ciit.library_app_admin.Models.Books;
import edu.ciit.library_app_admin.R;

public class BookAdapterTemplate extends FirestoreRecyclerAdapter<Books, BookAdapterTemplate.BookAdapterHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private Context mContext;

    public BookAdapterTemplate(@NonNull FirestoreRecyclerOptions<Books> options, Context context) {
        super(options);
        this.mContext = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull BookAdapterHolder holder, int position, @NonNull Books model) {
        holder.StudentName.setText(model.getName());
        holder.StudentEmail.setText(model.getEmail());
        holder.StudentSection.setText(model.getSection());
        holder.BookTitle.setText(model.getTitle());
        holder.BookGenre.setText(model.getGenre());
    }

    @NonNull
    @Override
    public BookAdapterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_items,viewGroup,false);
        return new BookAdapterHolder(v);
    }

    class BookAdapterHolder extends RecyclerView.ViewHolder{
        TextView BookTitle;
        TextView BookGenre;
        TextView StudentName;
        TextView StudentEmail;
        TextView StudentSection;

        public BookAdapterHolder(@NonNull View itemView) {
            super(itemView);
            BookTitle = itemView.findViewById(R.id.bookTitleTemplate);
            BookGenre = itemView.findViewById(R.id.bookGenreTemplate);
            StudentName = itemView.findViewById(R.id.studentNameTemplate);
            StudentEmail = itemView.findViewById(R.id.studentEmailTemplate);
            StudentSection = itemView.findViewById(R.id.studentSectionTemplate);
        }
    }
}
