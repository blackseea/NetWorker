package report.util.content;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

public class Cookie {
    private StringBuilder cookieContent = new StringBuilder("Set-Cookie: ");

    private Cookie(ReferenceQueue referenceQueue) {
        SoftReference softReference =new SoftReference(cookieContent,referenceQueue);
    }

    public void setDomain(String domain) {
        cookieContent.append("domain=").append(domain).append("; ");
    }

    public void setPath(String path) {
        cookieContent.append("Path=").append(path).append("; ");
    }

    public void setExpires(String expires) {
        cookieContent.append("Expires=").append(expires).append("; ");
    }

    public void setJsession(String jsession) {
        cookieContent.append("JSESSIONID=").append(jsession).append("; ");
    }

    public void setSecure(String secure) {
        cookieContent.append("Secure=").append(secure).append("; ");
    }

    public void put(String key, String value) {
        cookieContent.append(key).append('=').append(value).append("; ");
    }

    @Override
    public String toString() {
        return cookieContent.toString();
    }

    public int getCookieLength() {
        return cookieContent.length();
    }
}
