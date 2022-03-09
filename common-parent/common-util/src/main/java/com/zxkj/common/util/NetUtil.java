package com.zxkj.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.util.Enumeration;

/**
 * net util
 */
public class NetUtil {
    private static Logger logger = LoggerFactory.getLogger(NetUtil.class);

    private static String localIp = null;

    public static String getLocalIp() {
        if (localIp != null) {
            return localIp;
        }
        localIp = getNacosRegisterIp();
        logger.info("init local ip : {}", localIp);
        return localIp;
    }

    /**
     * getNacosRegisterIp
     *
     * @return
     */
    private static String getNacosRegisterIp() {
        try {
            InetAddress result = null;
            Enumeration<NetworkInterface> nie = NetworkInterface.getNetworkInterfaces();
            while (nie.hasMoreElements()) {
                NetworkInterface ni = nie.nextElement();
                Enumeration<InetAddress> ie = ni.getInetAddresses();
                while (ie.hasMoreElements()) {
                    InetAddress address = ie.nextElement();
                    if (address instanceof Inet4Address && !address.isLoopbackAddress()) {
                        result = address;
                    }
                }
            }
            if (result != null) {
                return result.getHostAddress();
            }
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            logger.error("getNacosRegisterIp-Error", e);
            return null;
        }
    }

    /**
     * getFormatPort
     *
     * @param portStr
     * @return
     */
    public static int getFormatPort(String portStr) {
        if (portStr.length() == 3) {
            portStr = "31" + portStr;
        } else if (portStr.length() == 4) {
            portStr = "3" + portStr;
        } else if (portStr.length() == 5) {
            portStr = "3" + portStr.substring(1);
        }
        return Integer.parseInt(portStr);
    }

    /**
     * find avaliable port
     *
     * @param defaultPort
     * @return
     */
    public static int findAvailablePort(int defaultPort) {
        int portTmp = defaultPort;
        while (portTmp < 65535) {
            if (!isPortUsed(portTmp)) {
                return portTmp;
            } else {
                portTmp++;
            }
        }
        portTmp = defaultPort--;
        while (portTmp > 0) {
            if (!isPortUsed(portTmp)) {
                return portTmp;
            } else {
                portTmp--;
            }
        }
        throw new RuntimeException("no available port.");
    }

    /**
     * check port used
     *
     * @param port
     * @return
     */
    public static boolean isPortUsed(int port) {
        boolean used = false;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            used = false;
        } catch (IOException e) {
            used = true;
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    logger.info("");
                }
            }
        }
        return used;
    }

}
