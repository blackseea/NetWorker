package report;

import java.io.IOException;
import java.net.*;

public class HttpAddress {
    public static InetAddress home, school;
    public static InetSocketAddress socketAddressHome, socketAddressSchool;

    private static int[] ports = {8080, 8081, 8082, 8083, 8084};

    static {
        try {
            home = InetAddress.getByName("192.168.0.125");
            school = InetAddress.getByName("192.168.0.112");
            socketAddressHome = new InetSocketAddress(home, 8080);
            socketAddressSchool = new InetSocketAddress(school, 8090);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static ServerSocket getAvailableHost(InetAddress host) {
        ServerSocket socket =null;
        int[] availablePorts = new int[5];
        try {
            for (int i = 0; i < ports.length; i++) {
                int port = ports[i];
                socket = new ServerSocket(port);
                availablePorts[i]= port;
            }


        } catch (IOException e) {
                e.printStackTrace();
            return null;
        }
        printAvailablePort(socket);
        return socket;
    }

    public static void printAvailablePort(ServerSocket serverSocket){
        System.out.println(serverSocket.getLocalPort());
    }
}
