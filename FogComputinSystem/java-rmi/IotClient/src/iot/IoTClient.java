package iot;

import java.rmi.Naming;
import common.FogService;

public class IoTClient {

    public static void main(String[] args) {
        try {
            FogService fog1 = (FogService) Naming.lookup("rmi://localhost:2000/FogService");
            FogService fog2 = (FogService) Naming.lookup("rmi://localhost:2001/FogService");
            FogService fog3 = (FogService) Naming.lookup("rmi://localhost:2002/FogService");

            String video = "park.mp4";
            String video1 = "distance.mp4";
            String video2 = "movie.mp4";
            

            Thread t1 = new Thread(() -> {
                try {
                    fog1.processVideo(video);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            Thread t2 = new Thread(() -> {
                try {
                    fog2.processVideo(video2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            Thread t3 = new Thread(() -> {
                try {
                    fog3.processVideo(video1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            // Start all at same time
            t1.start();
            t2.start();
            t3.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}