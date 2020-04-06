package org.alee.dokodemo.door.demo.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.alee.dokodemo.door.demo.R;
import org.alee.dokodemo.door.demo.base.BaseRecyclerAdapter;
import org.alee.dokodemo.door.demo.base.OnItemClickListener;

import java.util.ArrayList;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/7
 * @description: xxxx
 *
 *********************************************************/
final class MainAdapter extends BaseRecyclerAdapter<String> {
    /**
     * 布局
     */
    private LayoutInflater mInflater;

    MainAdapter(Context context, OnItemClickListener listener) {
        super(listener, new ArrayList<>());
        mInflater = LayoutInflater.from(context);
    }

    @Override
    protected boolean isEquals(String first, Object second) {
        return first.equals(second);
    }

    @Override
    protected int getHeaderSize() {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(mInflater.inflate(R.layout.layout_main_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        if (position >= getItemCount()) {
            return;
        }
        String str = getItem(position);
        if (TextUtils.isEmpty(str)) {
            return;
        }
        itemViewHolder.mFunctionBtn.setText(str);
        itemViewHolder.mFunctionBtn.setTag(str);
        onBindClickListener(itemViewHolder.mFunctionBtn);
    }

    /**
     * item布局
     */
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private Button mFunctionBtn;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mFunctionBtn = itemView.findViewById(R.id.btn_function);
        }
    }
}
