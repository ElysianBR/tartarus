package br.com.elysian.tartarus.utils;


import jakarta.servlet.AsyncEvent;
import jakarta.servlet.AsyncListener;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class CustomAsyncListener implements AsyncListener {
    private final CompletableFuture<?> future;

    public CustomAsyncListener(CompletableFuture<?> future) {
        super();
        this.future = future;
    }

    @Override
    public void onComplete(AsyncEvent asyncEvent) throws IOException {

    }

    @Override
    public void onTimeout(AsyncEvent asyncEvent) throws IOException {
        System.out.println("Entrei aq: onTimeout");
        future.cancel(true);
    }

    @Override
    public void onError(AsyncEvent asyncEvent) throws IOException {
        System.out.println("Entrei aq: onError");
        future.cancel(true);
    }

    @Override
    public void onStartAsync(AsyncEvent asyncEvent) throws IOException {
        System.out.println("Entrei aq: asyncEvent");
    }
}
