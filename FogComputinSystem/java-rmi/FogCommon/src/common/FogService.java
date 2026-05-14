package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FogService extends Remote {

    String processVideo(String videoName) throws RemoteException;

}