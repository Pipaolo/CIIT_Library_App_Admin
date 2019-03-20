package edu.ciit.library_app_admin;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import edu.ciit.library_app_admin.Adapters.PendingBooksAdapter;
import edu.ciit.library_app_admin.Fragments.AcceptedBooksFragment;
import edu.ciit.library_app_admin.Fragments.AddBookFragment;
import edu.ciit.library_app_admin.Fragments.Denied_Books;
import edu.ciit.library_app_admin.Fragments.ReturnedBooksFragment;
import edu.ciit.library_app_admin.Models.Books;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AddBookFragment.OnFragmentInteractionListener {

    //Firebase Stuff
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Book_Shelf = db.collection("Book_Shelf");
    CollectionReference Book_Transaction_Logs = db.collection("Book_Shelf");
    CollectionReference Genres = db.collection("Book_Shelf");
    CollectionReference Book_Borrowed = db.collection("Borrowed_Books");
    CollectionReference Pending_Books = db.collection("Pending_Books");

    //Recycler View Adapter
    private RecyclerView mRecyclerView;
    private PendingBooksAdapter mAdapter;
    //

    String studentName;
    String studentEmail;
    String studentSection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddBookFragment fragment = new AddBookFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.main_menu,fragment).addToBackStack(null).commit();

            }
        });

        setUpRecyclerView();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setUpRecyclerView()
    {
        Query query = Pending_Books.orderBy("title", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Books> options = new FirestoreRecyclerOptions.Builder<Books>()
            .setQuery(query, Books.class).build();

        mAdapter = new PendingBooksAdapter(options, getApplicationContext(), Pending_Books, Book_Borrowed);

        mRecyclerView = findViewById(R.id.recyclerView_requestedBooksMenu);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_RequestedBooks) {
            for (Fragment fragment:getSupportFragmentManager().getFragments()) {
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        } else if (id == R.id.nav_AcceptedBooks) {
            AcceptedBooksFragment fragment = new AcceptedBooksFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_menu, fragment).addToBackStack(null).commit();
        } else if (id == R.id.nav_DeniedBooks) {
            Denied_Books fragment = new Denied_Books();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_menu, fragment).addToBackStack(null).commit();
        } else if (id == R.id.nav_ReturnedBooks) {
            ReturnedBooksFragment fragment = new ReturnedBooksFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_menu, fragment).addToBackStack(null).commit();
        } else if (id == R.id.nav_SignOut) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onStart()
    {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        mAdapter.stopListening();
    }

}
