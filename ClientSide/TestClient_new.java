import java.rmi.*;
import java.rmi.registry.Registry; 
import java.rmi.registry.LocateRegistry; 
import java.rmi.RemoteException; 
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
// import java.rmi.server.UnicastRemoteObject; 
import java.io.Serializable;
import java.io.*;

public class TestClient_new extends Application {
    
    final public static int BUF_SIZE = 1024 * 64;
	static File sel_file, new_file;
    String password, f_path;
    File dec_dir, down_dir;
	public static Server srv;
	byte[] Key;
	Decryptor dec;
	
    public static void copy(InputStream in, OutputStream out) throws IOException {
        System.out.println("using byte[] read/write");
        byte[] b = new byte[BUF_SIZE];
        int len;
        while ((len = in.read(b)) >= 0) {
            out.write(b, 0, len);
        }
        in.close();
        out.close();
    }
    
    public static void upload(Server srv, File src, File dest) throws IOException {
        copy (new FileInputStream(src), srv.getOutputStream(dest));
    }

    public static void download(Server srv, File src, File dest) throws IOException {	
        //copy (srv.getInputStream(src), new FileOutputStream(dest));
		FileOutputStream outputStream;
        try (FileInputStream inputStream = new FileInputStream(src)) {
            byte[] inputBytes = new byte[(int) src.length()];
            inputStream.read(inputBytes);
            //byte[] outputBytes = cipher.doFinal(inputBytes);
            outputStream = new FileOutputStream(dest);
            outputStream.write(inputBytes);
            inputStream.close();
			outputStream.close();
        }
		catch (Exception ex) {
                    ex.printStackTrace();
                }
        
    }
    
	public static void main(String[] args) throws Exception {
		launch(args);
        try{
        String url = "server";
		Registry registry = LocateRegistry.getRegistry(null); 
		srv = (Server) registry.lookup("192.168.0.2"); 

		//Remote r = java.rmi.Naming.lookup(url);
		//ServerImpl srv = (ServerImpl)r;

        //ServerImpl srv = (ServerImpl)java.rmi.Naming.lookup(url);
		
        System.out.println("srv says: " + srv.sayHello());
			}
		catch(Exception e){
			e.printStackTrace();
		}
		
    }
	public void start(Stage primaryStage) {
		dec_dir = new File("C:\\Users\\Mradul Srivastava\\Desktop\\enc files"); 	//directory where encrypted files are downloaded
		down_dir = new File("\\\\SIRI\\Users\\siri\\Desktop\\encrypted files"); 	//directory on the server from where encrypted file has to be downloaded
		Scene scene;
		
		Button btn1 = new Button();
        Button btn2 = new Button();
		Label fileLabel = new Label();
		PasswordField passwordField = new PasswordField();
		Button button = new Button("Go");
		HBox hbox = new HBox(passwordField, button);
		Scene pswd_scene = new Scene(hbox, 200, 100);

		
		btn1.setText("Decrypt");
        btn2.setText("Download");
		
		btn1.setTranslateX(20); //sets the position of the buttons
        btn1.setTranslateY(200);
        
        btn2.setTranslateX(120);
        btn2.setTranslateY(200);
		
		Pane root = new Pane();
        root.getChildren().add(btn1);
        root.getChildren().add(btn2);
		
        root.getChildren().add(fileLabel);

        scene = new Scene(root, 300, 250);
        
        primaryStage.setTitle("File Encryption System");
        primaryStage.setScene(scene);
        primaryStage.show();
		
		btn1.setOnAction((ActionEvent event) -> {   //handles button clicks of btn1 (Decrypt)
            System.out.println("Decrypt");
            sel_file = select_file(dec_dir);
            
            if (sel_file != null) {
                try {
                    //System.out.println("File chosen successfully!");
                    fileLabel.setText("File selected: "+sel_file.getPath());
                    //System.out.println("label text set");
                    String output_name = "C:\\Users\\Mradul Srivastava\\Desktop\\dec files"+ "\\"+sel_file.getName();
					String Key_File_Path = "\\\\SIRI\\Users\\siri\\Desktop\\encrypted files\\siri"+"\\Key"+sel_file.getName();
					//File Key_File = new File(Key_File_Path);
					new_file = new File(output_name);
					//f_path = Key_File.getPath();
                    if (new_file.createNewFile()){
                        System.out.println("File created!");
                       
						primaryStage.setScene(pswd_scene);
						primaryStage.show();
						
						password = passwordField.getText();
						button.setOnAction((ActionEvent event1) -> {
						if(password.equals("abc") || true){
							//call decrypt function 
							File d_file = new File(Key_File_Path);
							try {
							FileInputStream fin = new FileInputStream(d_file);
							Key = new byte[(int)d_file.length()];
							fin.read(Key);
							fin.close();
							
						} 
						catch (FileNotFoundException ex) {
									System.out.println(ex);						
						} 
						catch (IOException ex) {
							System.out.println(ex);	
						}
						dec = new Decryptor(Key, sel_file, new_file); 
           
						}
						
						else{
							System.out.println("Authorization unsuccessful");
						}
						primaryStage.setScene(scene);
						primaryStage.show();
						//fileLabel.setVisibility(false);
						});
						
						}
					else{
						System.out.println("File already exists.");
					}	 
					 
				}catch (IOException ex) {
                    ex.printStackTrace();
                   }
            }
            else{
                System.out.println("File not chosen!");
            }
		
        } 
        );
		
		btn2.setOnAction((ActionEvent event) -> {   //handles button clicks of btn2 (Download)
            System.out.println("Download");
            sel_file = select_file(down_dir);
            
            if (sel_file != null) {
                try {
                    
                    fileLabel.setText("File selected: "+sel_file.getPath());
                    String output_name = "C:\\Users\\Mradul Srivastava\\Desktop\\enc files"+ "\\"+sel_file.getName();
					new_file = new File(output_name);
					if (new_file.createNewFile()){
					System.out.println("File created!");
					}
					
			else{
                        System.out.println("File already exists.");
                    }
						
						download(srv, sel_file, new_file);
                    }
                
                    
                 catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            else{
                System.out.println("File not chosen!");
            }
        } 
        );
		
		
	}
	public File select_file(File dir){          //opens dialog to choose file
        FileChooser fileChooser = new FileChooser();
        
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TEXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        fileChooser.setTitle("Select file to encrypt");
        fileChooser.setInitialDirectory(dir);

        Stage file_stage = new Stage();
        //fileChooser.showOpenDialog(file_stage);
        File file = fileChooser.showOpenDialog(file_stage);
        
        return file;
        
    }

    
}
