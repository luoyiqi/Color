package com.cx.color;

import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * RecyclerView点击事件的监听类
 * Created by CX on 2016/9/16.
 */
public class RecycleViewClickListener extends RecyclerView.SimpleOnItemTouchListener {
   private OnItemClickListener onItemClickListener;
    private GestureDetector gestureDetector;

    //设置点击事件的回调接口
    public interface OnItemClickListener {
        void onItemClick(View view, int postion);

        void onItemLongClick(View view, int postion);
    }
    public RecycleViewClickListener(final RecyclerView recyclerView, final OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
        gestureDetector=new GestureDetector(recyclerView.getContext(),new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                View childView=recyclerView.findChildViewUnder(e.getX(),e.getY());
                if (childView!=null&&onItemClickListener!=null){
                  onItemClickListener.onItemClick(childView,recyclerView.getChildAdapterPosition(childView));
                    return true;
                }else return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View childView=recyclerView.findChildViewUnder(e.getX(),e.getY());
                if (childView!=null&&onItemClickListener!=null){
                    onItemClickListener.onItemLongClick(childView,recyclerView.getChildAdapterPosition(childView));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        return gestureDetector.onTouchEvent(e);
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
