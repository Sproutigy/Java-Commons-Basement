package com.sproutigy.commons.basement.runtime;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class LocalHost {
    private static InetAddress address;

    private LocalHost() {
    }

    public static InetAddress get() {
        if (address == null) {
            synchronized (LocalHost.class) {
                if (address == null) {
                    try {
                        address = lookup();
                    } catch (Exception e) {
                        UnknownHostException unknownHostException = new UnknownHostException("Failed to determine local address: " + e);
                        unknownHostException.initCause(e);
                        throw new RuntimeException(unknownHostException);
                    }
                }
            }
        }

        return address;
    }

    private static InetAddress lookup() throws UnknownHostException, SocketException {
        InetAddress candidateAddress = null;
        for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
            NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
            for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                if (!inetAddr.isLoopbackAddress()) {

                    if (inetAddr.isSiteLocalAddress()) {
                        return inetAddr;
                    } else if (candidateAddress == null) {
                        candidateAddress = inetAddr;
                    }
                }
            }
        }
        if (candidateAddress != null) {
            return candidateAddress;
        }
        InetAddress javaSuppliedAddress = InetAddress.getLocalHost();
        if (javaSuppliedAddress == null) {
            throw new UnknownHostException("The InetAddress.getLocalHost() method unexpectedly returned null.");
        }
        return javaSuppliedAddress;
    }

    public static String getName() {
        return get().getHostName();
    }

    public static String getIP() {
        return get().getHostAddress();
    }
}

