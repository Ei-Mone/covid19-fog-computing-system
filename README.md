# COVID-19 Monitoring System with Fog Computing

## Short description of the project

In traditional cloud computing systems, data is sent to a central cloud server where all processing and decision-making take place. The results are then sent back to the devices. This process can introduce delays due to network latency and the long distance between devices and the cloud.

Our project takes advantage of this limitation by exploring a more efficient computing approach called fog computing. In fog computing, fog nodes are placed closer to IoT devices, allowing data to be processed locally without always communicating with the central cloud. This reduces response time and improves system performance.

To further increase efficiency, we integrate AI models into each fog node for real-time decision making. This is especially useful in monitoring systems where rapid responses are critical.

As a practical example, we developed a COVID-19 monitoring system. During the pandemic, detecting people who are not wearing masks requires immediate action to help maintain public safety. By using fog computing with AI at the edge, the system can quickly identify violations and generate alerts with minimal delay.

Another important part of our project is the installation and integration of Apache Hadoop on Ubuntu Linux to create a distributed system environment.

Whenever a fog node receives data from an IoT device, the detected data is automatically stored in the corresponding fog node storage within the Hadoop distributed system. This allows data to be managed efficiently across multiple nodes while supporting scalability and reliability.

To make the monitoring system easily accessible, we also developed an HTTP web server on Ubuntu Linux. Through this server, users can view monitoring results and system data directly from their mobile phones in real time.

By combining fog computing, AI-based real-time detection, Hadoop distributed storage, and web-based monitoring, the project demonstrates a complete distributed system architecture for efficient and responsive IoT applications.

## Features

- IoT device servers continuously collect and transmit monitoring data.

- Fog nodes receive data from the nearest IoT devices for low-latency processing.

- AI models deployed on fog nodes perform real-time face mask detection.

- Each fog node stores detection results as json files and transfers them to Hadoop distributed storage on Ubuntu Linux.

- Administrators can publish and manage a web server on Ubuntu Linux for monitoring purposes.

- Users within the IoT monitoring area can view real-time mask violation statistics.

## Technologies Used

- Python

- Ubuntu Linux Desktop

- Java RMI (Remote Method Invocation)

- HTTP Web Server

- Hadoop Distributed Storage

- Fog Computing

- IoT Devices

- AI-based Mask Detection

## How to Run

1. Run `FogService.java` to start the fog computing system.

2. Run `IoTClient.java` to start the IoT device. The video stream for mask detection will be sent to the nearest fog node.

3. Run the fog node servers:
   - `FogServer1.java`
   - `FogServer2.java`
   - `FogServer3.java`

4. Install Hadoop and Python in Ubuntu Linux.

5. Start the HTTP web server using the following command:

```bash
python3 -m http.server 8000
```

6. Open the web server address on a mobile phone browser to monitor mask violation results in real time.

## Project Structure

- `FogService.java`  
  Handles connections between fog servers and IoT devices.

- `IoTClient.java`  
  Acts as an IoT device and sends monitoring data to fog servers.

- `FogServer.java`  
  Processes data received from IoT devices and transfers the results to Hadoop distributed storage.

- `detect.py`  
  Performs AI-based face mask detection using video input.

## Future Improvements

- Develop a dedicated software application instead of using a basic web server interface.
- Integrate live camera streams instead of pre-recorded video files for real-time monitoring.
- Expand the system by adding more fog nodes to improve scalability and performance.
- Implement more advanced and intelligent AI models for higher detection accuracy and faster processing.
