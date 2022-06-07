package report;

import database.Login;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ��������Ҫ���þ�����������http����
 */
@Deprecated
public class HTTPRequest {
    private StringBuffer httpContent;
    private ReferenceQueue referenceQueue;
    private StringBuilder header;
    private String requestLine/* ������ */, requestURI/* �������� */, httpProtocol/* httpЭ�� */;
    private String keyWord;//�������е�GET֮��Ĺؼ���
    private String body;
    private String fileName;
    private Login login;

    public HTTPRequest(StringBuffer httpContent) {
    }

    public HTTPRequest(StringBuffer httpContent, ReferenceQueue referenceQueue) {
        login = new Login();
        this.httpContent = httpContent;
        this.referenceQueue = referenceQueue;
    }

    /**
     * ��ȡHttp�������ݣ����ҳ��Խ���http����
     */
    public void flush() {
        String content = httpContent.toString();
        SoftReference softReference = new SoftReference<>(content, referenceQueue);
//        Objects.requireNonNull(content);
        String[] headerAndBody = content.split("01");
        parseHttpContent(headerAndBody[0]);//����http��������
        if (headerAndBody.length >= 2) {
            body = headerAndBody[1];
        } else body = null;
        content = null;
    }

    /**
     * ����Http�������ݣ����һ�ȡ��������
     */
    private void parseHttpContent(String httpContent) {
        // ���ƥ�������ǣ���ƥ��һ����д��ĸ��Ȼ��ƥ�����������������ַ������ƥ��һ�������е��ַ���
        String regex = "([A-Z]{1}.{0,}(\\r\\n){1})";//�������׳�������ʱʹ��\\n��ʵװ��ʱ���Ϊ\\r\\n
        Pattern compile = Pattern.compile(regex);
        Matcher matcher = compile.matcher(httpContent);
        if (matcher.find()) {
            parseRequestLine(matcher.group(1));//����������
        }
        parseHttpAttribute(matcher);//������������
    }

    /**
     * ��requestline���н�������ȡ���е�GET��POST�ؼ��֣��������ӣ�����httpЭ��
     *
     * @param requestLine
     */
    public void parseRequestLine(String requestLine) {
        header = new StringBuilder();
        SoftReference softReference = new SoftReference(header, referenceQueue);
        header.append(requestLine);
//        this.requestLine = requestLine;
        int firstIndexOf = requestLine.indexOf(" ");
        int lastIndexOf = requestLine.lastIndexOf(" ");
        requestURI = requestLine.substring(firstIndexOf + 1, lastIndexOf);
        requestURI = parseFileName(requestURI);
        parseLoginAttr(requestURI);
        keyWord = requestLine.substring(0, firstIndexOf);
        httpProtocol = requestLine.substring(lastIndexOf);
//        header = null;
    }

    /**
     * ��ȡ���ĵ���������
     *
     * @param matcher
     */
    public void parseHttpAttribute(Matcher matcher) {
        while (matcher.find()) {
            String group = matcher.group();
            String[] keyAndValue = group.split("(:\\s{1})");
            String key = keyAndValue[0];
            if (keyAndValue.length >= 2) {
                String value = keyAndValue[1];
                setKeyAndValue(key, value);
            } else {
                setKeyAndValue(key, "\n");
            }
        }
    }

    /**
     * ��ȡ���������е��ļ���
     *
     * @param requestURI
     * @return
     */
    private String parseFileName(String requestURI) {
        if (requestURI.contains("?")) {
            String[] values = requestURI.split("\\?");
            fileName = values[0];
            return values[1];
        }
        if (requestURI.equals("/")) {
            fileName = "index.html";
        }
        return requestURI;
    }

    /**
     * ���Ϊ��¼������������е��˺�����
     *
     * @param requestURI
     */
    public void parseLoginAttr(String requestURI) {
//        ([A-Za-z0-9]+\.(ht(?:ml|m)|(ico)))|
//        String regex = "([A-Za-z]{0,}=[A-Za-z0-9]{0,})";
//        Pattern compile = Pattern.compile(regex);
//        Matcher matcher = compile.matcher(requestURI);
//        while (matcher.find()) {
//            String part = matcher.group();
        if (requestURI.contains("=")) {
            String[] keyAndValue = requestURI.split("=");
            login.setAccount(keyAndValue[0]);
            if (keyAndValue.length >= 2) {
                login.setPassword(keyAndValue[1]);
                boolean isLogin = login.requireLogin();
                if (isLogin)
                    fileName = login.getFileName();
                else login.requireRegister();
            }
//            } else {
//                fileName = part;
//            }
        }
    }

    private void setKeyAndValue(String key, String value) {
        header.append(key).append(": ").append(value);
    }

    public String getRequestURI() {
        return requestURI;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public String getBody() {
        return body;
    }

    public String getFileName() {
        return fileName;
    }
}
