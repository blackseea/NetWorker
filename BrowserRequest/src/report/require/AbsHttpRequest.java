package report.require;

import database.Login;
import report.util.builder.RequestHeaderBuilder;
import report.util.content.RequestHeader;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

/**
 * ��ΪHttpRequest�ĸ��࣬��Ҫ���þ�����������������������ݡ�
 */
public abstract class AbsHttpRequest {
    protected ReferenceQueue referenceQueue;
    protected RequestHeader requestHeader;
    protected Login login;
    protected boolean isLogin;
    private StringBuffer httpContent;

    protected AbsHttpRequest(StringBuffer httpContent, ReferenceQueue referenceQueue) {
        this.httpContent = httpContent;
        this.referenceQueue = referenceQueue;
    }

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void flush() {
        String content = httpContent.toString();
        SoftReference softReference = new SoftReference<>(content, referenceQueue);
//        Objects.requireNonNull(content);
        String[] headerAndBody = content.split("01");
        parseHttpContent(headerAndBody[0]);//����http��������
        if (headerAndBody.length >= 2) {
//            body = headerAndBody[1];
        } else {
        }
//            body = null;
        System.out.println("---------------------------");
        System.out.println(requestHeader.toString());
        System.out.println("---------------------------");
        content = null;
    }

    protected abstract void parseHttpContent(String httpContent);
    protected abstract void parseHttpBody(String httpBody);

}
