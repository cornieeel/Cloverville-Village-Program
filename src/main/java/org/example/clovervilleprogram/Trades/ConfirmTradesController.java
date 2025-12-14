package org.example.clovervilleprogram.Trades;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.clovervilleprogram.Users.User;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

public class ConfirmTradesController
{

  @FXML private Button saveButton;
  @FXML private Button cancelButton;

  @FXML private TextField fullNameEdit;
  @FXML private TextField pointsEdit;
  @FXML private TextField tasksEdit;
  @FXML private ComboBox<String> buyersList;
  @FXML private Label textLabel;
  private Consumer<Trades> onTradeConfirmed;

  private Trades trade;
  private final File buyers = new File("users.json");


  public void initialize(){
    loadBuyersFromJson();
  }
  public void setTrade(Trades trade){
    this.trade = trade;

    fullNameEdit.setText(trade.getResidentName());
    pointsEdit.setText(String.valueOf(trade.getPrice()));
    tasksEdit.setText(trade.getGoodToOffer());

  }
  public void setOnTradeConfirmed(Consumer<Trades> callback) {
    this.onTradeConfirmed = callback;
  }
  public void loadBuyersFromJson(){

    if(!buyers.exists()) return;

    try{
      ObjectMapper mapper = new ObjectMapper();
      List<User> users = mapper.readValue(buyers, new TypeReference<List<User>>() {});
      ObservableList<String> names = FXCollections.observableArrayList();
      for(User u : users){
        names.add(u.getFullName());

      }
      buyersList.setItems(names);


    } catch (Exception e){
      e.printStackTrace();
    }

  }
  public void handleSaveButton(){
    String selectedBuyer = buyersList.getValue();
    String ownerName = trade.getResidentName();

    if (selectedBuyer == null || selectedBuyer.equals(ownerName)) {
      textLabel.setVisible(true);
      textLabel.setText("You need to select a different buyer");
      textLabel.setStyle("-fx-text-fill: red; -fx-font-size: 13px; -fx-font-weight: bold");
      return;
    }

    if (onTradeConfirmed != null) {
      onTradeConfirmed.accept(trade);
    }

    Stage stage = (Stage) saveButton.getScene().getWindow();
    stage.close();
  }
  public void handleCancelButton(){
    Stage stage = (Stage) cancelButton.getScene().getWindow();
    stage.close();


  }
}
