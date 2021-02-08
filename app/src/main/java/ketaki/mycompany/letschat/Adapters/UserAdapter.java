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

import ketaki.mycompany.letschat.Model.Users;
import ketaki.mycompany.letschat.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<Users> myUsers;

    public  UserAdapter(Context context, List<Users> myUsers )
    {
        this.context = context;
        this.myUsers = myUsers;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item,
                parent,
                false);

        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Users mUsers = myUsers.get(position);
        holder.textView.setText(mUsers.getName());
        holder.textView.setVisibility(View.VISIBLE);

       if(mUsers.getImageURL().equals("default"))
        {
            holder.imageView.setImageResource(R.mipmap.ic_launcher);
        }
        else
        {
            Glide.with(context)
                    .load(mUsers.getImageURL())
                    .into(holder.imageView);
        }


    }

    @Override
    public int getItemCount() {
        return myUsers.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
       public ImageView imageView;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            textView = itemView.findViewById(R.id.userName);
            imageView = itemView.findViewById(R.id.userImage);


        }
    }












}
