package com.softdesign.devintensive.ui.activities;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;

public class ProfileUserActivity extends AppCompatActivity {

	private Toolbar mToolbar;
	private ImageView mProfileImage;
	private EditText mUserGit, mUserBio;
	private TextView mUserRating, mUserCodeLines, mUserProjects;

	private CollapsingToolbarLayout mCollapsingToolbarLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_user);

		mToolbar = findViewById(R.id.toolbar);
		mProfileImage = findViewById(R.id.user_photo_img);
		mUserGit = findViewById(R.id.git_et);
		mUserBio = findViewById(R.id.bio_et);
		mUserRating = findViewById(R.id.user_info_rait_txt);
		mUserCodeLines = findViewById(R.id.user_info_code_lines_txt);
		mUserProjects = findViewById(R.id.user_info_project_txt);

		mCollapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);

		setupToolbar();
		initProfileData();

	}

	private void setupToolbar() {
		setSupportActionBar(mToolbar);

		ActionBar actionBar = getSupportActionBar();

		if(actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	private void initProfileData() {
		UserDTO userDTO = getIntent().getParcelableExtra(ConstantManager.PARCELABLE_KEY);

		mUserGit.setText("github.com/pipka");
		mUserBio.setText(userDTO.getBio());
		mUserRating.setText(userDTO.getRating());
		mUserCodeLines.setText(userDTO.getCodeLines());
		mUserProjects.setText(userDTO.getProjects());

		mCollapsingToolbarLayout.setTitle(userDTO.getFullName());

		Picasso.with(this)
				.load(R.drawable.user_photo)
				.placeholder(R.drawable.ic_add_a_photo_black_24dp)
				.error(R.drawable.ic_add_a_photo_black_24dp)
				.into(mProfileImage);
	}
}
