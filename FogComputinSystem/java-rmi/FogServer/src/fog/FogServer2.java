package fog;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileWriter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import common.FogService;

public class FogServer2 extends UnicastRemoteObject implements FogService {

    private static final long serialVersionUID = 1L;

    protected FogServer2() throws RemoteException {
        super();
    }

    // ---------------- JSON VALUE EXTRACT ----------------
    private int extractInt(String json, String key) {
        try {
            String searchKey = "\"" + key + "\":";
            int start = json.indexOf(searchKey) + searchKey.length();
            int end = json.indexOf(",", start);

            if (end == -1)
                end = json.indexOf("}", start);

            return Integer.parseInt(json.substring(start, end).trim());

        } catch (Exception e) {
            return 0;
        }
    }

    // ---------------- SAVE TO HDFS ----------------
    private void saveViolationToHDFS(String jsonOutput) {

        String friendUser = "eimone";          // CHANGE
        String friendIp = "192.168.1.8";      // CHANGE

        try {

            String localDir = "C:\\temp\\fog_logs";
            new File(localDir).mkdirs();

            String timestamp =
                    LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            String fileName = "FogNode2_" + timestamp + ".json";

            File localFile =
                    new File(localDir + "\\" + fileName);

            FileWriter writer = new FileWriter(localFile);
            writer.write(jsonOutput);
            writer.close();

            System.out.println("Local JSON created");

            // ---------- SCP COPY ----------
            ProcessBuilder scp = new ProcessBuilder(
                    "scp",
                    localFile.getAbsolutePath(),
                    friendUser + "@" + friendIp +
                    ":/home/" + friendUser + "/" + fileName
            );

            scp.inheritIO();
            Process scpProcess = scp.start();
            scpProcess.waitFor();

            System.out.println("Copied to Ubuntu");

            // ---------- HDFS PUT ----------
            String hdfsCmd =
            		"/home/eimone/hadoop/bin/hdfs dfs -mkdir -p /covid_data/FogNode2 && " +
            		"/home/eimone/hadoop/bin/hdfs dfs -put -f /home/" +
            		friendUser + "/" + fileName +
            		" /covid_data/FogNode2/";
            ProcessBuilder ssh = new ProcessBuilder(
                    "ssh",
                    friendUser + "@" + friendIp,
                    hdfsCmd
            );

            ssh.inheritIO();
            Process sshProcess = ssh.start();
            sshProcess.waitFor();

            System.out.println("Saved to HDFS SUCCESS");

        } catch (Exception e) {
            System.out.println("HDFS ERROR: " + e.getMessage());
        }
    }

    // ---------------- PROCESS VIDEO ----------------
    @Override
    public String processVideo(String videoName)
            throws RemoteException {

        try {

            String pythonPath = "python";

            String scriptPath =
            "C:\\Users\\Kaung Myat Htut\\Desktop\\FogComputinSystem\\python-ai\\detect.py";

            ProcessBuilder pb =
                    new ProcessBuilder(
                            pythonPath,
                            scriptPath,
                            videoName
                    );
            pb.directory(new File("C:\\Users\\Kaung Myat Htut\\Desktop\\FogComputinSystem\\python-ai"));
            pb.redirectErrorStream(true);

            Process process = pb.start();

            BufferedReader reader =
                    new BufferedReader(
                    new InputStreamReader(
                    process.getInputStream()));

            String line;
            String output = "";

            while ((line = reader.readLine()) != null) {
                output += line;
            }
            process.waitFor();

            System.out.println("Python Output:");
            System.out.println(output);

            int people =
                    extractInt(output, "people");

            int noMask =
                    extractInt(output, "no_mask");

            // ---------- SAVE IF VIOLATION ----------
            if (noMask > 0) {
                saveViolationToHDFS(output);
            }

            return "Fog Node 1\nPeople: "
                    + people +
                    "\nNo Mask: " + noMask;

        } catch (Exception e) {
            return "FogNode1 Error: "
                    + e.getMessage();
        }
    }

    // ---------------- MAIN ----------------
    public static void main(String[] args) {

        try {

            LocateRegistry.createRegistry(2001);

            FogServer2 server =
                    new FogServer2();

            Naming.rebind(
                    "rmi://localhost:2001/FogService",
                    server);

            System.out.println(
                    "Fog Node 2 running on port 2001...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}