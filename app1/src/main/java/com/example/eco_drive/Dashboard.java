package com.example.eco_drive;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private NavigationView navigationView;
    private FirebaseUser currentUser;
    private static final int REQUEST_INVITE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mAuth = FirebaseAuth.getInstance();
        navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new HomeFragment());
            ft.commit();
            navigationView.setCheckedItem(R.id.HomeF);
        }

    }
    private void onInviteClicked() {
        Intent intent = new AppInviteInvitation.IntentBuilder("Invitation ")
                .setMessage(getString(R.string.invitation_message))
                .setDeepLink(Uri.parse("mailto:"))
                .setCallToActionText("Invitation")
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    System.out.println("MainActivity.onActivityResult" + id);
                }
            } else {
                //ERROR
                Toast.makeText(Dashboard.this, "fjdsbf", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

    }

    private void updateUI(FirebaseUser currentUser) {
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.HeaderEmail);
        if (currentUser != null) {
            String email = currentUser.getEmail();
            navUsername.setText(email);

        }
    }

    private void SignOut() {
        if (currentUser != null) {
            FirebaseAnalytics mFirebase;
            mFirebase = FirebaseAnalytics.getInstance(Dashboard.this);
            mFirebase.setUserId(null);
            Log.i(" ", "SignOut: "+currentUser.getUid());
            mAuth.signOut();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.HomeF) {
            fragment = new HomeFragment();
        }
        else if (id == R.id.ProfileF) {
            fragment = new ProfileFragment();
        }
        else if (id == R.id.inviteF) {

        }
        else if (id == R.id.invite_download){
            onInviteClicked();
        }
        else if (id == R.id.logoutF) {
            SignOut();
            Toast.makeText(Dashboard.this, "Logging out...", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Dashboard.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
