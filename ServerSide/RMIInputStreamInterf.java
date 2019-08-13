import java.rmi.*;
import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.RemoteException; 
import java.rmi.server.UnicastRemoteObject; 
import java.io.Serializable;
import java.io.*;

public interface RMIInputStreamInterf extends Remote {
    
    public byte[] readBytes(int len) throws IOException, 
    RemoteException;
    public int read() throws IOException, RemoteException;
    public void close() throws IOException, RemoteException;

}