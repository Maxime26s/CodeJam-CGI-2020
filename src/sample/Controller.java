package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;

public class Controller {

    public void openAddToGardeMangerWindow()
    {
        try
        {
            Parent addToGardeMangerScene = FXMLLoader.load(getClass().getResource("addToGardeManger.fxml"));
            Main.addToGardeMangerStage.setTitle("Watchu Puttin' in yer Frigo?");
            try
            {
                Main.addToGardeMangerStage.initModality(Modality.APPLICATION_MODAL);
            }
            catch(Exception ignored) {}
            Main.addToGardeMangerStage.setScene(new Scene(addToGardeMangerScene, 480, 400));
            Main.addToGardeMangerStage.show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
