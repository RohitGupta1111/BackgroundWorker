package com.example.backgroundworker;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncWorker {

    public static final String TAG = "AsyncWorker";

    ExecutorService executor;
    Task currentTask;
    Queue<Task> taskQueue;
    Boolean mActivetask = false;
    Handler mHandler;

    public AsyncWorker() {
        executor = Executors.newSingleThreadExecutor();
        taskQueue = new LinkedList<>();
        mHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                Log.d(TAG,"complete loading");
                Object t = msg.obj;
                currentTask.onTaskComplete(t);
                runTask();
            }
        };

    }



    public void addTask(Task task) {
        Log.d(TAG,"adding task");
        taskQueue.add(task);
        if(!mActivetask) {
            runTask();
        }
    }

    private void runTask() {
        if(taskQueue.isEmpty()) {
            mActivetask = false;
            return;
        } else {
            mActivetask = true;
        }
        currentTask = taskQueue.peek();
        taskQueue.poll();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Object t = null;
                try {
                     Log.d(TAG,"executing task");
                     t = currentTask.onExecuteTask();
                } finally {
                    Message msg = mHandler.obtainMessage(0,t);
                    mHandler.sendMessage(msg);
                }
            }
        });
    }
}
