package report.util.content;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

/**
 *
 */
public class ResponseHeader {
    private ReferenceQueue referenceQueue;
    private StringBuilder header = new StringBuilder();
    private Cookie cookie;

    public ResponseHeader(ReferenceQueue referenceQueue) {
        this.referenceQueue = referenceQueue;
        SoftReference softReference = new SoftReference(header, referenceQueue);
    }

    public void setHttpLine(String httpPrinciple, String responseState, String responseInfo) {
        header.append(httpPrinciple).append(' ').append(responseState).append(' ').append(responseInfo).append('\n');
    }

    public void setDate(String time) {
        header.append("Date: ").append(time).append('\n');
    }

    /**
     * @param cookie ���cookieΪnull�������һ�����
     * @return 1 ������ӳɹ���0 ����cookie��ˢ�¡�
     */
    public void setCookie(Cookie cookie) {
        this.cookie = cookie;
    }

    public void setContentType(String contentType) {
        header.append("Content-Type: ").append(contentType).append('\n');
    }

    public void setBlanket() {
        header.append('\n');
    }

    public void setHttpBody(ResponseBody body) {
        setBlanket();
        header.append(body.toString());
    }

    @Override
    public String toString() {
        if (cookie != null) {
            header.append(cookie.toString());
        }
        return header.toString();
    }

    public void clean() {
        header.delete(0, header.length());
    }

    public void setContentLength(int length) {
        header.append("Content-Length: ").append(length).append('\n');
    }

    public void setConnection(String state) {
        header.append("Connection: ").append(state).append('\n');
    }
}
