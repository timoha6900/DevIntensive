package com.softdesign.devintensive.ui.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.PhoneNumberTextWatcher;
import com.softdesign.devintensive.utils.RoundedDrawable;
import com.softdesign.devintensive.utils.SiteLinkTextWatcher;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

	private static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";

	private DataManager mDataManager;
	private int mCurrentEditMode;

	private CoordinatorLayout mCoordinatorLayout;
	private Toolbar mToolbar;
	private DrawerLayout mNavigatioDrawer;
	private FloatingActionButton mFab;
	private RelativeLayout mProfilePlaceholder;
	private CollapsingToolbarLayout mCollapsingToolbar;
	private AppBarLayout mAppBarLayout;
	private ImageView mProfileImage;
	private ImageView mAvatarImg;

	@BindView(R.id.phone_et)
	EditText mUserPhone;
	@BindView(R.id.email_et)
	EditText mUserMail;
	@BindView(R.id.vk_et)
	EditText mUserVk;
	@BindView(R.id.git_et)
	EditText mUserGit;
	@BindViews({R.id.phone_et, R.id.email_et, R.id.vk_et, R.id.git_et, R.id.bio_et})
	List<EditText> mUserInfoViews;

	private TextView mUserValueRating, mUserValueCodeLines, mUserValueProjects;
	private List<TextView> mUserValuesViews;

	private AppBarLayout.LayoutParams mAppBarParams = null;
	private File mPhotoFile = null;
	private Uri mSelectedImage = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ButterKnife.bind(this);

		mDataManager = DataManager.getInstance();

		mCoordinatorLayout = findViewById(R.id.main_coordinator_container);
		mToolbar = findViewById(R.id.toolbar);
		mNavigatioDrawer = findViewById(R.id.navigation_drawer);
		mFab = findViewById(R.id.fab);
		mProfilePlaceholder = findViewById(R.id.profile_placeholder);
		mCollapsingToolbar = findViewById(R.id.collapsing_toolbar);
		mAppBarLayout = findViewById(R.id.appbar_layout);
		mProfileImage = findViewById(R.id.user_photo_img);

		mUserValueRating = findViewById(R.id.user_info_rait_txt);
		mUserValueCodeLines = findViewById(R.id.user_info_code_lines_txt);
		mUserValueProjects = findViewById(R.id.user_info_project_txt);

		mUserValuesViews = new ArrayList<>();
		mUserValuesViews.add(mUserValueRating);
		mUserValuesViews.add(mUserValueCodeLines);
		mUserValuesViews.add(mUserValueProjects);

		mUserPhone.addTextChangedListener(new PhoneNumberTextWatcher());
		mUserVk.addTextChangedListener(new SiteLinkTextWatcher("vk.com/"));
		mUserGit.addTextChangedListener(new SiteLinkTextWatcher("github.com/"));

		setupToolbar();
		setupDrawer();
		initUserFields();
		initUserInfoValues();

		Picasso.with(this)
				.load(mDataManager.getPreferencesManager().loadUserPhoto())
				.placeholder(R.drawable.user_photo)
				.into(mProfileImage);

		if(savedInstanceState == null) {
			mCurrentEditMode = 0;
		} else {
			mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY, 0);
		}
		changeEditMode(mCurrentEditMode);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home) {
			mNavigatioDrawer.openDrawer(GravityCompat.START);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPause() {
		super.onPause();
		saveUserFields();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);
	}

	@Override
	public void onBackPressed() {
		if(mNavigatioDrawer.isDrawerOpen(GravityCompat.START)) {
			mNavigatioDrawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		switch (requestCode){
			case ConstantManager.REQUEST_GALLERY_PICTURE:
				if(resultCode == RESULT_OK && data != null) {
					mSelectedImage = data.getData();

					insertProfileImage(mSelectedImage);
				}
				break;

			case ConstantManager.REQUEST_CAMERA_PICTURE:
				if(resultCode == RESULT_OK && mPhotoFile != null) {
					mSelectedImage = Uri.fromFile(mPhotoFile);

					insertProfileImage(mSelectedImage);
				}
				break;
		}
	}

	@OnClick({R.id.fab, R.id.profile_placeholder, R.id.call_img, R.id.email_img, R.id.vk_img, R.id.git_img})
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.fab:
				if(mCurrentEditMode == 0){
					changeEditMode(1);
					mCurrentEditMode = 1;
				} else {
					changeEditMode(0);
					mCurrentEditMode = 0;
				}
				break;

			case R.id.profile_placeholder:
				showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
				break;

			case R.id.call_img:
				String phone = mUserPhone.getText().toString();
				Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
				startActivity(callIntent);
				break;

			case R.id.email_img:
				String email = mUserMail.getText().toString();
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
				startActivity(emailIntent);
				break;

			case R.id.vk_img:
				String urlVk = mUserVk.getText().toString();
				Intent vkIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("https", urlVk, null));
				startActivity(vkIntent);
				break;

			case R.id.git_img:
				String urlGit = mUserGit.getText().toString();
				Intent gitIntent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("https", urlGit, null));
				startActivity(gitIntent);
				break;
		}
	}



	private void showSnackbar(String message) {
		Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
	}

	private void setupToolbar() {
		setSupportActionBar(mToolbar);
		ActionBar actionBar = getSupportActionBar();

		mAppBarParams = (AppBarLayout.LayoutParams) mCollapsingToolbar.getLayoutParams();

		if(actionBar != null) {
			actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	private void setupDrawer() {
		NavigationView navigationView = findViewById(R.id.navigation_view);
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem item) {
				showSnackbar(item.getTitle().toString());
				item.setChecked(true);
				mNavigatioDrawer.closeDrawer(GravityCompat.START);
				return false;
			}
		});

		if(navigationView.getHeaderCount() != 0){
			mAvatarImg = navigationView.getHeaderView(0).findViewById(R.id.user_avatar_img);
			if(mAvatarImg != null){
				mAvatarImg.setImageDrawable(new RoundedDrawable(getResources(), R.drawable.user_photo));
			}
		}
	}

	/**
	 * switch edit mode
	 * @param mode 1 - edit mode, 0 - read mode
	 */
	private void changeEditMode(int mode) {
		if(mode == 1) {
			mFab.setImageResource(R.drawable.ic_done_black_24dp);
			ButterKnife.apply(mUserInfoViews, enableSetter, true);
			mUserPhone.requestFocus();
			showProfilePlaceholder();
			lockToolbar();
			mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
		} else {
			mFab.setImageResource(R.drawable.ic_create_black_24dp);
			ButterKnife.apply(mUserInfoViews, enableSetter, false);
			saveUserFields();
			hideProfilePlaceholder();
			unlockToolbar();
			mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.white));
		}
	}

	private ButterKnife.Setter enableSetter = new ButterKnife.Setter<EditText, Boolean>() {
		@Override
		public void set(@NonNull EditText view, Boolean value, int index) {
			view.setEnabled(value);
			view.setFocusable(value);
			view.setFocusableInTouchMode(value);
		}
	};

	private void initUserFields() {
		List<String> userData = mDataManager.getPreferencesManager().loadUserProfileData();
		for(int i = 0; i < userData.size(); i++) {
			mUserInfoViews.get(i).setText(userData.get(i));
		}
	}

	private void saveUserFields() {
		List<String> userData = new ArrayList<>();
		for(EditText userFieldView : mUserInfoViews) {
			userData.add(userFieldView.getText().toString());
		}
		mDataManager.getPreferencesManager().saveUserProfileData(userData);
	}

	private void initUserInfoValues(){
		List<String> userData = mDataManager.getPreferencesManager().loadUserProfileValues();
		for(int i = 0; i < userData.size(); i++){
			mUserValuesViews.get(i).setText(userData.get(i));
		}
	}

	private void loadPhotoFromGallery() {
		Intent takeGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

		takeGalleryIntent.setType("image/*");
		startActivityForResult(Intent.createChooser(takeGalleryIntent, getString(R.string.user_profile_chose_manager)),
				ConstantManager.REQUEST_GALLERY_PICTURE);
	}

	private void loadPhotoFromCamera() {
		if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
				&& ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

			Intent takeCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			try {
				mPhotoFile = createImageFile();
			} catch (IOException e) {
				e.printStackTrace();
				// TODO: handle error
			}

			if (mPhotoFile != null) {
				Uri imageUri = FileProvider.getUriForFile(MainActivity.this,
						getPackageName() + ".provider",
						mPhotoFile);
				takeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(takeCaptureIntent, ConstantManager.REQUEST_CAMERA_PICTURE);
			}
		} else {
			ActivityCompat.requestPermissions(this, new String[] {
					Manifest.permission.CAMERA,
					Manifest.permission.WRITE_EXTERNAL_STORAGE
				}, ConstantManager.CAMERA_REQUEST_PERMISSION_CODE);

			Snackbar.make(mCoordinatorLayout, R.string.request_permission_snackbar_hint, Snackbar.LENGTH_LONG)
					.setAction("Разрешить", new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							openApplicationSettings();
						}
					}).show();
		}

	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if(requestCode == ConstantManager.CAMERA_REQUEST_PERMISSION_CODE && grantResults.length == 2) {
			if(grantResults[0] == PackageManager.PERMISSION_GRANTED
					&& grantResults[1] == PackageManager.PERMISSION_GRANTED) {
				loadPhotoFromCamera();
			}
		}
	}

	private void hideProfilePlaceholder() {
		mProfilePlaceholder.setVisibility(View.GONE);
	}

	private void showProfilePlaceholder() {
		mProfilePlaceholder.setVisibility(View.VISIBLE);
	}

	private void lockToolbar() {
		mAppBarLayout.setExpanded(true, true);
		mAppBarParams.setScrollFlags(0);
		mCollapsingToolbar.setLayoutParams(mAppBarParams);
	}

	private void unlockToolbar() {
		mAppBarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL |
				AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
		mCollapsingToolbar.setLayoutParams(mAppBarParams);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id){
			case ConstantManager.LOAD_PROFILE_PHOTO:
				String[] selectItems = {getString(R.string.user_profile_dialog_gallery),
						getString(R.string.user_profile_dialog_camera),
						getString(R.string.user_profile_dialog_cancel)};

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(R.string.user_profile_dialog_title);
				builder.setItems(selectItems, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int choiceItem) {
						switch (choiceItem){
							case 0:
								loadPhotoFromGallery();
								break;

							case 1:
								loadPhotoFromCamera();
								break;

							case 2:
								dialog.cancel();
								break;
						}
					}
				});
				return builder.create();

				default:
					return null;
		}
	}

	private File createImageFile() throws IOException {
		String timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

		File image = File.createTempFile(imageFileName, ".jpg", storageDir);

		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
		values.put(MediaStore.MediaColumns.DATA, image.getAbsolutePath());

		this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

		return image;
	}

	private void insertProfileImage(Uri selectedImage) {
		Picasso.with(this)
				.load(selectedImage)
				.placeholder(R.drawable.user_photo)
				.into(mProfileImage);

		mDataManager.getPreferencesManager().saveUserPhoto(selectedImage);
	}

	public void openApplicationSettings() {
		Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));

		startActivityForResult(appSettingsIntent, ConstantManager.PERMISSION_REQUEST_SETTINGS_CODE);
	}

}
