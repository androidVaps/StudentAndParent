package com.example.san.myapplication.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.san.myapplication.Module.Attendance;
import com.example.san.myapplication.R;

import java.util.List;

/**
 * Created by vaps on 8/18/2017.
 */

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.MyViewHolder> {

    private List<Attendance> attendanceList ;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txtMonth, txtHeld, txtAttended, txtPercent;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            txtMonth = (TextView) itemView.findViewById(R.id.month);
            txtHeld = (TextView) itemView.findViewById(R.id.held);
            txtAttended = (TextView) itemView.findViewById(R.id.attended);
            txtPercent = (TextView) itemView.findViewById(R.id.percentage);
        }
    }

    public AttendanceAdapter(List<Attendance> attendanceList) {
        this.attendanceList = attendanceList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_attendance_history, parent, false);
            return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Attendance attendance = attendanceList.get(position);

        holder.txtMonth.setText(attendance.getMonth());
        holder.txtHeld.setText(attendance.getClassHeld()+"");
        holder.txtAttended.setText(attendance.getAttended()+"");
        holder.txtPercent.setText(attendance.getPercentage()+"");
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }
}
