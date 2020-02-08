package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;

public class addToGardeMangerController {
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
}
