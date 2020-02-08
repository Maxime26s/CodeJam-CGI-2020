package distributeur;

import classes.Gestionnaire;
import classes.Mesures;
import classes.Produit;
import classes.ProduitInventaire;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import sample.Main;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DistributeurController {
    public static Gestionnaire gestionnaire = new Gestionnaire();
    public static ArrayList<String[]> commande = new ArrayList<String[]>();

    @FXML
    private ListView listView;

    @FXML
    private TextField modNom, modCode, modMes, modPrix, modInventaire, addNom, addCode, addMes, addPrix, addInventaire;

    @FXML
    private RadioButton modG, modL, modMili, modRien, modKilo, addG, addL, addMili, addRien, addKilo;

    @FXML
    private ToggleGroup modType, modQuantity, addType, addQuantity;

    @FXML
    private Label erreur;

    public void addItemListView(){
        if(!addNom.getText().isEmpty()&&!addCode.getText().isEmpty()&&!addMes.getText().isEmpty()&& !addPrix.getText().isEmpty()
                &&!addInventaire.getText().isEmpty()&&addType.getSelectedToggle().isSelected()&&addQuantity.getSelectedToggle().isSelected()){
            messageParser("blanc", "Envoie des données...");
            try {
                Socket socket = new Socket("127.0.0.1", 8080);

                OutputStream fluxSortant = socket.getOutputStream();
                OutputStreamWriter sortie = new OutputStreamWriter(fluxSortant);
                sortie.write("addItem\n");
                sortie.write(addNom.getText()+"\n");
                sortie.write(addCode.getText()+"\n");
                sortie.write(addMes.getText()+"\n");
                sortie.write(addPrix.getText()+"\n");
                sortie.write(addInventaire.getText()+"\n");
                sortie.write(((RadioButton)addType.getSelectedToggle()).getText() + "\n");
                sortie.write(((RadioButton)addQuantity.getSelectedToggle()).getText() + "\n");
                sortie.flush();

                InputStream fluxEntrant = socket.getInputStream();
                BufferedReader entree = new BufferedReader(new InputStreamReader(fluxEntrant));
                String couleur = entree.readLine();
                String message = entree.readLine();
                sortie.close();
                entree.close();
                messageParser(couleur, message);
            } catch(Exception e){
                messageParser("rouge", "ERREUR LORS DE L'ENVOIE DES DONNÉES");
                e.printStackTrace();
            }
        } else{
            messageParser("rouge", "ERREUR: ENTRÉE INVALIDE");
        }

    }

    public void modItemListView(){

    }

    public void loadProduit(){
        try {
            Socket socket = new Socket("127.0.0.1", 8080);

            OutputStream fluxSortant = socket.getOutputStream();
            OutputStreamWriter sortie = new OutputStreamWriter(fluxSortant);
            sortie.write("findByName\n");
            sortie.write(listView.getSelectionModel().getSelectedIndex()+"\n");
            sortie.close();

            InputStream fluxEntrant = socket.getInputStream();
            BufferedReader entree = new BufferedReader(new InputStreamReader(fluxEntrant));
            modNom.setText(entree.readLine());
            modCode.setText(entree.readLine());
            modMes.setText(entree.readLine());
            modPrix.setText(entree.readLine());
            modInventaire.setText(entree.readLine());
            entree.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void messageParser(String couleur, String message){
        if(couleur.equals("rouge"))
            erreur.setTextFill(Color.web("#ff0000"));
        else
            erreur.setTextFill(Color.web("#ffffff"));
        erreur.setText(message);
    }

    public static void sendData() {
        try {
            Socket socket = new Socket("127.0.0.1", 8080);

            OutputStream fluxSortant = socket.getOutputStream();
            OutputStreamWriter sortie = new OutputStreamWriter(fluxSortant);
            sortie.write("commande\n");
            sortie.write(commande.size() + "\n");
            for (int i = 0; i < commande.size(); i++) {
                sortie.write(commande.get(i)[0] + "\n");
                sortie.write(commande.get(i)[1] + "\n");
                sortie.write(commande.get(i)[2] + "\n");
            }
            sortie.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}