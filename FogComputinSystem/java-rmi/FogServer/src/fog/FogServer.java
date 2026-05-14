package fog;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import common.FogService;

public class FogServer extends UnicastRemoteObject implements FogService {

    protected FogServer() throws RemoteException {
        super();
    }
    private int extractInt(String json, String key) {
        try {
            String searchKey = "\"" + key + "\":";
            int start = json.indexOf(searchKey) + searchKey.length();
            int end = json.indexOf(",", start);

            if (end == -1) {
                end = json.indexOf("}", start);
            }

            return Integer.parseInt(json.substring(start, end).trim());
        } catch (Exception e) {
            return 0;
        }
    }
    @Override
    public String processVideo(String videoName) throws RemoteException {

        try {
            String pythonPath = "python";
            String scriptPath = "C:\\Users\\Kaung Myat Htut\\Desktop\\FogComputinSystem\\python-ai\\detect.py";

            ProcessBuilder pb = new ProcessBuilder(pythonPath, scriptPath, videoName);
            pb.redirectErrorStream(true);

            Process process = pb.start();

            java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.InputStreamReader(process.getInputStream())
            );

            String line;
            String jsonOutput = "";

            while ((line = reader.readLine()) != null) {
                jsonOutput += line;
            }

            process.waitFor();

            // Extract values from JSON (simple parsing)
            int people = extractInt(jsonOutput, "people");
            int noMask = extractInt(jsonOutput, "no_mask");
            int distanceViolation = extractInt(jsonOutput, "distance_violation");

            

            // Return formatted result
            String result =
                    "Fog Node Report\n" +
                    "Video: " + videoName + "\n" +
                    "People detected: " + people + "\n" +
                    "No-mask detected: " + noMask + "\n" +
                    "Distance violations: " + distanceViolation + "\n" ;
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error running AI model: " + e.getMessage();
        }
    }

    public static void main(String[] args) {
        try {
            java.rmi.registry.LocateRegistry.createRegistry(1099);

            FogServer server = new FogServer();
            Naming.rebind("FogService", server);

            System.out.println("Fog Node is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}