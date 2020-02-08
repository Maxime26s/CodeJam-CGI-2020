package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import classes.*;

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
    TableView commandeTable;
    @FXML
    TableColumn colNom;
    @FXML
    TableColumn colQuantity;
    @FXML
    TableColumn colPrix;
    @FXML
    DatePicker datePicker;

    public void openAddAlimentWindow()
    {
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


    public void addCommande()
    {
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        String produitPrix = "";


        for (int i = 0; i < Main.gestionnaire.getProduitsDisponibles().size(); i++)
        {
            if (Main.gestionnaire.getProduitsDisponibles().get(i).getNom().contains(listView.getSelectionModel().toString()))
            {
                produitPrix = Main.gestionnaire.getProduitsDisponibles().get(i).getPrix();
                break;
            }
            else
            {
                produitPrix = "ERROR";
            }
        }
        ProduitTable produitTable = new ProduitTable(listView.getItems().get(listView.getSelectionModel().getSelectedIndex()).toString(),
                qteTextField.getText().toString(),
                produitPrix);
        /*
        commande.add(listView.getItems().get(listView.getSelectionModel().getSelectedIndex()).toString());
        ObservableList<ProduitTable> observableList = FXCollections.observableArrayList(commande);
        */

        commandeTable.getItems().add(produitTable);
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
        ArrayList<String> nomsAlimentsDispos = new ArrayList<>();
        for (Produit produit:
             Main.gestionnaire.getProduitsDisponibles()) {
            nomsAlimentsDispos.add(produit.getNom());
        }
        ObservableList<String> observableList = FXCollections.observableList(nomsAlimentsDispos);
        listView.setItems(observableList);
    }
}