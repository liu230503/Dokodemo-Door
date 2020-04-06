package org.alee.dokodemo.door.demo.saveState;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import org.alee.dokodemo.door.demo.R;
import org.alee.dokodemo.door.demo.base.BaseRecyclerAdapter;
import org.alee.dokodemo.door.demo.base.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/7
 * @description: xxxx
 *
 *********************************************************/
final class Adapter extends BaseRecyclerAdapter<String> {
    /**
     * 布局
     */
    private LayoutInflater mInflater;

    private final Map<String, Boolean> mSelected;

    Adapter(Context context, OnItemClickListener listener) {
        super(listener, new ArrayList<>());
        mInflater = LayoutInflater.from(context);
        mSelected = new HashMap<>();
    }

    @Override
    protected boolean isEquals(String first, Object second) {
        return first.equals(second);
    }

    @Override
    protected int getHeaderSize() {
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ItemViewHolder(mInflater.inflate(R.layout.layout_tab_bar_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        if (position >= getItemCount()) {
            return;
        }
        String str = getItem(position);
        if (TextUtils.isEmpty(str)) {
            return;
        }
        itemViewHolder.mTextView.setText(str);
        itemViewHolder.mCheckBox.setOnCheckedChangeListener(null);
        itemViewHolder.mCheckBox.setChecked(mSelected.containsKey(str) ? mSelected.get(str) : false);
        itemViewHolder.mCheckBox.setTag(str);
        itemViewHolder.mCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> mSelected.put((String) buttonView.getTag(), isChecked));
        onBindClickListener(itemViewHolder.itemView);
    }


    /**
     * item布局
     */
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        private CheckBox mCheckBox;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tv_content);
            mCheckBox = itemView.findViewById(R.id.cb_select);
        }
    }
}
