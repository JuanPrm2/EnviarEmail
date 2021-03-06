package enviaremail;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;





public class EnviarController implements Initializable{

		private Task <Void> enviarEmailTask;
	
		
	
	 	@FXML
	    private TextField asuntoDelMensajeTextField;

	    @FXML
	    private Button cerrarButton;

	    @FXML
	    private CheckBox conexionSSlCheckBox;

	    @FXML
	    private PasswordField contraseniaPasswordField;

	    @FXML
	    private TextField emailRemitenteTextField;

	    @FXML
	    private TextField emailDestinatarioTextField;

	    @FXML
	    private Button enviarButton;

	    @FXML
	    private TextArea mensajeTextArea;

	    @FXML
	    private TextField nombreIPTextField;

	    @FXML
	    private TextField puertoTextField;

	    @FXML
	    private GridPane root;

	    @FXML
	    private Button vaciarButton;

	    
	    public EnviarController() throws IOException{
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/View.fxml"));
    		loader.setController(this);
    		loader.load();
			
		}
	    	
	  		public void initialize(URL location, ResourceBundle resources) {
	    		
	  			
	  		}

	    @FXML
	    void onCerrarAction(ActionEvent event) {
	    	Platform.exit();
	    	
	    }

	    @FXML
	    void onEnviarAction(ActionEvent event) {
	    	
	    	enviarEmailTask=new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					
					
					
					Email email = new SimpleEmail();
					
					email.setHostName(nombreIPTextField.getText());
		    		email.setSmtpPort( Integer.parseInt(puertoTextField.getText()));
		    		email.setAuthenticator(new DefaultAuthenticator(emailRemitenteTextField.getText(),contraseniaPasswordField.getText() ));
		    		email.setSSLOnConnect(conexionSSlCheckBox.isSelected());
		    		email.setFrom(emailRemitenteTextField.getText());
		    		email.setSubject(asuntoDelMensajeTextField.getText());
		    		email.setMsg(mensajeTextArea.getText());
		    		email.addTo(emailDestinatarioTextField.getText());
		    		email.send();
					return null;
					
				}
			};
			
	    	enviarEmailTask.setOnSucceeded(e -> { aciertoAlert();});
	    	enviarEmailTask.setOnFailed(e -> {
	    		
	    		Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("No se pudo enviar el email");
				alert.setContentText(e.getSource().getMessage());

				alert.showAndWait();
	    		
				
	    	});
	    	Thread hilo= new Thread(enviarEmailTask);
	    	hilo.start();
	    	
	    }

	    @FXML
	    void onVaciarAction(ActionEvent event) {
	    	asuntoDelMensajeTextField.clear();
	    	contraseniaPasswordField.clear();
	    	emailDestinatarioTextField.clear();
	    	nombreIPTextField.clear();
	    	puertoTextField.clear();
	    	emailRemitenteTextField.clear();
	    	conexionSSlCheckBox.setSelected(false);
	    	mensajeTextArea.clear();
	    	
	    }

		public GridPane getRoot() {
			return root;
		}
		public void aciertoAlert() {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Mensaje enviado");
			alert.setHeaderText("Mensaje enviado con exito a "+"'"+emailDestinatarioTextField.getText()+"'.");
			alert.showAndWait();
		}
		
		public void errorAlert() {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("No se pudo enviar el email");
			alert.setContentText("Invalid message supplied");

			alert.showAndWait();
		}
}
