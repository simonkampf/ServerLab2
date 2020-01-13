package com.example.lab3;


import io.vertx.core.Vertx;


public class App
{
    public static void main( String[] args )
    {
        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(new ConsumerVerticle("V1"));
        vertx.deployVerticle(new ConsumerVerticle("V2"));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        vertx.deployVerticle(new HttpVerticle());

    }
}
