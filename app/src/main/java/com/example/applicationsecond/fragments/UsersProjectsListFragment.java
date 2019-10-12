package com.example.applicationsecond.fragments;


import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewSwitcher;

import com.example.applicationsecond.R;
import com.example.applicationsecond.activities.ProjectDetailActivity;
import com.example.applicationsecond.adapters.AdapterRecyclerViewProjects;
import com.example.applicationsecond.adapters.AdapterUsersProjectsList;
import com.example.applicationsecond.api.ProjectHelper;
import com.example.applicationsecond.models.Project;
import com.example.applicationsecond.utils.ItemClickSupport;
import com.example.applicationsecond.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsersProjectsListFragment extends Fragment {
    //------------------------------
    //BIND VIEWS
    //-------------------------------
    @BindView(R.id.view_switcher_fragment_users_projects_list)
    ViewSwitcher viewSwitcher;
    @BindView(R.id.recycler_view_fragment_users_projects_list)
    RecyclerView recyclerView;
    //-----------------------------------
    //-------------------------------------
    private AdapterUsersProjectsList adapter;
    private SharedPreferences preferences;
    private List<Project> projectList;
    //-----------------------------------------------
    //-------------------------------------------------
    public static final String APP_PREFERENCES = "appPreferences";
    public static final String KEY_USERS_PROJECTS_LIST = "keyUsersProjectsList";

    public UsersProjectsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_users_projects_list, container, false);
        ButterKnife.bind(this, result);

        preferences = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        doBasicConfiguration();
        return result;
    }
    private void doBasicConfiguration() {
        projectList = new ArrayList<>();

        configureRecyclerView();
        configureOnClickRecyclerView();
        configureViewSwitcher();

    }

    private void configureViewSwitcher() {
        // Declare in and out animations and load them using AnimationUtils class
        Animation newsAvailable = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), android.R.anim.slide_in_left);
        Animation noNewsAvailable = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), android.R.anim.slide_out_right);
        // set the animation type to ViewSwitcher
        viewSwitcher.setInAnimation(newsAvailable);
        viewSwitcher.setOutAnimation(noNewsAvailable);

        displayScreenDependingOfNewsAvailable();
    }

    private void configureRecyclerView() {
        String currentUsersId = Utils.getCurrentUser().getUid();
        getDataToConfigureRecyclerView(currentUsersId);
    }

    private void configureOnClickRecyclerView() {

    }

    private void displayScreenDependingOfNewsAvailable() {
        if (checkIfTheresNewsToDisplay()) {
            viewSwitcher.setDisplayedChild(0);
        } else {
            viewSwitcher.setDisplayedChild(1);
        }
    }

    private boolean checkIfTheresNewsToDisplay() {
        return true;
    }

    private void getDataToConfigureRecyclerView(String userId) {
        ProjectHelper.getUsersPublishedProjects(userId).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Project> projects = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Project project = document.toObject(Project.class);
                        projects.add(project);
                    }

                    adapter = new AdapterUsersProjectsList(projects);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(adapter);

                } else {
                    Log.e("TAG", "Error");
                }
            }
        });
    }
}
