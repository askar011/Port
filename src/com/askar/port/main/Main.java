package com.askar.port.main;

import com.askar.port.entity.Delivery;
import com.askar.port.entity.Port;
import com.askar.port.entity.ServiceType;
import com.askar.port.entity.Ship;
import com.askar.port.service.DeliveryController;
import com.askar.port.service.LoadController;

import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {

        Port port = new Port("Askar",500);
        LoadController loadController = new LoadController(port);
        DeliveryController deliveryController = new DeliveryController(port);
        Semaphore semaphore = new Semaphore(2);


        new Thread(new Delivery(port,loadController,deliveryController)).start();
        new Thread(new Ship(1000,String.valueOf(1), ServiceType.DELIVERY,1000,port,loadController,deliveryController,semaphore)).start();
        new Thread(new Ship(300,String.valueOf(2), ServiceType.DELIVERY,1000,port,loadController,deliveryController,semaphore)).start();
//        new Thread(new Ship(500,String.valueOf(3), ServiceType.DELIVERY,1000,port,loadController,deliveryController,semaphore)).start();
//        new Thread(new Ship(350,String.valueOf(4), ServiceType.DELIVERY,1000,port,loadController,deliveryController,semaphore)).start();
//        new Thread(new Ship(450,String.valueOf(5), ServiceType.DELIVERY,1000,port,loadController,deliveryController,semaphore)).start();
//        new Thread(new Ship(550,String.valueOf(6), ServiceType.DELIVERY,1000,port,loadController,deliveryController,semaphore)).start();
//        new Thread(new Ship(660,String.valueOf(7), ServiceType.DELIVERY,1000,port,loadController,deliveryController,semaphore)).start();
//        new Thread(new Ship(430,String.valueOf(8), ServiceType.DELIVERY,1000,port,loadController,deliveryController,semaphore)).start();
        new Thread(new Ship(420,String.valueOf(9), ServiceType.DELIVERY,1000,port,loadController,deliveryController,semaphore)).start();



    }
}
