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

import java.io.*;
import java.net.Socket;
import java.text.DecimalFormat;
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


    public void addCommande()
    {
        if(!qteTextField.getText().isEmpty()&&Integer.parseInt(qteTextField.getText())>0){
            colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
            String produitPrix = "";

            DecimalFormat df = new DecimalFormat("#.##");
            for (int i = 0; i < Main.gestionnaire.getProduitsDisponibles().size(); i++)
            {
                if (Main.gestionnaire.getProduitsDisponibles().get(i).getNom().equals(listView.getItems().get(listView.getSelectionModel().getSelectedIndex()).toString()))
                {


                    produitPrix = df.format(Float.parseFloat(Main.gestionnaire.getProduitsDisponibles().get(i).getPrix())*Integer.parseInt(qteTextField.getText()));
                    break;
                }
            }
            ProduitTable produitTable = new ProduitTable(listView.getItems().get(listView.getSelectionModel().getSelectedIndex()).toString(),
                    qteTextField.getText().toString(),
                    produitPrix);
            commandeTable.getItems().add(produitTable);
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

    public void commander(){
        try {
            Socket socket = new Socket("127.0.0.1", 8080);

            OutputStream fluxSortant = socket.getOutputStream();
            OutputStreamWriter sortie = new OutputStreamWriter(fluxSortant);
            ObservableList items = commandeTable.getItems();
            sortie.write("client\n");
            sortie.write("commande\n");
            sortie.write(items.size()+"\n");
            for(int i=0; i<items.size();i++){
                sortie.write(((ProduitTable)items.get(i)).getNom()+"\n"+
                        ((ProduitTable)items.get(i)).getQuantity()+"\n"+
                        ((ProduitTable)items.get(i)).getPrix()+"\n");
            }
            sortie.flush();

            InputStream fluxEntrant = socket.getInputStream();
            BufferedReader entree = new BufferedReader(new InputStreamReader(fluxEntrant));
            String amount = entree.readLine();
            for(int i = 0;i<Integer.parseInt(amount);i++){
                String nom= entree.readLine();
                String quantity = entree.readLine();
                boolean found=false;
                for(int j=0;j<Main.gestionnaire.getInventaire().size();i++)
                    if(Main.gestionnaire.getInventaire().get(j).getProduit().getNom().equals(nom)){
                        Main.gestionnaire.getInventaire().get(j).setQuantite(Main.gestionnaire.getInventaire().get(j).getQuantite()+Integer.parseInt(quantity));
                        found=true;
                        break;
                    }

                if(!found){
                    for(int j=0;j<Main.gestionnaire.getProduitsDisponibles().size();j++){
                        if(Main.gestionnaire.getProduitsDisponibles().get(j).getNom().equals(nom)){
                            Main.gestionnaire.getInventaire().add(new ProduitInventaire(Main.gestionnaire.getProduitsDisponibles().get(j), Float.parseFloat(quantity),"",new DateExpiration(2050,05,01)));
                                    break;
                        }
                    }
                }

            }
            sortie.close();
            entree.close();
        } catch (Exception e) {
            e.printStackTrace();
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