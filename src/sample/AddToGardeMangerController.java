package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import classes.*;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class AddToGardeMangerController {

    @FXML
    Label labelUnite;

    @FXML
    ListView listView;

    @FXML
    TextField qteTextField;

    @FXML
    DatePicker datePicker;

    @FXML
    public void initialize() {
        reloadItems();
    }

    public void openAddAlimentWindow() {
        try
        {
            Parent alimentScene = FXMLLoader.load(getClass().getResource("addAliment.fxml"));
            Main.alimentStage.setTitle("Enter yer aliment por favor");
            try
            {
                Main.alimentStage.initModality(Modality.APPLICATION_MODAL);
            }
            catch(Exception ignored) {}
            Main.alimentStage.setScene(new Scene(alimentScene, 480, 400));
            Main.alimentStage.show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void checkUniteMesure(){
        try{
            Produit itemSelected = Main.gestionnaire.getProduitsDisponibles().get(listView.getSelectionModel().getSelectedIndex());
            labelUnite.setText(itemSelected.getMesureType());
        }catch (Exception e){
            System.out.println("Aucun item sélectionné");
            System.out.println(e);
        }
    }

    public void ajouterAuGardeManger(){
        try{
            Produit itemSelected = Main.gestionnaire.getProduitsDisponibles().get(listView.getSelectionModel().getSelectedIndex());
            LocalDate localDate = datePicker.getValue();

            boolean preexistant = false;
            for (ProduitInventaire produitInventaire:
                 Main.gestionnaire.getInventaire()) {
                if (produitInventaire.getProduit() == itemSelected){
                    preexistant = true;
                    produitInventaire.setQuantite(produitInventaire.getQuantite()+Float.parseFloat(qteTextField.getText()));
                    produitInventaire.setDateExp(new DateExpiration(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth()));
                    produitInventaire.setExpire(false);
                }
            }
            if (!preexistant){
                Main.gestionnaire.getInventaire().add(new ProduitInventaire(itemSelected, Float.parseFloat(qteTextField.getText()),
                        itemSelected.getMesureType(), new DateExpiration(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth())));
            }

        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void updateListView(){
        listView.getSelectionModel().clearSelection();
        listView.getItems().clear();
        ArrayList<String> nomsAlimentsDispos = new ArrayList<>();
        for (Produit produit:
             Main.gestionnaire.getProduitsDisponibles()) {
            nomsAlimentsDispos.add(produit.getNom());
        }
        ObservableList<String> observableList = FXCollections.observableList(nomsAlimentsDispos);
        listView.setItems(observableList);
    }

    public void reloadItems(){
        try {
            Socket socket = new Socket("127.0.0.1", 8080);

            OutputStream fluxSortant = socket.getOutputStream();
            OutputStreamWriter sortie = new OutputStreamWriter(fluxSortant);
            sortie.write("client\n");
            sortie.write("reloadItems\n");
            sortie.flush();

            InputStream fluxEntrant = socket.getInputStream();
            BufferedReader entree = new BufferedReader(new InputStreamReader(fluxEntrant));
            String amount = entree.readLine();
            Main.gestionnaire.getProduitsDisponibles().clear();
            for(int i = 0;i<Integer.parseInt(amount);i++){
                Main.gestionnaire.getProduitsDisponibles().add(new Produit(entree.readLine(), entree.readLine(), entree.readLine(), entree.readLine(), entree.readLine(), entree.readLine()));
            }
            sortie.close();
            entree.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateListView();
    }
}
