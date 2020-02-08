package distributeur;

import classes.Gestionnaire;
import classes.Mesures;
import classes.Produit;
import classes.ProduitInventaire;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.Socket;
import java.text.DecimalFormat;
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
    private Label erreur, oldName;

    @FXML
    public void initialize() {
        reloadItems();
    }

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
                float mesure = Math.round(Float.parseFloat(addMes.getText()) * 1000.0f) / 1000.0f;
                switch (((RadioButton)addQuantity.getSelectedToggle()).getText()){
                    case "Kilo":
                        mesure*=1000;
                        sortie.write(mesure+"\n");
                        break;
                    case "Mili":
                        mesure*=0.0001;
                        sortie.write(mesure+"\n");
                        break;
                    default:
                        sortie.write(mesure+"\n");
                }
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
                reloadItems();
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
        if(!modNom.getText().isEmpty()&&!modCode.getText().isEmpty()&&!modMes.getText().isEmpty()&& !modPrix.getText().isEmpty()
                &&!modInventaire.getText().isEmpty()&&modType.getSelectedToggle().isSelected()&&modQuantity.getSelectedToggle().isSelected()){
            messageParser("blanc", "Envoie des données...");
            try {
                Socket socket = new Socket("127.0.0.1", 8080);

                OutputStream fluxSortant = socket.getOutputStream();
                OutputStreamWriter sortie = new OutputStreamWriter(fluxSortant);
                sortie.write("modItem\n");
                sortie.write(oldName.getText()+"\n");
                sortie.write(modNom.getText()+"\n");
                sortie.write(modCode.getText()+"\n");
                float mesure = Math.round(Float.parseFloat(modMes.getText()) * 1000.0f) / 1000.0f;
                switch (((RadioButton)modQuantity.getSelectedToggle()).getText()){
                    case "Kilo":
                        mesure*=1000;
                        sortie.write(mesure+"\n");
                        break;
                    case "Mili":
                        mesure*=0.0001;
                        sortie.write(mesure+"\n");
                        break;
                    default:
                        sortie.write(mesure+"\n");
                }
                sortie.write(modPrix.getText()+"\n");
                sortie.write(modInventaire.getText()+"\n");
                sortie.write(((RadioButton)modType.getSelectedToggle()).getText() + "\n");
                sortie.write(((RadioButton)modQuantity.getSelectedToggle()).getText() + "\n");
                sortie.flush();

                InputStream fluxEntrant = socket.getInputStream();
                BufferedReader entree = new BufferedReader(new InputStreamReader(fluxEntrant));
                String couleur = entree.readLine();
                String message = entree.readLine();
                sortie.close();
                entree.close();
                reloadItems();
                messageParser(couleur, message);
                oldName.setText("Objet à modifier: " + modNom.getText());
            } catch(Exception e){
                messageParser("rouge", "ERREUR LORS DE L'ENVOIE DES DONNÉES");
                e.printStackTrace();
            }
        } else{
            messageParser("rouge", "ERREUR: ENTRÉE INVALIDE");
        }
    }

    public void loadProduit(){
        try {
            if(modType.getSelectedToggle() !=null)
                modType.getSelectedToggle().setSelected(false);
            if(modQuantity.getSelectedToggle() !=null)
                modQuantity.getSelectedToggle().setSelected(false);
            Socket socket = new Socket("127.0.0.1", 8080);

            OutputStream fluxSortant = socket.getOutputStream();
            OutputStreamWriter sortie = new OutputStreamWriter(fluxSortant);
            sortie.write("findByName\n");
            sortie.write(listView.getSelectionModel().getSelectedItem()+"\n");
            sortie.flush();
            oldName.setText(listView.getSelectionModel().getSelectedItem().toString());
            InputStream fluxEntrant = socket.getInputStream();
            BufferedReader entree = new BufferedReader(new InputStreamReader(fluxEntrant));
            modNom.setText(entree.readLine());
            modCode.setText(entree.readLine());
            String what = entree.readLine();
            float mesure = Math.round(Float.parseFloat(what) * 1000.0f) / 1000.0f;
            if(mesure>=1000){
                modKilo.setSelected(true);
                mesure*=0.001;

            }
            else if(mesure<1){
                modMili.setSelected(true);
                mesure*=1000;
            }
            else
                modRien.setSelected(true);
            modMes.setText(Double.toString(mesure));
            modPrix.setText(entree.readLine());
            modInventaire.setText(entree.readLine());
            String mesureType = entree.readLine();
            if(mesureType.equals("Litre"))
                modL.setSelected(true);
            else if(mesureType.equals("Gramme"))
                modG.setSelected(true);
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

    public void reloadItems(){
        try {
            Socket socket = new Socket("127.0.0.1", 8080);

            OutputStream fluxSortant = socket.getOutputStream();
            OutputStreamWriter sortie = new OutputStreamWriter(fluxSortant);
            sortie.write("reloadItems\n");
            sortie.flush();

            InputStream fluxEntrant = socket.getInputStream();
            BufferedReader entree = new BufferedReader(new InputStreamReader(fluxEntrant));
            listView.getSelectionModel().clearSelection();
            listView.getItems().clear();
            String amount = entree.readLine();
            String parts[] = new String[Integer.parseInt(amount)];
            for(int i = 0;i<Integer.parseInt(amount);i++){
                listView.getItems().add(entree.readLine());
            }
            String couleur = entree.readLine();
            String message = entree.readLine();
            sortie.close();
            entree.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void modSupItem(){
        try {
            Socket socket = new Socket("127.0.0.1", 8080);

            OutputStream fluxSortant = socket.getOutputStream();
            OutputStreamWriter sortie = new OutputStreamWriter(fluxSortant);
            sortie.write("supItem\n");
            sortie.write(oldName.getText()+"\n");
            sortie.flush();
            sortie.close();

            reloadItems();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}