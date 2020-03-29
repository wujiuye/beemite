package com.wujiuye.beemite.util;

/**
 * sessionId上下文
 *
 * @author wujiuye 2020/03/29
 */
public class SessionIdContext extends ThreadLocal<String> {

    private static SessionIdContext context;

    public static SessionIdContext getContext() {
        if (context == null) {
            synchronized (SessionIdContext.class) {
                if (context == null) {
                    context = new SessionIdContext();
                }
            }
        }
        return context;
    }

    public void setSessionId(String sessionId) {
        this.set(sessionId);
    }

    public String getSessionId() {
        return this.get();
    }

    public void removeSessionId() {
        this.remove();
    }

}
