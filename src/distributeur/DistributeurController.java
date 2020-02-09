package distributeur;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.io.*;
import java.net.Socket;

public class DistributeurController {
    @FXML
    private ListView listView;

    @FXML
    private TextField modNom, modCode, modMes, modPrix, modInventaire, modLong, addNom, addCode, addMes, addPrix, addInventaire, addLong;

    @FXML
    private RadioButton modG, modL, modMili, modRien, modKilo, addG, addL, addMili, addRien, addKilo;

    @FXML
    private ToggleGroup modType, modQuantity, addType, addQuantity;

    @FXML
    private Label erreur, oldName;

    @FXML
    private Menu revenu;

    @FXML
    public void initialize() {
        reloadItems();
    }

    private String username = "Default";

    public void addItemListView() {
        if (!addNom.getText().isEmpty() && !addCode.getText().isEmpty() && !addMes.getText().isEmpty() && !addPrix.getText().isEmpty()
                && !addInventaire.getText().isEmpty() && addType.getSelectedToggle().isSelected() && addQuantity.getSelectedToggle().isSelected()) {
            messageParser("blanc", "Envoie des données...");
            try {
                Socket socket = new Socket("127.0.0.1", 8080);

                OutputStream fluxSortant = socket.getOutputStream();
                OutputStreamWriter sortie = new OutputStreamWriter(fluxSortant);
                sortie.write("distributeur\n");
                sortie.write(username + "\n");
                sortie.write("addItem\n");
                sortie.write(addNom.getText() + "\n");
                sortie.write(addCode.getText() + "\n");
                float mesure = Math.round(Float.parseFloat(addMes.getText()) * 1000.0f) / 1000.0f;
                switch (((RadioButton) addQuantity.getSelectedToggle()).getText()) {
                    case "Kilo":
                        mesure *= 1000;
                        sortie.write(mesure + "\n");
                        break;
                    case "Mili":
                        mesure *= 0.0001;
                        sortie.write(mesure + "\n");
                        break;
                    default:
                        sortie.write(mesure + "\n");
                }
                sortie.write(addPrix.getText() + "\n");
                sortie.write(addInventaire.getText() + "\n");
                sortie.write(((RadioButton) addType.getSelectedToggle()).getText() + "\n");
                sortie.write(((RadioButton) addQuantity.getSelectedToggle()).getText() + "\n");
                sortie.write(addLong.getText() + "\n");
                sortie.flush();

                InputStream fluxEntrant = socket.getInputStream();
                BufferedReader entree = new BufferedReader(new InputStreamReader(fluxEntrant));
                String couleur = entree.readLine();
                String message = entree.readLine();
                sortie.close();
                entree.close();
                reloadItems();
                messageParser(couleur, message);
            } catch (Exception e) {
                messageParser("rouge", "ERREUR LORS DE L'ENVOIE DES DONNÉES");
                e.printStackTrace();
            }
        } else {
            messageParser("rouge", "ERREUR: ENTRÉE INVALIDE");
        }
    }

    public void modItemListView() {
        if (!modNom.getText().isEmpty() && !modCode.getText().isEmpty() && !modMes.getText().isEmpty() && !modPrix.getText().isEmpty()
                && !modInventaire.getText().isEmpty() && modType.getSelectedToggle().isSelected() && modQuantity.getSelectedToggle().isSelected()) {
            messageParser("blanc", "Envoie des données...");
            try {
                Socket socket = new Socket("127.0.0.1", 8080);

                OutputStream fluxSortant = socket.getOutputStream();
                OutputStreamWriter sortie = new OutputStreamWriter(fluxSortant);
                sortie.write("distributeur\n");
                sortie.write(username + "\n");
                sortie.write("modItem\n");
                sortie.write(oldName.getText() + "\n");
                sortie.write(modNom.getText() + "\n");
                sortie.write(modCode.getText() + "\n");
                float mesure = Math.round(Float.parseFloat(modMes.getText()) * 1000.0f) / 1000.0f;
                switch (((RadioButton) modQuantity.getSelectedToggle()).getText()) {
                    case "Kilo":
                        mesure *= 1000;
                        sortie.write(mesure + "\n");
                        break;
                    case "Mili":
                        mesure *= 0.0001;
                        sortie.write(mesure + "\n");
                        break;
                    default:
                        sortie.write(mesure + "\n");
                }
                sortie.write(modPrix.getText() + "\n");
                sortie.write(modInventaire.getText() + "\n");
                sortie.write(((RadioButton) modType.getSelectedToggle()).getText() + "\n");
                sortie.write(((RadioButton) modQuantity.getSelectedToggle()).getText() + "\n");
                sortie.write(modLong.getText() + "\n");
                sortie.flush();

                InputStream fluxEntrant = socket.getInputStream();
                BufferedReader entree = new BufferedReader(new InputStreamReader(fluxEntrant));
                String couleur = entree.readLine();
                String message = entree.readLine();
                sortie.close();
                entree.close();
                reloadItems();
                messageParser(couleur, message);
                oldName.setText(modNom.getText());
            } catch (Exception e) {
                messageParser("rouge", "ERREUR LORS DE L'ENVOIE DES DONNÉES");
                e.printStackTrace();
            }
        } else {
            messageParser("rouge", "ERREUR: ENTRÉE INVALIDE");
        }
    }

    public void loadProduit() {
        try {
            if (modType.getSelectedToggle() != null)
                modType.getSelectedToggle().setSelected(false);
            if (modQuantity.getSelectedToggle() != null)
                modQuantity.getSelectedToggle().setSelected(false);
            Socket socket = new Socket("127.0.0.1", 8080);

            OutputStream fluxSortant = socket.getOutputStream();
            OutputStreamWriter sortie = new OutputStreamWriter(fluxSortant);
            sortie.write("distributeur\n");
            sortie.write(username + "\n");
            sortie.write("findByName\n");
            sortie.write(listView.getSelectionModel().getSelectedItem() + "\n");
            sortie.flush();
            oldName.setText(listView.getSelectionModel().getSelectedItem().toString());
            InputStream fluxEntrant = socket.getInputStream();
            BufferedReader entree = new BufferedReader(new InputStreamReader(fluxEntrant));
            modNom.setText(entree.readLine());
            modCode.setText(entree.readLine());
            String what = entree.readLine();
            float mesure = Math.round(Float.parseFloat(what) * 1000.0f) / 1000.0f;
            if (mesure >= 1000) {
                modKilo.setSelected(true);
                mesure *= 0.001;

            } else if (mesure < 1) {
                modMili.setSelected(true);
                mesure *= 1000;
            } else
                modRien.setSelected(true);
            modMes.setText(Double.toString(mesure));
            modPrix.setText(entree.readLine());
            modInventaire.setText(entree.readLine());
            String mesureType = entree.readLine();
            if (mesureType.equals("Litre"))
                modL.setSelected(true);
            else if (mesureType.equals("Gramme"))
                modG.setSelected(true);
            modLong.setText(entree.readLine());
            entree.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void messageParser(String couleur, String message) {
        if (couleur.equals("rouge"))
            erreur.setTextFill(Color.web("#ff0000"));
        else
            erreur.setTextFill(Color.web("#ffffff"));
        erreur.setText(message);
    }

    public void reloadItems() {
        try {
            Socket socket = new Socket("127.0.0.1", 8080);

            OutputStream fluxSortant = socket.getOutputStream();
            OutputStreamWriter sortie = new OutputStreamWriter(fluxSortant);
            sortie.write("distributeur\n");
            sortie.write(username + "\n");
            sortie.write("reloadItems\n");
            sortie.flush();

            InputStream fluxEntrant = socket.getInputStream();
            BufferedReader entree = new BufferedReader(new InputStreamReader(fluxEntrant));
            listView.getSelectionModel().clearSelection();
            listView.getItems().clear();
            String amount = entree.readLine();
            for (int i = 0; i < Integer.parseInt(amount); i++) {
                listView.getItems().add(entree.readLine());
            }
            revenu.setText("Revenu(s): "+entree.readLine()+"$");
            String couleur = entree.readLine();
            String message = entree.readLine();
            sortie.close();
            entree.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void modSupItem() {
        try {
            Socket socket = new Socket("127.0.0.1", 8080);

            OutputStream fluxSortant = socket.getOutputStream();
            OutputStreamWriter sortie = new OutputStreamWriter(fluxSortant);
            sortie.write("distributeur\n");
            sortie.write(username + "\n");
            sortie.write("supItem\n");
            sortie.write(oldName.getText() + "\n");
            sortie.flush();
            sortie.close();

            reloadItems();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changerCompte(){
        TextInputDialog alerteValeur = new TextInputDialog("Entrez ici");
        alerteValeur.setTitle("Authentification");
        alerteValeur.setHeaderText("Entrez votre nom de distributeur");
        username = alerteValeur.showAndWait().get();
        reloadItems();
    }
}