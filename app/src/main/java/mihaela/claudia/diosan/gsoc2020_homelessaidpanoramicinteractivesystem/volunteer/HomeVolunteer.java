package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.volunteer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.common.ConfigurationFragment;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.common.ContactFragment;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.common.DonateFragment;
import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.login.LoginActivity;

public class HomeVolunteer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    /*Navigation Elements*/
    private DrawerLayout volunteerDrawer;
    NavigationView navigationView;
    Toolbar mToolbar;
    View header;

    /*TextViews*/
    TextView volunteerUsername;
    TextView volunteerEmail;
    TextView volunteerPhone;
    TextView volunteerFirstName;
    TextView volunteerLastName;

  /*  *//*Firebase*//*
    FirebaseUser user;
    FirebaseFirestore mFirestore;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_volunteer);

        initViews();
      /*  initFirebase();*/
        setNavigationElements();
       /* setUserData();*/

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeVolunteerFragment()).commit();
            navigationView.setCheckedItem(R.id.user_menu_home);
        }
    }


    private void initViews() {
        mToolbar = findViewById(R.id.volunteer_toolbar);
        volunteerDrawer = findViewById(R.id.volunteer_drawer);
        navigationView = findViewById(R.id.nav_view_volunteer);

    }

    private void setNavigationElements() {
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(this);
        header = navigationView.getHeaderView(0);

        volunteerUsername = header.findViewById(R.id.user_username_text_view);
        volunteerEmail = header.findViewById(R.id.user_email_text_view);
        volunteerPhone = header.findViewById(R.id.user_phone_text_view);
        volunteerFirstName = header.findViewById(R.id.user_first_name);
        volunteerLastName = header.findViewById(R.id.user_last_name);


        ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(this, volunteerDrawer, R.string.open_navigation_drawer, R.string.close_navigation_drawer);
        volunteerDrawer.addDrawerListener(mToggle);
        mToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            volunteerDrawer.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.user_menu_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeVolunteerFragment()).commit();
                break;
            case R.id.user_menu_donate:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DonateFragment()).commit();
                break;
            case R.id.user_menu_configuration:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConfigurationFragment()).commit();
                break;
            case R.id.user_menu_logout:
              /*  FirebaseAuth.getInstance().signOut();*/
                finish();
                startActivity( new Intent(HomeVolunteer.this, LoginActivity.class));
                break;
            case R.id.user_menu_contact:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ContactFragment()).commit();
                break;
        }
        volunteerDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        volunteerDrawer = findViewById(R.id.volunteer_drawer);

        if (volunteerDrawer.isDrawerOpen(GravityCompat.START)) {
            volunteerDrawer.closeDrawer(GravityCompat.START);
        } else {
            startActivity(new Intent(HomeVolunteer.this, HomeVolunteer.class));

        }
    }

}
