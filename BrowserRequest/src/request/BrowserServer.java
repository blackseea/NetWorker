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
 * ���������ServerSocket�����ķ�����
 * <p>
 * ��������ĵ�ַ��������ip��ַ���������������ͽ����׽������ӣ�
 * ���ӳɹ��������ҳ��һֱ�ȴ����������ص����ݣ��������������Ȼ˵���յ���������������ģ�
 * ��whileѭ����֮��Ͷ����ˣ�������������ֻ���ڹر������ҳ��֮��Ż�ִ�С�
 * <p>
 * ������������������ʹ�ǳ����Ӻ�Ҳֻ��ά��һ�ε�����ʱ�䣬֮���ֶϿ��ˡ�
 * ��ҳ������ҳ����ֻ��������������
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
     * ��̨�豸ͬʱ���Ӳ��Է���һЩ����
     * ����̨�豸A��B������վʱ���������̨�豸ͬʱ����ͬһ�����棬
     * ������һ���ᷢ������������һ��������һ������ʱ��
     * ֮ǰ���������豸���ܹ���������Ľ�����
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
     * ֮ǰʹ�õ���io����read�������������������䲻ִ�С�
     * �����ʹ��һ��readline������ͬ�������������䲻ִ�С�
     * �ͻ��˺ͷ���˵��׽������Ӻ�io����read�����������׵�������ĩβ��
     * ��Ϊ˫����ȷ���Է��������Ƿ�����ϣ���ˣ�read������whileѭ���л�һֱ������
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
                httpContent.append("��������");
                break;
            }
            httpContent.append(line);//����ط�������ջ����쳣

        }
        System.out.println(httpContent.toString());
        httpRequest.flush();
    }

    /**
     * Using the {@code BufferedReader} class {@code read(char[])} method to reaceive the http telegram.
     * This telegram is saved by the {@code RequestHeader} class.
     * @param socket
     * @return ����һ�����Σ������ж��Ƿ�����ж�ȡ�������ݡ�
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
                //length���ܳ���-1��ԭ������Ϊ��һ������ʱ���ø÷�����ȡHTTP���ģ�Ȼ�������shutdownInput��
                // ����tcp���Ӳ�δ�Ͽ�������˵ڶ��ε��ø÷�����ȡHTTP���ģ������Ѿ�shutdownInput�ˣ�����ֱ�ӷ���-1��
                if (length < 0) return 0;
                if (length < oldLength) {
                    byte[] newBytes = new byte[length];
                    System.arraycopy(bytes, 0, newBytes, 0, length);
                    bytes = newBytes;
                }
                decode = new String(bytes);
                //�Ʋ��������decode���󣬼�ʹ����decode=null��gcҲ��������ڴ棬��Ϊ�����StringBuffer��decodeָ��Ķ��������append
                //ֻ�е�HttpRequest���ڲ���StringBuffer���������ݽ�������󣬲��ܻ��ա�
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
                //length���ܳ���-1��ԭ������Ϊ��һ������ʱ���ø÷�����ȡHTTP���ģ�Ȼ�������shutdownInput��
                // ����tcp���Ӳ�δ�Ͽ�������˵ڶ��ε��ø÷�����ȡHTTP���ģ������Ѿ�shutdownInput�ˣ�����ֱ�ӷ���-1��
                if (length < 0) return 0;
                if (length < oldLength) {
                    char[] newChars = new char[length];
                    System.arraycopy(chars, 0, newChars, 0, length);
                    chars = newChars;
                }
                decode = chars.toString();
                //�Ʋ��������decode���󣬼�ʹ����decode=null��gcҲ��������ڴ棬��Ϊ�����StringBuffer��decodeָ��Ķ��������append
                //ֻ�е�HttpRequest���ڲ���StringBuffer���������ݽ�������󣬲��ܻ��ա�
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
