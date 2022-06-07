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
                //length���ܳ���-1��ԭ������Ϊ��һ������ʱ���ø÷�����ȡHTTP���ģ�Ȼ�������shutdownInput��
                // ����tcp���Ӳ�δ�Ͽ�������˵ڶ��ε��ø÷�����ȡHTTP���ģ������Ѿ�shutdownInput�ˣ�����ֱ�ӷ���-1��
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

