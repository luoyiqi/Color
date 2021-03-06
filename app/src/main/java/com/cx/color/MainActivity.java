package com.cx.color;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cx.target.FinalData;
import com.cx.utils.ScaleImage;

import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE = 1;
    private static final int UPDATE_IMAGE = 11;
    public static final int CLEAR_DATA=0;
    private ImageView imageView;
    private static int[] colors;
    private Bitmap bitmap;
    private  int[] pixels;

    MyHandler myHandler = new MyHandler(MainActivity.this);

    /**
     * 通过静态内部类+弱应用来解决Handler可能存在的内存泄漏问题
     */
    static class MyHandler extends Handler {
        WeakReference<MainActivity> mWeakReference;
        Toast toast;

        MyHandler(MainActivity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final MainActivity mainActivity = mWeakReference.get();
            if (mainActivity!=null&&mainActivity.pixels!=null&&msg.what==CLEAR_DATA){
                mainActivity.pixels=null;
            }
            if (mainActivity != null && msg.what == UPDATE_IMAGE) {
                View view = LayoutInflater.from(mainActivity).inflate(R.layout.recycler_home, null);
                RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv);
                LinearLayoutManager lm = new LinearLayoutManager(mainActivity);
                lm.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(lm);
                recyclerView.setAdapter(mainActivity.new MyAdapter());
                recyclerView.addOnItemTouchListener(new RecycleViewClickListener(recyclerView, new RecycleViewClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int postion) {
                        int temp = colors[postion];
                        int r, g, b;
                        r = temp >> 16 & 0xff;
                        g = temp >> 8 & 0xff;
                        b = temp & 0xff;
                        if (toast == null) {
                            toast = Toast.makeText(mainActivity, "r:" + r + "   g:" + g + "   b:" + b, Toast.LENGTH_SHORT);
                        } else {
                            toast.setText("r:" + r + "   g:" + g + "   b:" + b);
                        }
                        toast.show();
                    }

                    @Override
                    public void onItemLongClick(View view, int postion) {

                    }
                }));
                new AlertDialog.Builder(mainActivity)
                        .setView(view).create().show();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    private void initView() {
        imageView = (ImageView) findViewById(R.id.iv);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                } else {
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                }
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri selectedContent = data.getData();
            if (requestCode == REQUEST_IMAGE) {
                try {
                    if (bitmap!=null)bitmap.recycle();
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedContent));
                    imageView.setImageBitmap(bitmap);
                    startInitData();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startInitData() {
        pixels = ScaleImage.getPixels(bitmap);
        Thread t1 = new Thread() {
            @Override
            public void run() {
                colors = FinalData.getColor(pixels, pixels.length,myHandler);
                myHandler.sendEmptyMessage(UPDATE_IMAGE);
            }
        };
        t1.start();
    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.recycler_item, parent, false));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv.setBackgroundColor(colors[position]);
        }

        @Override
        public int getItemCount() {
            return colors.length;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv;

            public MyViewHolder(View itemView) {
                super(itemView);
                tv = (TextView) itemView.findViewById(R.id.rv_item);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myHandler.removeCallbacksAndMessages(null);
    }
}
