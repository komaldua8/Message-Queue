package com.messagebroker.queue;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.messagebroker.model.Message;

public class MessageQueue {
    private final Queue<Message> queue = new LinkedList<>();
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    public void enqueue(Message message) {
        lock.lock();
        try {
            queue.add(message);
            condition.signal();
        } finally {
            lock.unlock();
        }
    }
    public Message dequeue() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                condition.await();
            }
            return queue.poll();
        } finally {
            lock.unlock();
        }
    }
}
