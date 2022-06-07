package report.util.builder;

import report.util.content.RequestHeader;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * The builder of {@code RequestHeader} class and responsible for praising the http telegram head.
 *
 * @see #parseHttpHead(String, String)
 */
public class RequestHeaderBuilder {
    private int count;
    private RequestHeader requestHeader;
    private Class<? extends RequestHeader> clazz;

    public RequestHeaderBuilder(ReferenceQueue referenceQueue) {
        requestHeader = new RequestHeader();
        SoftReference softReference = new SoftReference(requestHeader, referenceQueue);
        clazz = requestHeader.getClass();
    }

    //Ŀǰ���������Ʋ������ڶ�ͻ�������ʱhttp���ĵ����ݴ���
    public void flush() {
        count = 0;
    }

    private String replaceSymbol(String item) {
        return item.replace('-', '_');
    }

    public void parseHttpHead(String key, String value) {
        if (key.contains("-")) {
            key = replaceSymbol(key);
        }
        try {
            Method declaredMethod = clazz.getDeclaredMethod(key, String.class);
            declaredMethod.invoke(requestHeader, value);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void saveRequestLine(String key, String uri, String protocol) {
        requestHeader.setKey(key);
        requestHeader.setUri(uri);
        requestHeader.setProtocol(protocol);
    }

    public void saveFileName(String fileName) {
        requestHeader.setFileName(fileName);
    }

    public RequestHeader build() {
        count++;
        return requestHeader;
    }
}
