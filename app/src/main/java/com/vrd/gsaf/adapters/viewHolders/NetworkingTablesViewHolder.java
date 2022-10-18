package com.vrd.gsaf.adapters.viewHolders;

import android.app.AlertDialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.vrd.gsaf.R;
import com.vrd.gsaf.callbacks.NetworkingTablesCallback;
import com.vrd.gsaf.model.NetworkingTable;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.Helper;


public class NetworkingTablesViewHolder extends RecyclerView.ViewHolder {

    public TextView typeText, type, membersText, members, title;
    public ImageView cross;
    public View parent;
    Button join, viewDetails;
    Context context;
    ImageView startTimeIcon, endTimeIcon, userImageViewRound;

    public NetworkingTablesViewHolder(@NonNull View itemView) {
        super(itemView);
        parent = itemView;
        title = itemView.findViewById(R.id.title);
        typeText = itemView.findViewById(R.id.type);
        type = itemView.findViewById(R.id.startAtTxt);
        membersText = itemView.findViewById(R.id.endAt);
        members = itemView.findViewById(R.id.entAtTxt);
        join = itemView.findViewById(R.id.joinBtn);
        userImageViewRound = itemView.findViewById(R.id.userImageViewRound);
        startTimeIcon = itemView.findViewById(R.id.startTimeIcon);
        endTimeIcon = itemView.findViewById(R.id.endTimeIcon);
        viewDetails = itemView.findViewById(R.id.viewDetails);

        ConstraintLayout mainLayout = itemView.findViewById(R.id.parentLayout);
        mainLayout.setBackgroundColor(Singleton.getCardBGColor());
        Singleton.changeVectorIconColor(R.drawable.ic_group, endTimeIcon, Singleton.getSidebarInnerTextColor());
        Singleton.changeVectorIconColor(R.drawable.ic_networking, startTimeIcon, Singleton.getSidebarInnerTextColor());
        Helper.setButtonColorWithDrawable(join, Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(),
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());
        Helper.setButtonColorWithDrawable(viewDetails, Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(),
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());

        type.setText(Singleton.getKeywords().getType());

    }


    public void setData(Context context, NetworkingTable item, NetworkingTablesViewHolder holder, int position, NetworkingTablesCallback callback) {
        title.setText(item.getTitle());
        type.setText(item.getType());
        Helper.loadCircularImageFromUrl(userImageViewRound, Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + item.getTableLogo());
        members.setText(item.getMembersCount());
        join.setText(Singleton.getKeywords().getClickToJoin());
        viewDetails.setText(Singleton.getKeywords().getDetail());
        join.setOnClickListener(view ->
                Helper.viewDocument(item.getJoiningLink())
        );
        viewDetails.setOnClickListener(view ->
                openWelcomeDialog(item.getDescription())
        );
    }

    public void openWelcomeDialog(String data) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Singleton.getInstance().getContext());
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = Singleton.getInstance().getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_term_and_conditions, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();

        ImageView cancel = dialogView.findViewById(R.id.cancelImageView);
        TextView webinarTitleTxt = dialogView.findViewById(R.id.webinarTitleTxt);
        TextView descriptionTxt = dialogView.findViewById(R.id.descriptionTxt);
        webinarTitleTxt.setText(Singleton.getKeywords().getDetail());
        Helper.loadHtml(descriptionTxt, data);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        Singleton.getInstance().getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
//        ViewGroup.LayoutParams params = dialogView.getLayoutParams();
//        params.height = height-200;
//        params.width = width-200;
//        dialogView.setLayoutParams(params);

//        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//        layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
//        layoutParams.width = height-200;
//        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT-200;
//        alertDialog.getWindow().setAttributes(layoutParams);
        ViewGroup.LayoutParams window = dialogView.getLayoutParams();

        // window.setLayout(width-100, height/2);
//        window.height=height/2;

        alertDialog.getWindow().setLayout(200, 400);
        alertDialog.show();
    }

}
