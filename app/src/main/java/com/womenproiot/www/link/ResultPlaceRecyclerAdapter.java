package com.womenproiot.www.link;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ResultPlaceRecyclerAdapter extends RecyclerView.Adapter {

    private LinkDAO db;
    private ArrayList<MeetupDto> meetupList;

    //생성자
    public ResultPlaceRecyclerAdapter(Context context, String seq) {
        db = LinkDAO.getInstance(context);
        //meetupList = db.selectMeetUp(seq);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {

        return meetupList.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {

        //생성자
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
        }
    }
}
