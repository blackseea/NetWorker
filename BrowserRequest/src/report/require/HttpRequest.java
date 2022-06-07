package report.require;

import database.Login;
import report.util.builder.RequestHeaderBuilder;

import java.lang.ref.ReferenceQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ��ΪAbsHttpRequest�����࣬��Ҫ���þ���������������
 */
public class HttpRequest extends AbsHttpRequest {
    protected RequestHeaderBuilder builder;
    private boolean hasLoginParams;

    public HttpRequest(StringBuffer httpContent, ReferenceQueue referenceQueue) {
        super(httpContent, referenceQueue);
        builder = new RequestHeaderBuilder(referenceQueue);
        login = new Login();
    }

    /**
     * Parsing the requisition of the http telegram and create a http telegram in the end of progression.
     *
     * @param httpContent
     */
    @Override
    protected void parseHttpContent(String httpContent) {
        // ���ƥ�������ǣ���ƥ��һ����д��ĸ��Ȼ��ƥ�����������������ַ������ƥ��һ�������е��ַ���
        String regex = "([A-Z]{1}.{0,}(\\r\\n){1})";//�������׳�������ʱʹ��\\n��ʵװ��ʱ���Ϊ\\r\\n
        Pattern compile = Pattern.compile(regex);
        Matcher matcher = compile.matcher(httpContent);
        if (matcher.find()) {
            String group = matcher.group(1);
            String requestURI = parseRequestLine(group);//����������
            String loginParamter = parseFileName(requestURI);
            if (hasLoginParams) parseLoginAttr(loginParamter);
        }
        parseHttpAttribute(matcher);//������������
        requestHeader = builder.build();
        builder.flush();
    }

    @Override
    protected void parseHttpBody(String httpBody) {

    }

    private String parseRequestLine(String requestLine) {
        int firstIndexOf = requestLine.indexOf(" ");
        int lastIndexOf = requestLine.lastIndexOf(" ");
        String requestURI = requestLine.substring(firstIndexOf + 1, lastIndexOf);//�����������
        String keyWord = requestLine.substring(0, firstIndexOf);
        String httpProtocol = requestLine.substring(lastIndexOf);
        builder.saveRequestLine(keyWord, requestURI, httpProtocol);
        return requestURI;
    }

    private void parseHttpAttribute(Matcher matcher) {
        while (matcher.find()) {
            String group = matcher.group();
            String[] keyAndValue = group.split("(:\\s{1})");
            String key = keyAndValue[0];
            if (keyAndValue.length >= 2) {
                String value = keyAndValue[1];
                builder.parseHttpHead(key, value);
//                setKeyAndValue(key, value);
            } else {
//                setKeyAndValue(key, "\n");
            }
        }
    }

    /**
     * @param requestURI
     * @return
     */
    private String parseFileName(String requestURI) {
        if (requestURI.contains("?")) {
            String[] values = requestURI.split("\\?");
            builder.saveFileName(values[0]);
            hasLoginParams = true;
            return values[1];//return login paramter
        }
        if (requestURI.equals("/")) {
            builder.saveFileName("index.html");
        } else {
            builder.saveFileName(requestURI);
        }
        hasLoginParams = false;
        return null;
    }

    private void parseLoginAttr(String requestURI) {
//        if (requestURI.contains("=")) {
        String[] keyAndValue = requestURI.split("=");
        login.setAccount(keyAndValue[0]);
        if (keyAndValue.length >= 2) {
            login.setPassword(keyAndValue[1]);
            isLogin = login.requireLogin();
            builder.saveFileName(login.getFileName());
//                fileName = login.getFileName();
        }
//        }
    }

    private void requireRegister() {
        String referer = requestHeader.getReferer();
    }


}
