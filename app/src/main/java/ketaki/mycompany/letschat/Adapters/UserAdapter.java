package ketaki.mycompany.letschat.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import ketaki.mycompany.letschat.Model.MyUsers;
import ketaki.mycompany.letschat.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<MyUsers> mUsers;

    public UserAdapter(Context mContext, List<MyUsers> mUsers)
    {
        this.mUsers = mUsers;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            MyUsers user = mUsers.get(position);
            holder.username.setText(user.getUsername());
            if(user.getImageURL().equals("default"))
            {
                holder.profile_image.setImageResource(R.mipmap.ic_launcher);
            }
            else
            {
                Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);
            }

    }

    @Override
    public int getItemCount() {
       return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public  ImageView profile_image;

        public  ViewHolder(View itemView)
        {
            super(itemView);

            username = itemView.findViewById(R.id.userName);
            profile_image = itemView.findViewById(R.id.userImage);
        }

    }


}