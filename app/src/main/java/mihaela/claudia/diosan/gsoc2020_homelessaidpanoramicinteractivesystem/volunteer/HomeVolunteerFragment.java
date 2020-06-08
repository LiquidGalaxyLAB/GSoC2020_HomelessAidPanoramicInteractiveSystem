package mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.volunteer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.github.clans.fab.FloatingActionButton;

import mihaela.claudia.diosan.gsoc2020_homelessaidpanoramicinteractivesystem.R;


public class HomeVolunteerFragment extends Fragment  implements View.OnClickListener{

    /*Views*/
    private View view;

    /*Floating Action menu*/
    private FloatingActionButton newHomelessProfile;
    private FloatingActionButton sendDeliveryNotification;

    /*SearchView bar*/
    private SearchView searchView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_volunteer, container, false);

        initViews();



        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        newHomelessProfile.setOnClickListener(this);
        sendDeliveryNotification.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.new_homeless_profile:
                startActivity(new Intent(getActivity(), CreateHomelessProfile.class));
                break;
            case R.id.send_delivery_notification:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DeliveryFragment())
                        .addToBackStack(null).commit();
                break;
        }
    }

    private void initViews(){
        newHomelessProfile = view.findViewById(R.id.new_homeless_profile);
        sendDeliveryNotification = view.findViewById(R.id.send_delivery_notification);

        searchView = view.findViewById(R.id.volunteer_search);
        searchView.onActionViewExpanded();
        searchView.clearFocus();
    }



}
