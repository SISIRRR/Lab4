package com.company;

public class Ship extends Thread {
    int containersToTake;
    int containersToLeave;
    Port port;

    int currentContainersQty;

    public Ship(String name, int containersToTake, int containersToLeave, Port port) {
        super(name);
        this.containersToTake = containersToTake;
        this.containersToLeave = containersToLeave;
        this.port = port;
        currentContainersQty = containersToLeave;
        start();
    }

    @Override
    public void run() {
        boolean isChanged = false;

        try {

            while (true) {
                if (!isChanged) {
                    port.askPermission();
                }

                isChanged = false;
                if (containersToLeave != 0 && containersToTake != 0) {
                    containersToTake--;
                    containersToLeave--;
                    isChanged = true;
                } else {
                    if (containersToLeave != 0  ){
                        synchronized (port) {
                            if (port.getContainersCapacity() > port.getCurrentContainersQty()) {
                                port.addContainer();
                                containersToLeave--;
                                isChanged = true;
                            }
                        }
                    } else {
                        if (containersToTake != 0 ){
                            synchronized (port) {
                                if (port.getCurrentContainersQty() > 0) {
                                    port.takeConatiner();
                                    containersToTake--;
                                    isChanged = true;
                                }
                            }
                        } else {
                            System.out.println(Thread.currentThread().getName() + " has finished his task");
                            port.returnPermission();
                            break;
                        }
                    }
                }

                if (isChanged){
                    Thread.sleep(10);
                } else {
                    port.returnPermission();
                }
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }

}