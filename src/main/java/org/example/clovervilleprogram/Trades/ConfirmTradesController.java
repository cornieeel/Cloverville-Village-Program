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
import javafx.stage.Stage;
import org.example.clovervilleprogram.Users.User;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ConfirmTradesController {

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
  private final File usersPoints = new File("userPoints.json");
  private Map<String, Integer> userPointsMap = new HashMap<>();

  @FXML
  public void initialize() {
    loadBuyersFromJson();
    loadUserPoints();

    buyersList.setOnAction(e -> displayBuyerPoints());
  }

  public void setTrade(Trades trade) {
    this.trade = trade;

    fullNameEdit.setText(trade.getResidentName()); // Seller
    pointsEdit.setText(String.valueOf(trade.getPrice()));
    tasksEdit.setText(trade.getGoodToOffer());
  }

  public void setOnTradeConfirmed(Consumer<Trades> callback) {
    this.onTradeConfirmed = callback;
  }

  private void loadBuyersFromJson() {
    if (!buyers.exists()) return;
    try {
      ObjectMapper mapper = new ObjectMapper();
      List<User> users = mapper.readValue(buyers, new TypeReference<List<User>>() {});
      ObservableList<String> names = FXCollections.observableArrayList();
      for (User u : users) {
        names.add(u.getFullName());
      }
      buyersList.setItems(names);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void loadUserPoints() {
    if (!usersPoints.exists()) return;
    try {
      ObjectMapper mapper = new ObjectMapper();
      userPointsMap = mapper.readValue(usersPoints, new TypeReference<Map<String, Integer>>() {});
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void displayBuyerPoints() {
    String selectedBuyer = buyersList.getValue();
    if (selectedBuyer != null) {
      int points = userPointsMap.getOrDefault(selectedBuyer, 0);
      textLabel.setVisible(true);
      textLabel.setText(selectedBuyer + " has " + points + " points");
      textLabel.setStyle("-fx-text-fill: green; -fx-font-size: 13px; -fx-font-weight: bold");
    } else {
      textLabel.setVisible(false);
    }
  }

  @FXML
  public void handleSaveButton() {
    String buyerName = buyersList.getValue();
    String sellerName = trade.getResidentName();
    int tradePrice = trade.getPrice();

    if (buyerName == null || buyerName.equals(sellerName)) {
      textLabel.setVisible(true);
      textLabel.setText("You need to select a different buyer");
      textLabel.setStyle("-fx-text-fill: red; -fx-font-size: 13px; -fx-font-weight: bold");
      return;
    }

    int buyerPoints = userPointsMap.getOrDefault(buyerName, 0);

    if (buyerPoints < tradePrice) {
      textLabel.setVisible(true);
      textLabel.setText(buyerName + " does not have enough points to buy this item.");
      textLabel.setStyle("-fx-text-fill: red; -fx-font-size: 13px; -fx-font-weight: bold");
      return;
    }

    // Deduct points from buyer
    userPointsMap.put(buyerName, buyerPoints - tradePrice);

    // Add points to seller
    int sellerPoints = userPointsMap.getOrDefault(sellerName, 0);
    userPointsMap.put(sellerName, sellerPoints + tradePrice);

    // Save updated points
    saveUserPoints();

    if (onTradeConfirmed != null) {
      onTradeConfirmed.accept(trade);
    }

    Stage stage = (Stage) saveButton.getScene().getWindow();
    stage.close();
  }

  private void saveUserPoints() {
    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.writerWithDefaultPrettyPrinter().writeValue(usersPoints, userPointsMap);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FXML
  public void handleCancelButton() {
    Stage stage = (Stage) cancelButton.getScene().getWindow();
    stage.close();
  }
}
