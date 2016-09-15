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

import com.cx.target.FinalData;
import com.cx.utils.ScaleImage;

import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE = 1;
    private static final int UPDATE_IMAGE = 11;
    private ImageView imageView;
    private static int[] colors;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE_IMAGE) {
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.recycler_home, null);
                RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv);
                LinearLayoutManager lm = new LinearLayoutManager(MainActivity.this);
                lm.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(lm);
                recyclerView.setAdapter(new MyAdapter());
                new AlertDialog.Builder(MainActivity.this)
                        .setView(view).create().show();
            }
        }
    };

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
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedContent));
                    imageView.setImageBitmap(bitmap);
                    final int[] pixels = ScaleImage.getPixels(bitmap);
                    Thread t1 = new Thread() {
                        @Override
                        public void run() {
                            colors = FinalData.getColor(pixels, pixels.length);
                            handler.sendEmptyMessage(UPDATE_IMAGE);
                        }
                    };
                    t1.start();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


            }

        }
        super.onActivityResult(requestCode, resultCode, data);
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
}
