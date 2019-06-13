package com.askar.port.main;

import com.askar.port.entity.Delivery;
import com.askar.port.entity.Port;
import com.askar.port.entity.ServiceType;
import com.askar.port.entity.Ship;
import com.askar.port.controller.DeliveryController;
import com.askar.port.controller.LoadController;

import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {

        Port port = new Port("Askar",1300);
        LoadController loadController = new LoadController(port);
        Semaphore semaphore = new Semaphore(2);
        DeliveryController deliveryController = new DeliveryController(port);


        new Thread(new Delivery(port,loadController,deliveryController)).start();
        new Thread(new Ship(300,String.valueOf(1), ServiceType.DELIVERY,1000,port,loadController,deliveryController,semaphore)).start();
//        new Thread(new Ship(100,String.valueOf(2), ServiceType.LOAD,1000,port,loadController,deliveryController,semaphore)).start();
//        new Thread(new Ship(1200,String.valueOf(3), ServiceType.DELIVERY,1000,port,loadController,deliveryController,semaphore)).start();
//        new Thread(new Ship(150,String.valueOf(4), ServiceType.LOAD,1000,port,loadController,deliveryController,semaphore)).start();
//        new Thread(new Ship(1050,String.valueOf(5), ServiceType.DELIVERY,1000,port,loadController,deliveryController,semaphore)).start();
//        new Thread(new Ship(550,String.valueOf(6), ServiceType.DELIVERY,1000,port,loadController,deliveryController,semaphore)).start();
//        new Thread(new Ship(660,String.valueOf(7), ServiceType.DELIVERY,1000,port,loadController,deliveryController,semaphore)).start();
//        new Thread(new Ship(430,String.valueOf(8), ServiceType.DELIVERY,1000,port,loadController,deliveryController,semaphore)).start();
//        new Thread(new Ship(420,String.valueOf(9), ServiceType.DELIVERY,1000,port,loadController,deliveryController,semaphore)).start();



    }
}
