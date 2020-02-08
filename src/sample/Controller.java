package sample;
import classes.Mesures;
import classes.Produit;
import classes.Gestionnaire;
import classes.ProduitInventaire;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Controller {

    public static Gestionnaire gestionnaire = new Gestionnaire();
    public static ArrayList<String[]> commande = new ArrayList<String[]>();

    public void addProduit(){
        LocalDate date = LocalDate.now();
        Produit produit = new Produit("nom", "1111", 10.50f, date, 10, Mesures.LITRE);
        gestionnaire.inventaire.add(new ProduitInventaire(produit));
    }

    public void addInventaire(){

    }

    public void addCommande(){
        String parts[] = new String[3];
        parts[0] = "read name";
        parts[1] = "amount wanted";
        parts[2] = "price per unit";
        commande.add(parts);
    }

    public static void sendData(){
        try {
            Socket socket = new Socket("127.0.0.1", 8080);

            OutputStream fluxSortant = socket.getOutputStream();
            OutputStreamWriter sortie = new OutputStreamWriter(fluxSortant);
            sortie.write("commande\n");
            sortie.write(commande.size()+"\n");
            for(int i=0;i<commande.size();i++) {
                sortie.write(commande.get(i)[0]+"\n");
                sortie.write(commande.get(i)[1]+"\n");
                sortie.write(commande.get(i)[2]+"\n");
            }
            sortie.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
