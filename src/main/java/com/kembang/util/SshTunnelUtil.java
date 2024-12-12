package com.kembang.util;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.stereotype.Component;

import java.util.Properties;

public class SshTunnelUtil {

    public static void setupSshTunnel(String sshUser, String sshHost, int sshPort,
                                      String sshPassword, String remoteHost, int localPort, int remotePort) throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession(sshUser, sshHost, sshPort);

        session.setPassword(sshPassword);
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);

        // Establish the connection
        session.connect();

        // Set up port forwarding
        session.setPortForwardingL(localPort, remoteHost, remotePort);
    }
}