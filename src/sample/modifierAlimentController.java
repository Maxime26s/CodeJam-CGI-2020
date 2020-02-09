package sample;

import classes.DateExpiration;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.awt.*;

import static sample.Controller.selectedIndex;

public class modifierAlimentController {

    @FXML
    TextField actualDate;

    @FXML
    DatePicker newDate;

    @FXML
    public void initialize()
    {
        String annee, mois, jour;
        if (Main.gestionnaire.getInventaire().get(selectedIndex).getDateExp().getDay() < 10)
        {
            jour = "0" + Integer.toString(Main.gestionnaire.getInventaire().get(selectedIndex).getDateExp().getDay());
        }
        else
        {
            jour = Integer.toString(Main.gestionnaire.getInventaire().get(selectedIndex).getDateExp().getDay());
        }
        if (Main.gestionnaire.getInventaire().get(selectedIndex).getDateExp().getMonth() < 10)
        {
            mois = "0" + Integer.toString(Main.gestionnaire.getInventaire().get(selectedIndex).getDateExp().getMonth());
        }
        else
        {
            mois = Integer.toString(Main.gestionnaire.getInventaire().get(selectedIndex).getDateExp().getMonth());
        }
        annee = Integer.toString(Main.gestionnaire.getInventaire().get(selectedIndex).getDateExp().getYear());
        String stringDate = annee + "-" + mois + "-" + jour;
        actualDate.setText(stringDate);
    }

    public void confirmerDate()
    {
        Main.gestionnaire.getInventaire().get(selectedIndex).setDateExp(new DateExpiration(newDate.getValue().getYear(), newDate.getValue().getMonthValue(), newDate.getValue().getDayOfMonth()));
        Main.modifierAlimentStage.close();
    }

    public void annuler()
    {
        Main.modifierAlimentStage.close();
    }
}
