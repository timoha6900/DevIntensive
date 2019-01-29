package com.softdesign.devintensive.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.ui.views.AspectRatioImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

	Context mContext;
	List<UserListRes.UserData> mUsers;
	UserViewHolder.CustomClickListener mCustomClickListener;


	public UsersAdapter(List<UserListRes.UserData> users, UserViewHolder.CustomClickListener clickListener) {
		mUsers = users;
		this.mCustomClickListener = clickListener;
	}

	@NonNull
	@Override
	public UsersAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		mContext = parent.getContext();
		View convertView = LayoutInflater.from(mContext).inflate(R.layout.item_user_list, parent, false);
		return new UserViewHolder(convertView, mCustomClickListener);
	}

	@Override
	public void onBindViewHolder(@NonNull UsersAdapter.UserViewHolder holder, int position) {

		UserListRes.UserData user = mUsers.get(position);

		holder.mFullName.setText(user.getFullName());
		holder.mRating.setText(String.valueOf(user.getRait()));
		holder.mCodeLines.setText(String.valueOf(user.getLinesCode()));
		holder.mProjects.setText(String.valueOf(user.getProjects()));

		Picasso.with(mContext)
				.load(R.drawable.user_photo)
				.placeholder(R.drawable.login_bg)
				.error(R.drawable.login_bg)
				.into(holder.userPhoto);

		if(user.getBio() == null || user.getBio().isEmpty()){
			holder.mBio.setVisibility(View.GONE);
		}else{
			holder.mBio.setVisibility(View.VISIBLE);
			holder.mBio.setText(user.getBio());
		}
	}

	@Override
	public int getItemCount() {
		return mUsers.size();
	}


	public static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

		protected AspectRatioImageView userPhoto;
		protected TextView mFullName, mRating, mCodeLines, mProjects, mBio;
		protected Button mShowMore;

		private CustomClickListener mListener;

		public UserViewHolder(@NonNull View itemView, CustomClickListener clickListener) {
			super(itemView);
			this.mListener = clickListener;

			userPhoto = itemView.findViewById(R.id.user_photo_img);
			mFullName = itemView.findViewById(R.id.user_full_name_txt);
			mRating = itemView.findViewById(R.id.rating_txt);
			mCodeLines = itemView.findViewById(R.id.code_lines_txt);
			mProjects = itemView.findViewById(R.id.projects_txt);
			mBio = itemView.findViewById(R.id.bio_txt);
			mShowMore = itemView.findViewById(R.id.more_info_btn);
			mShowMore.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			if(mListener != null) {
				mListener.onUserItemClickListener(getAdapterPosition());
			}
		}

		public interface CustomClickListener{
			void onUserItemClickListener(int position);
		}
	}

}
