package report;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

public class HTTPFactory {
    public static InetAddress home, school;
    public static InetSocketAddress socketAddressHome, socketAddressSchool;

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
}
