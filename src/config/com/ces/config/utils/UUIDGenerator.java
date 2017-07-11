package com.ces.config.utils;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * <p>描述: 唯一主键生成办法 </p>
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * @author qiucs   
 * @date 2013-9-12 下午4:12:51
 */
public class UUIDGenerator {
    
    private static final int IP;
    
    private static UUIDGenerator instance;
    
    

    protected static int IptoInt(byte[] bytes) {
        int result = 0;
        for (int i = 0; i < 4; i++) {
            result = (result << 8) - Byte.MIN_VALUE + (int) bytes[i];
        }
        return result;
    }

    static {
        int ipadd;
        try {
            ipadd = IptoInt(InetAddress.getLocalHost().getAddress());
        } catch (Exception e) {
            ipadd = 0;
        }
        IP = ipadd;
    }

    private static short counter = (short) 0;

    private static final int JVM = (int) (System.currentTimeMillis() >>> 8);

    private UUIDGenerator() {    }

    /**
     * Unique across JVMs on this machine (unless they load this class in the same quater second - very unlikely)
     */
    protected int getJVM() {
        return JVM;
    }

    /**
     * Unique in a millisecond for this JVM instance (unless there are > Short.MAX_VALUE instances created in a
     * millisecond)
     */
    protected short getCount() {
        synchronized (UUIDGenerator.class) {
            if (counter < 0)
                counter = 0;
            return counter++;
        }
    }

    /**
     * Unique in a local network
     */
    protected int getIP() {
        return IP;
    }

    /**
     * Unique down to millisecond
     */
    protected short getHiTime() {
        return (short) (System.currentTimeMillis() >>> 32);
    }

    protected int getLoTime() {
        return (int) System.currentTimeMillis();
    }

    private final static String sep = "";

    protected String format(int intval) {
        String formatted = Integer.toHexString(intval);
        StringBuffer buf = new StringBuffer("00000000");
        buf.replace(8 - formatted.length(), 8, formatted);
        return buf.toString();
    }

    protected String format(short shortval) {
        String formatted = Integer.toHexString(shortval);
        StringBuffer buf = new StringBuffer("0000");
        buf.replace(4 - formatted.length(), 4, formatted);
        return buf.toString();
    }

    protected Serializable generate() {
        return new StringBuffer(36).append(format(getIP())).append(sep).append(format(getJVM())).append(sep)
                .append(format(getHiTime())).append(sep).append(format(getLoTime())).append(sep)
                .append(format(getCount())).toString();
    }
    
    private static UUIDGenerator getInstance() {
        if (null == instance) {
            instance = new UUIDGenerator();
        }
        return instance;
    }
    
    public static String uuid() {
        return String.valueOf(getInstance().generate());
    }
}
