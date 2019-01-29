package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.NetworkStatusChecker;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends BaseActivity implements View.OnClickListener {

	private Button mSignIn;
	private TextView mRememberPassword;
	private EditText mLogin, mPassword;
	private CoordinatorLayout mCoordinatorLayout;

	private DataManager mDataManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auth);

		mDataManager = DataManager.getInstance();

		mCoordinatorLayout = findViewById(R.id.main_coordinator_container);
		mSignIn = findViewById(R.id.login_btn);
		mRememberPassword = findViewById(R.id.remember_txt);
		mLogin = findViewById(R.id.login_email_et);
		mPassword = findViewById(R.id.login_password_et);

		mSignIn.setOnClickListener(this);
		mRememberPassword.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.login_btn:
				signIn();
				break;

			case R.id.remember_txt:
				rememberPassword();
				break;
		}
	}

	private void showSnackbar(String msg) {
		Snackbar.make(mCoordinatorLayout, msg, Snackbar.LENGTH_LONG).show();
	}

	private void rememberPassword() {
		Intent rememberIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://devintensive.softdesign-apps.ru/forgotpass"));
		startActivity(rememberIntent);
	}

	private void loginSuccess(UserModelRes userModel) {
		Log.d("MyLogs", "Phone: " + userModel.getData().getPhone());
		Log.d("MyLogs", "Email: " + userModel.getData().getEmail());
		Log.d("MyLogs", "VK: " + userModel.getData().getVk());
		Log.d("MyLogs", "Git: " + userModel.getData().getGit());

		saveUserValues(userModel.getProfileValues());
		saveUserData(userModel.getData());

		Intent loginIntent = new Intent(this, MainActivity.class);
		startActivity(loginIntent);
	}

	private void saveUserValues(UserModelRes.ProfileValues profileValues) {
		int[] values = new int[3];
		values[0] = profileValues.getRait();
		values[1] = profileValues.getLinesCode();
		values[2] = profileValues.getProjects();
		mDataManager.getPreferencesManager().saveUserProfileValues(values);
	}

	private void saveUserData(UserModelRes.Data userData) {
		ArrayList<String> data = new ArrayList<>();
		data.add(userData.getPhone());
		data.add(userData.getEmail());
		data.add(userData.getVk());
		data.add(userData.getGit());
		mDataManager.getPreferencesManager().saveUserProfileDataWithoutBio(data);
	}

	private void signIn() {
		if (NetworkStatusChecker.isNetworkAvailable(this)) {
			Call<UserModelRes> call = mDataManager.loginUser(new UserLoginReq(mLogin.getText().toString(), mPassword.getText().toString()));
//			Call<UserModelRes> call = mDataManager.loginUser(new UserLoginReq("test@ukr.net", "qwerty"));
			call.enqueue(new Callback<UserModelRes>() {
				@Override
				public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
					if (response != null && response.body() != null) {
						loginSuccess(response.body());
					} else {
						showSnackbar("Неверный логин или пароль!");
					}
				}

				@Override
				public void onFailure(Call<UserModelRes> call, Throwable t) {
					showSnackbar("Шеф, всё пропало!");
				}
			});
		} else {
			showSnackbar("Сеть недоступна, попробуйте позже!");
		}
	}

}
