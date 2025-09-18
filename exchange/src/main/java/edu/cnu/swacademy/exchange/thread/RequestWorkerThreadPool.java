package edu.cnu.swacademy.exchange.thread;

import edu.cnu.swacademy.exchange.config.RequestWorkerThreadSetting;
import edu.cnu.swacademy.exchange.process.WorkerQueue;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RequestWorkerThreadPool {
    private final Thread[] workerThreads;

    public RequestWorkerThreadPool(RequestWorkerThreadSetting setting, WorkerQueue workerQueue) {
        if (Objects.isNull(workerQueue)) {
            throw new IllegalArgumentException("workerQueue is null");
        }

        workerThreads = new Thread[setting.getTotalThread()];
        for (int i = 0; i < setting.getTotalThread(); i++) {
            workerThreads[i] = new Thread(new RequestWorkerThread(workerQueue));
            workerThreads[i].setName(String.format("RequestWorkerThread-%d", i));
        }
    }

    public synchronized void start() {
        for (Thread thread : workerThreads) {
            thread.start();
        }
    }

    public synchronized void stop() {
        for (Thread thread : workerThreads) {
            if (Objects.nonNull(thread) && thread.isAlive()) {
                thread.interrupt();
            }
        }
        for (Thread thread : workerThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
