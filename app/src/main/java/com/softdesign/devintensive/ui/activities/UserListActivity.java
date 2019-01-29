package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.UsersAdapter;
import com.softdesign.devintensive.utils.ConstantManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListActivity extends AppCompatActivity {

	private CoordinatorLayout mCoordinatorLayout;
	private Toolbar mToolbar;
	private DrawerLayout mNavigationDrawer;
	private RecyclerView mRecyclerView;

	private DataManager mDataManager;
	private UsersAdapter mUsersAdapter;
	private List<UserListRes.UserData> mUsers;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_list);

		mDataManager = DataManager.getInstance();
		mCoordinatorLayout = findViewById(R.id.main_coordinator_container);
		mToolbar = findViewById(R.id.toolbar);
		mNavigationDrawer = findViewById(R.id.navigation_drawer);
		mRecyclerView = findViewById(R.id.user_list);

		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		mRecyclerView.setLayoutManager(linearLayoutManager);

		setupToolbar();
		setupDrawer();
		loadUsers();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home) {
			mNavigationDrawer.openDrawer(GravityCompat.START);
		}

		return super.onOptionsItemSelected(item);
	}

	private void showSnackbar(String msg) {
		Snackbar.make(mCoordinatorLayout, msg, Snackbar.LENGTH_LONG).show();
	}

	private void loadUsers() {
		Call<UserListRes> call = mDataManager.getUserList();

		call.enqueue(new Callback<UserListRes>() {
			@Override
			public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {
				try {
					mUsers = response.body().getUserData();
					mUsersAdapter = new UsersAdapter(mUsers, new UsersAdapter.UserViewHolder.CustomClickListener() {
						@Override
						public void onUserItemClickListener(int position) {
							UserDTO userDTO = new UserDTO(mUsers.get(position));

							Intent profileIntent = new Intent(UserListActivity.this, ProfileUserActivity.class);
							profileIntent.putExtra(ConstantManager.PARCELABLE_KEY, userDTO);

							startActivity(profileIntent);
						}
					});
					mRecyclerView.setAdapter(mUsersAdapter);
				} catch (NullPointerException e) {
					Log.e("MyLogs", e.toString());
					showSnackbar("Что-то пошло не так");
				}
			}

			@Override
			public void onFailure(Call<UserListRes> call, Throwable t) {

			}
		});
	}

	private void setupDrawer() {
		//TODO: Реализовать переход в другую активити при клике по эдементу меню в NavigationDrawer
	}

	private void setupToolbar() {
		setSupportActionBar(mToolbar);
		ActionBar actionBar = getSupportActionBar();

		if(actionBar != null) {
			actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}
}
