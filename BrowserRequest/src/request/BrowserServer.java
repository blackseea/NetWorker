package request;

import report.HTTPFactory;
import report.require.AbsHttpRequest;
import report.require.HttpRequest;
import report.response.AbsHttpResponse;
import report.response.HttpResponse;

import java.io.*;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 浏览器连接ServerSocket建立的服务器
 * <p>
 * 在浏览器的地址栏中输入ip地址后，浏览器与服务器就进行套接字连接，
 * 连接成功后浏览器页面一直等待服务器返回的数据，而服务器这边虽然说接收到了浏览器的请求报文，
 * 但while循环完之后就堵塞了，后续的输出语句只有在关闭浏览器页面之后才会执行。
 * <p>
 * 发现浏览器与服务器即使是长连接后也只是维持一段的连接时间，之后又断开了。
 * 网页请求新页面后，又会与服务器连接上
 */
public class BrowserServer {
    private static AbsHttpRequest httpRequest;
    private static AbsHttpResponse httpResponse;
    private static ReferenceQueue referenceQueue;
    //    private static HTTPResponse httpResponse;
//    private static HTTPRequest httpRequest;
    private static StringBuffer httpContent;

    private static boolean isRead = true;

    public static void main(String[] args) {
        referenceQueue = new ReferenceQueue();
        httpContent = new StringBuffer();

        httpRequest = new HttpRequest(httpContent, referenceQueue);
//        httpRequest = new HTTPRequest(httpContent, referenceQueue);
//        httpResponse = new HTTPResponse(httpRequest, referenceQueue);
        httpResponse = new HttpResponse(httpRequest, referenceQueue);

        inti();
//        HttpAddress.getAvailableHost(null);
//        test();
    }

    /**
     * 多台设备同时连接测试发现一些问题
     * 当多台设备A、B连接网站时，如果这两台设备同时请求同一个界面，
     * 则其中一个会发生堵塞，当另一个请求另一个界面时，
     * 之前被堵塞的设备就能够请求到所需的界面了
     */
    private static void inti() {
        try {
            ServerSocket serverSocket = new ServerSocket(8080, 0, HTTPFactory.school);

            Socket s = null, preSocket = null;
            while (true) {
                s = serverSocket.accept();

                if (s.isConnected()) {
                    System.out.println("Browser had connected");
//                    receive(s);
                    int state = receive2(s);
                    httpRequest.flush();

                    if (state > 0) {
                        httpResponse.flush();
                        response(s);
                        httpResponse.clean();

                    }

                } else {
                    System.out.println("Browser had not connected");
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 之前使用的是io流的read方法，但是最后的输出语句不执行。
     * 这次来使用一下readline方法，同样，最后的输出语句不执行。
     * 客户端和服务端的套接字连接后，io流的read方法不会轻易到达流的末尾，
     * 因为双方不确定对方的数据是否发送完毕，因此，read方法在while循环中会一直阻塞。
     *
     * @param socket
     * @throws IOException
     */
    private static void receive(Socket socket) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        httpContent.delete(0, httpContent.length());
        String line;
        while (isRead) {
            line = br.readLine() + "\r\n";
            if (line.equals("\r\n")) {
                socket.shutdownInput();
                httpContent.append("。。。。");
                break;
            }
            httpContent.append(line);//这个地方经常爆栈溢出异常

        }
        System.out.println(httpContent.toString());
        httpRequest.flush();
    }

    /**
     * Using the {@code BufferedReader} class {@code read(char[])} method to reaceive the http telegram.
     * This telegram is saved by the {@code RequestHeader} class.
     * @param socket
     * @return 返回一个整形，用来判断是否从流中读取到了数据。
     * @see BufferedReader#read(char[])
     * @see report.util.content.RequestHeader
     */
    private static int receive2(Socket socket) {
        try {
            httpContent.delete(0, httpContent.length());
            BufferedInputStream bufferedInputStream = new BufferedInputStream(socket.getInputStream());
            byte[] bytes = new byte[512];
            String decode = null;
            int length = 0;
            loop:
            while (isRead) {
                int oldLength = bytes.length;
                length = bufferedInputStream.read(bytes);
                //length可能出现-1的原因，是因为第一次连接时调用该方法读取HTTP报文，然后进行了shutdownInput，
                // 但是tcp连接并未断开，服务端第二次调用该方法读取HTTP报文，由于已经shutdownInput了，所以直接返回-1。
                if (length < 0) return 0;
                if (length < oldLength) {
                    byte[] newBytes = new byte[length];
                    System.arraycopy(bytes, 0, newBytes, 0, length);
                    bytes = newBytes;
                }
                decode = new String(bytes);
                //推测在这里的decode对象，即使设置decode=null后，gc也不会回收内存，因为下面的StringBuffer对decode指向的对象进行了append
                //只有当HttpRequest类内部的StringBuffer对自身内容进行清除后，才能回收。
                SoftReference<String> softReference = new SoftReference<>(decode, referenceQueue);
                httpContent.append(decode);
//                System.out.println(decode.toString());
                if (decode.contains("\r\n\r\n")) {
                    socket.shutdownInput();
                    break;
                }
            }

//            System.out.println(httpContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }

    private static int receive3(Socket socket) {
        try {
            httpContent.delete(0, httpContent.length());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            char[] chars = new char[512];
            String decode = null;
            int length;
            loop:
            while (isRead) {
                int oldLength = chars.length;
                length = bufferedReader.read(chars);
                //length可能出现-1的原因，是因为第一次连接时调用该方法读取HTTP报文，然后进行了shutdownInput，
                // 但是tcp连接并未断开，服务端第二次调用该方法读取HTTP报文，由于已经shutdownInput了，所以直接返回-1。
                if (length < 0) return 0;
                if (length < oldLength) {
                    char[] newChars = new char[length];
                    System.arraycopy(chars, 0, newChars, 0, length);
                    chars = newChars;
                }
                decode = chars.toString();
                //推测在这里的decode对象，即使设置decode=null后，gc也不会回收内存，因为下面的StringBuffer对decode指向的对象进行了append
                //只有当HttpRequest类内部的StringBuffer对自身内容进行清除后，才能回收。
                SoftReference<String> softReference = new SoftReference<>(decode, referenceQueue);
                httpContent.append(decode);
//                System.out.println(decode.toString());
                if (decode.contains("\r\n\r\n")) {
                    socket.shutdownInput();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }

    private static void response(Socket socket) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream(), Charset.forName("utf-8")));
        ) {
            String httpRes;
            bufferedWriter.write((httpRes = httpResponse.toString()));
            bufferedWriter.flush();
            System.out.println(httpRes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
