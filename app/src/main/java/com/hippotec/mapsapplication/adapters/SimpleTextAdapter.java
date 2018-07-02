package com.hippotec.mapsapplication.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hippotec.mapsapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SimpleTextAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context m_context;
    private int m_start;
    private int m_end;
    private int m_cur;
    private ValueClickedListener m_valueClickedListener;

    final private int TYPE_YEAR = 0;

    public interface ValueClickedListener {
        void onClick(int val);
    }

    public void registerCallback(ValueClickedListener listener) {
        m_valueClickedListener = listener;
    }

    public SimpleTextAdapter(Context context, int startYear, int endYear, int averageYear) {
        this.m_context = context;
        this.m_start = startYear;
        this.m_end = endYear;
        this.m_cur = averageYear;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case TYPE_YEAR:
                viewHolder = new SimpleTextViewHolder(layoutInflater.inflate(R.layout.simple_text_item_view, parent, false));
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int curYear = m_start + position;

        switch (holder.getItemViewType()) {
            case TYPE_YEAR:
                ((SimpleTextViewHolder)holder).bind(Integer.toString(curYear));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return m_end - m_start + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_YEAR;
    }

    public class SimpleTextViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_simple_text)
        TextView m_tvText;

        private SimpleTextViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(String txt) {

            m_tvText.setText(txt);
            m_tvText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    m_valueClickedListener.onClick(m_start + getLayoutPosition());
                }
            });

        }
    }
}

