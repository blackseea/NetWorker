package request;

import report.HTTPRequest;

import java.io.*;
import java.lang.ref.ReferenceQueue;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Set;

import static report.HTTPFactory.socketAddressSchool;

/**
 * 尝试使用Selecto和Channel来建立非阻塞服务器
 */
public class MultiLoadChannel {
//    private static HTTPResponse httpResponse;
    private static HTTPRequest httpRequest;
    private static boolean isRead = true;
    private static ReferenceQueue referenceQueue =new ReferenceQueue();

    public static void main(String[] args) {
//        httpResponse=new HTTPResponse(httpRequest, referenceQueue);
//        httpRequest=new HTTPRequest();
        init();
    }

    private static void init() {
        try {
            Selector selector = Selector.open();

            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(socketAddressSchool);
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            Socket s = null;
            CharBuffer charBuffer = null;
            StringBuilder stringBuilder = new StringBuilder();
            while (selector.select()>0) {
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for (SelectionKey selectionKey : selectionKeys) {
                    selectionKeys.remove(selectionKey);
                    if (selectionKey.isAcceptable()) {
                        ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel socketChannel = channel.accept();
                        Objects.requireNonNull(socketChannel);
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    }
                    if (selectionKey.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        Objects.requireNonNull(socketChannel,"未连接");
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        try {
                            while (true) {
                                if (socketChannel.read(byteBuffer) == 0) {
                                    String splits = "01";
                                    byteBuffer.put(Byte.parseByte(splits));
                                    break;
                                } else {
                                    charBuffer = StandardCharsets.UTF_8.decode(byteBuffer);
                                }
                                stringBuilder.append(charBuffer);
                                byteBuffer.flip();
                            }
//                            httpRequest.setContent(stringBuilder.toString());
                        } catch (IOException e) {
//                            selectionKey.cancel();
//                            if (selectionKey.channel() != null) {
//                                selectionKey.channel().close();
                        }
                    }
                }
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void receive(Socket socket) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        StringBuffer sb = new StringBuffer();
        String line;
        while (isRead) {
            line = br.readLine() + "\r\n";
            if (line.equals("\r\n")) {
                socket.shutdownInput();
                sb.append("。。。。");
                break;
            }
            sb.append(line);

        }
        System.out.println(sb.toString());
//        httpRequest.setContent(sb.toString());
    }

    private static void response(Socket socket) {
//        httpResponse.flush();
        try (BufferedWriter bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream(), Charset.forName("utf-8")));
        ) {
//            bufferedWriter.write(httpResponse.toString());
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
