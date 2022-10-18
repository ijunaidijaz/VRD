package com.vrd.gsaf.home.homeTab.documents;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.danikula.videocache.HttpProxyCacheServer;
import com.vrd.gsaf.R;
import com.vrd.gsaf.activity.PDFViewerActivity;
import com.vrd.gsaf.app.MainApp;
import com.vrd.gsaf.home.viewDocument.ViewDocumentFragment;
import com.vrd.gsaf.home.whereBy.WebViewActivity;
import com.vrd.gsaf.singleton.Singleton;
import com.vrd.gsaf.utility.DownloadFile;
import com.vrd.gsaf.utility.Helper;

import java.util.List;

public class DocumentsAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    public static final String VIDEO_ID = "6kx7kghRnvk";
    private static final long CLICK_TIME_INTERVAL = 500;
    private final List<DocumentModel> dataList;
    private final WebView webView;
    private final ProgressBar progressBar;
    public FragmentManager view;
    protected HttpProxyCacheServer proxy;
    protected MainApp app = new MainApp();
    protected double current_pos, total_duration;
    protected Handler mHandler, handler;
    private Context context;
    private long mLastClickTime = System.currentTimeMillis();


    public DocumentsAdapter(FragmentManager view, List<DocumentModel> dataList, WebView webView, ProgressBar progressBar) {
        this.webView = webView;
        this.dataList = dataList;
        this.view = view;
        mHandler = new Handler();
        handler = new Handler();
        this.progressBar = progressBar;

    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_documents, parent, false);
        return new CustomViewHolder(view);
    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        int index = position;

        holder.download.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getDownload());
        holder.view.setText(Singleton.getInstance().getFairData().getFair().getFairTranslations().get(Singleton.getInstance().getLanguageIndex()).getKeywords().getView());
        holder.documentName.setText(dataList.get(position).getDocumentName());


        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;

                String url = Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + dataList.get(index).getDocumentLink();
                if (url.contains(".pdf")){
                    MainApp.getAppContext().startActivity(new Intent(MainApp.getAppContext(), PDFViewerActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("url", url).putExtra("isCv", true));

                }else {
                    MainApp.getAppContext().startActivity(new Intent(MainApp.getAppContext(), WebViewActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("url", url).putExtra("isCv", true));
                }

                //  viewDocument(dataList.get(index).getDocumentLink());
            }
        });
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    return;
                }
                mLastClickTime = now;
                Helper.viewDocument(Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + dataList.get(index).getDocumentLink());

                //downloadDocument(dataList.get(index).getDocumentLink());


            }
        });
    }

    private void downloadDocument(String documentLink) {

        Helper.showProgressBar(progressBar);


        final DownloadFile downloadTask = new DownloadFile(Singleton.getInstance().getContext(), progressBar, Singleton.getInstance().getLoginData().getUser().getUserTaskList());
        downloadTask.execute(Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() +
                documentLink);
    }

    private void viewDocument(String documentLink) {

        Fragment fragment = new ViewDocumentFragment();
        Bundle args = new Bundle();
        args.putString("link", documentLink);
        //  args.putString("video", Singleton.getInstance().getCompanyMedia().getCompanyList().get(0).getCompanyMedia().get(j).getCompanyMediaVideo());
        fragment.setArguments(args);
        Singleton.getInstance().getMainActivity().getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, fragment)
                .setReorderingAllowed(true)
                .addToBackStack("video")
                .commit();

//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setUseWideViewPort(true);
//        webView.loadUrl("http://docs.google.com/gview?embedded=true&url="
//                + Singleton.getInstance().getFairData().getSystemUrl().getAssetsUrl() + documentLink);

    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }
}


class CustomViewHolder extends RecyclerView.ViewHolder {

    public final View mView;
    public Button view, download;
    public TextView documentName;

    CustomViewHolder(View itemView) {
        super(itemView);
        mView = itemView;

        view = mView.findViewById(R.id.viewDocumentBtn);
        download = mView.findViewById(R.id.downloadDocumentBtn);
        documentName = mView.findViewById(R.id.txt);
        documentName.setTextColor(Singleton.getCardTextColor());
        Helper.setButtonColorWithDrawable(view, Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(),
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());
        Helper.setButtonColorWithDrawable(download, Singleton.getInstance().getFairData().getFair().getOptions().getButtonBackgroundColorFront(),
                Singleton.getInstance().getFairData().getFair().getOptions().getButtonInnerColorFront());
    }
}



