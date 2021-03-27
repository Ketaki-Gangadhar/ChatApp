package ketaki.mycompany.letschat.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import ketaki.mycompany.letschat.MessageActivityy;
import ketaki.mycompany.letschat.Model.Chat;
import ketaki.mycompany.letschat.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Context mContext;
    private List<Chat> mChat;
    private String imgURL;
    FirebaseUser fUser;
 public static final int MSG_TYPE_LEFT = 0;
 public static final int MSG_TYPE_RIGHT= 1;

    public MessageAdapter(Context mContext, List<Chat> mChat, String imgURL)
    {
        this.mChat = mChat;
        this.mContext = mContext;
        this.imgURL = imgURL;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==MSG_TYPE_RIGHT){
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,
                parent,
                false);
        return new MessageAdapter.ViewHolder(view);}
        else
        {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left,
                    parent,
                    false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        Chat chat = mChat.get(position);

        holder.show_message.setText(chat.getMessage());

        if(imgURL.equals("default"))
        {
            holder.profileImage.setImageResource(R.drawable.person);
        }
        else
        {
            Glide.with(mContext).load(imgURL).into(holder.profileImage);
        }



    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView show_message;
        public  ImageView profileImage;

        public  ViewHolder(View itemView)
        {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_msg);
            profileImage = itemView.findViewById(R.id.profileImage);
        }

    }

    @Override
    public int getItemViewType(int position) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getSender().equals(fUser.getUid()))
        {
            return MSG_TYPE_RIGHT;
        }
        else
        {
            return MSG_TYPE_LEFT;
        }
    }
}