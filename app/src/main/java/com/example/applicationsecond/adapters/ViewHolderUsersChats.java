package com.example.applicationsecond.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.applicationsecond.R;
import com.example.applicationsecond.api.ProjectHelper;
import com.example.applicationsecond.api.UserHelper;
import com.example.applicationsecond.models.Message;
import com.example.applicationsecond.models.Project;
import com.example.applicationsecond.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewHolderUsersChats extends RecyclerView.ViewHolder {

    @BindView(R.id.user_chats_activity_item_image)
    ImageView imageView;
    @BindView(R.id.user_chats_activity_item_last_message)
    TextView textViewLastMessage;
    @BindView(R.id.user_chats_activity_item_title) TextView textViewTitle;
    @BindView(R.id.user_chats_activity_item_time) TextView textViewTime;
    @BindView(R.id.user_chats_activity_item_unread_message_text_view) TextView textViewUnreadMessages;

    public ViewHolderUsersChats(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateUi(Message message, RequestManager glide) {
        textViewLastMessage.setText(message.getMessage());
        if (message.getUserSender().getUrlPhoto() != null) {
            glide.load(message.getUserSender().getUrlPhoto())
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageView);
        }
        textViewTime.setText(convertDateToHour(message.getDateCreated()));
        if (message.getChatName() != null) {
            ProjectHelper.getProject(message.getChatName()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        Project project = task.getResult().toObject(Project.class);
                        textViewTitle.setText(giveNewSizeToTitle(project.getTitle()));
                    }
                }
            });

        }
    }

    private String convertDateToHour(Date date){
        DateFormat dfTime = new SimpleDateFormat("HH:mm");
        return dfTime.format(date);
    }

    private String giveNewSizeToTitle(String oldString) {
        if (oldString.length() > 10) {
            return oldString.substring(0, 10) + "...";
        } else {
            return oldString;
        }
    }
}
