package com.example.backgroundworker;

public interface Task<T> {
    T onExecuteTask();
    void onTaskComplete(T args);
}
