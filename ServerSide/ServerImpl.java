import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject; 
import java.io.*;

public class ServerImpl implements Server {
        
		Registry rmiRegistry;
        public void start() throws Exception{
			
            rmiRegistry = LocateRegistry.getRegistry();
            rmiRegistry.bind("192.168.0.2", this);
			
            System.out.println("Server started");
        }
        public void stop() throws Exception {
            rmiRegistry.unbind("server");
            UnicastRemoteObject.unexportObject(this, true);
            UnicastRemoteObject.unexportObject(rmiRegistry, true);
            System.out.println("Server stopped");
        }
        
        public String sayHello() {
            return "Hello world";
        }
		public OutputStream getOutputStream(File f) throws IOException {
			return new RMIOutputStream(new RMIOutputStreamImpl(new 
			FileOutputStream(f)));
		}

		public InputStream getInputStream(File f) throws IOException {
			return new RMIInputStream(new RMIInputStreamImpl(new FileInputStream(f)));
		}
        
    }