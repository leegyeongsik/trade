package edu.cnu.swacademy.exchange.thread;

import edu.cnu.swacademy.exchange.process.WorkerQueue;

public class RequestWorkerThread implements Runnable{
    private final WorkerQueue workerQueue;

    public RequestWorkerThread(WorkerQueue workerQueue) {
        this.workerQueue = workerQueue;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            workerQueue.orderPoll();
        }
    }
}
