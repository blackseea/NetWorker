package report.require;

import database.Login;
import report.util.builder.RequestHeaderBuilder;
import report.util.content.RequestHeader;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

/**
 * 作为HttpRequest的父类，主要作用就是用来保存解析出来的数据。
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
        parseHttpContent(headerAndBody[0]);//解析http报文内容
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
