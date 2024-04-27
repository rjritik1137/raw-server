package com.concurrent;

import java.util.ArrayList;

public class Queue<T> {
    private ArrayList<T> list;
    synchronized public void enqueue(T item ){
        list.add(item);
    }
    synchronized public T dequeue(){
        if(list.isEmpty()) throw new RuntimeException("Trying to remove an element from an empty queue");
        return list.remove(0);
    }

    synchronized public boolean isEmpty() {
        return list.isEmpty();
    }
}
