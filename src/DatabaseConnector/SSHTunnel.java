package DatabaseConnector;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHTunnel {
    /**
     * lPort := Local Port
     * rPort := Remote Port
     */

    private Session session;
    private Session sessionSSH;
    private int lPort;

    public void connect() {
        String jorah = "jove.macs.hw.ac.uk";
        String mysqlServer = "mysql-sever-1";
        String user = "db47";
        String password = "EdenHazard10";
        int sshPort = 22;
        lPort = 4545;
        int rPort = 3306;
        JSch ssh = new JSch();

        try {
            session = ssh.getSession(user, jorah, sshPort);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            session.setPortForwardingL(lPort, mysqlServer, rPort);

            if(isAlive()) {
                System.out.println("Jorah SSH TUNNEL CONNECTED");
               // System.out.println("localhost:" + assigned_port + " -> " + remoteHost + ":" + rPort);
            } else {
                throw new Exception("SSH Tunnel not connected. Failed to pass checks");
            }

//            int assignedPort = session.setPortForwardingL(lPort, host, rPort);
//            System.out.println("Port forwarded");

        } catch (JSchException e) {
            disconnect();
            System.err.println("JSchException");
            System.err.println(e);
        } catch (Exception e) {
            disconnect();
            System.err.println("Exception");
            System.err.println(e);
        }
    }

    public int getInternalPort() {
        return this.lPort;
    }

    public void disconnect() {
        session.disconnect();
        System.out.println("SSH TUNNEL DISCONNECTED");
    }

    public boolean isAlive() {
        return session.isConnected();
    }
}
