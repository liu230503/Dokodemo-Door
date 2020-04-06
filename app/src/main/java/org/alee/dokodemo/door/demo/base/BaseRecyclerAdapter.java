package org.alee.dokodemo.door.demo.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/7
 * @description: xxxx
 *
 *********************************************************/
public abstract class BaseRecyclerAdapter<E> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnItemClickListener {
    /**
     * item点击事件监听
     */
    protected OnItemClickListener mOnItemClickListener;
    /**
     * 数据源
     */
    protected List<E> mDatas;

    protected BaseRecyclerAdapter(OnItemClickListener listener, List<E> datas) {
        if (listener == null) {
            mOnItemClickListener = this;
        } else {
            mOnItemClickListener = listener;
        }
        mDatas = datas;
    }

    /**
     * 为item绑定点击事件
     *
     * @param view item
     */
    protected void onBindClickListener(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, getPositionByData((E) v.getTag()));
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mOnItemClickListener.onItemLongClick(v, getPositionByData((E) v.getTag()));
                return false;
            }
        });
    }

    /**
     * 根据数据获取位置
     *
     * @param data 数据
     * @return 位置
     */
    public int getPositionByData(E data) {
        int position = mDatas.indexOf(data);
        if (position >= 0) {
            return getHaveHeaderPosition(position);
        }
        for (E item : mDatas) {
            if (item == null) {
                continue;
            }
            if (isEquals(item, data)) {
                position = mDatas.indexOf(item);
                break;
            }
        }
        return getHaveHeaderPosition(position);
    }

    /**
     * 获取带有header后的位置
     *
     * @param position 位置
     * @return 结果
     */
    protected int getHaveHeaderPosition(int position) {
        if (0 > position) {
            return position;
        }
        return position + getHeaderSize();
    }

    /**
     * 两条数据是否相同
     *
     * @param first  第一条数据
     * @param second 第二条数据
     * @return 结果
     */
    protected abstract boolean isEquals(E first, Object second);

    /**
     * 获取当前header的数量
     *
     * @return header的数量
     */
    protected abstract int getHeaderSize();

    /**
     * 添加头数据
     *
     * @param items 数据
     */
    public void addHeaderItem(List<E> items) {
        if (items == null || items.size() <= 0) {
            return;
        }
        mDatas.addAll(0, items);
        updateDataSize();
        notifyItemRangeInserted(getHeaderSize(), getItemCount() - 1);
    }

    /**
     * 更新数据大小
     */
    protected void updateDataSize() {

    }

    /**
     * 删除数据
     *
     * @param position 位置
     */
    public void onDeleteItem(int position) {
        if (position >= getItemCount()) {
            return;
        }
        mDatas.remove(getDataPosition(position));
        updateDataSize();
        notifyItemRemoved(position);
    }

    /**
     * 获取数据的真实位置
     *
     * @param position 在列表中的位置
     * @return 在数据集合中的位置
     */
    protected int getDataPosition(int position) {
        int dataPosition = position - getHeaderSize();
        return dataPosition >= 0 ? dataPosition : 0;
    }

    /**
     * 刷新某一条item
     *
     * @param position 位置
     * @param data     数据
     */
    public void onUpdateItem(int position, E data) {
        onUpdateItem(position, data, new Object[0]);
    }

    /**
     * 刷新某一条item 只更新部分view
     *
     * @param position        位置
     * @param data            数据
     * @param needRefreshView item中需要刷新的view
     */
    public void onUpdateItem(int position, E data, Object... needRefreshView) {
        if (data == null) {
            return;
        }
        if (getDataPosition(position) >= mDatas.size()) {
            return;
        }
        mDatas.set(getDataPosition(position), data);
        if (null == needRefreshView || 0 == needRefreshView.length) {
            notifyItemChanged(position);
        } else {
            notifyItemChanged(position, needRefreshView);
        }
    }

    /**
     * 添加尾数据
     *
     * @param items 数据
     */
    public void addFooterItem(List<E> items) {
        if (items == null) {
            return;
        }
        int positionStart = mDatas.size();
        mDatas.addAll(items);
        updateDataSize();
        notifyItemRangeInserted(positionStart, mDatas.size() - 1);
    }

    /**
     * 刷新所有数据
     *
     * @param items 数据
     */
    public void notifyData(List<E> items) {
        clear();
        if (items.size() > 0) {
            mDatas.addAll(items);
            updateDataSize();
            notifyDataSetChanged();
        }
    }

    /**
     * 清空数据
     */
    private void clear() {
        int dataSize = getItemCount();
        mDatas.clear();
        if (dataSize == 0) {
            return;
        }
        notifyItemRangeChanged(getHeaderSize(), dataSize);
    }

    public List<E> getDatas() {
        return mDatas;
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null) {
            return;
        }
        holder.itemView.setTag(getItem(position));
    }


    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (holder == null) {
            return;
        }
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
            return;
        }
        onRefreshView(holder, position, payloads);
    }


    @Override
    public int getItemCount() {
        if (mDatas.size() == 0) {
            return getHeaderSize();
        }
        return mDatas.size() + getHeaderSize();
    }

    /**
     * 更新item的部分view
     *
     * @param holder   item
     * @param position 位置
     * @param payloads 需要更新的view标识
     */
    protected void onRefreshView(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {

    }

    /**
     * 获取一条数据
     *
     * @param position 位置
     * @return 数据
     */
    public E getItem(int position) {
        if (position < 0) {
            return null;
        }
        if (position >= getItemCount()) {
            return null;
        }
        // by liumy@meixing.com 2018-10-23
        if (getDataPosition(position) >= mDatas.size()) {
            return null;
        }
        return mDatas.get(getDataPosition(position));
    }

}