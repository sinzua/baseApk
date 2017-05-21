package com.ty.followboom.views;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.ty.instaview.R;
import java.util.List;

public class OptionDialog extends Dialog {
    private ListView mListView;
    private OnItemClickListener mOnItemClickListener;
    private OptionAdapter mOptionAdapter;
    private List<String> mOptionList;

    class OptionAdapter extends BaseAdapter {
        private List<String> mOptions;

        OptionAdapter() {
        }

        public void setOptions(List<String> options) {
            this.mOptions = options;
        }

        public int getCount() {
            if (this.mOptions == null) {
                return 0;
            }
            return this.mOptions.size();
        }

        public String getItem(int position) {
            return (String) this.mOptions.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(OptionDialog.this.getContext()).inflate(R.layout.option_item, parent, false);
            ((TextView) convertView.findViewById(R.id.option_text)).setText(getItem(position));
            return convertView;
        }
    }

    public OptionDialog(Context context, List<String> optionList, OnItemClickListener onItemClickListener) {
        super(context);
        requestWindowFeature(1);
        setContentView(R.layout.option_dialog);
        this.mOnItemClickListener = onItemClickListener;
        this.mOptionList = optionList;
        initViews();
    }

    private void initViews() {
        this.mListView = (ListView) findViewById(R.id.list_view);
        this.mListView.setOnItemClickListener(this.mOnItemClickListener);
        this.mOptionAdapter = new OptionAdapter();
        this.mOptionAdapter.setOptions(this.mOptionList);
        this.mListView.setAdapter(this.mOptionAdapter);
    }
}
