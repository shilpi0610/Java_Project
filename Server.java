import java.rmi.Remote; 
import java.rmi.RemoteException;
import java.io.*;

public interface Server extends Remote {
        public String sayHello() throws RemoteException;
		public void start() throws Exception;
		public void stop() throws Exception;
		public OutputStream getOutputStream(File f) throws IOException;
		public InputStream getInputStream(File f) throws IOException;
    }