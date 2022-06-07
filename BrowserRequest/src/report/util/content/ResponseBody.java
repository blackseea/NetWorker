package report.util.content;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.nio.charset.StandardCharsets;

public class ResponseBody {
    private ReferenceQueue referenceQueue;
    private StringBuilder body = new StringBuilder();

    public ResponseBody(ReferenceQueue referenceQueue) {
        this.referenceQueue = referenceQueue;
        SoftReference softReference =new SoftReference(body,referenceQueue);
    }

    public void readFile(File file){
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                body.append(line);
                body.append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFile2(File file){
        char[] chars = new char[1024];
//        String decode = null;
        int length = 0;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            while (true) {
                int oldLength = chars.length;
                length = bufferedReader.read(chars);
                //length可能出现-1的原因，是因为第一次连接时调用该方法读取HTTP报文，然后进行了shutdownInput，
                // 但是tcp连接并未断开，服务端第二次调用该方法读取HTTP报文，由于已经shutdownInput了，所以直接返回-1。
                if (length < 0) return;
                if (length < oldLength) {
                    char[] newChars = new char[length];
                    System.arraycopy(chars, 0, newChars, 0, length);
                    chars = newChars;
                }

                body.append(chars);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void readContent(String content){
        body.append(content);
    }

    @Override
    public String toString() {
        return body.toString();
    }

    public void clean() {
        body.delete(0,body.length());
    }
}

