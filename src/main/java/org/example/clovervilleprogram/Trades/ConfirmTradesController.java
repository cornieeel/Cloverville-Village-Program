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

/**
 * Controller for confirming a trade between two users.
 * Handles buyer selection, point validation, point transfer,
 * and final confirmation of the trade.
 */
public class ConfirmTradesController {

  // Buttons for saving or canceling the trade
  @FXML private Button saveButton;
  @FXML private Button cancelButton;

  // Text fields displaying trade details
  @FXML private TextField fullNameEdit;
  @FXML private TextField pointsEdit;
  @FXML private TextField tasksEdit;

  // ComboBox for selecting the buyer
  @FXML private ComboBox<String> buyersList;

  // Label for displaying information or error messages
  @FXML private Label textLabel;

  // Callback executed when a trade is confirmed
  private Consumer<Trades> onTradeConfirmed;

  // The trade being confirmed
  private Trades trade;

  // JSON files for users and their points
  private final File buyers = new File("users.json");
  private final File usersPoints = new File("userPoints.json");

  // Map storing user names and their current points
  private Map<String, Integer> userPointsMap = new HashMap<>();

  /**
   * Initializes the controller by loading buyers and user points.
   * Sets up listener for buyer selection.
   */
  @FXML
  public void initialize() {
    loadBuyersFromJson();
    loadUserPoints();

    buyersList.setOnAction(e -> displayBuyerPoints());
  }

  /**
   * Sets the trade details and populates the UI fields.
   *
   * @param trade the trade to be confirmed
   */
  public void setTrade(Trades trade) {
    this.trade = trade;

    // Display seller name, price, and offered task
    fullNameEdit.setText(trade.getResidentName()); // Seller
    pointsEdit.setText(String.valueOf(trade.getPrice()));
    tasksEdit.setText(trade.getGoodToOffer());
  }

  /**
   * Sets a callback that is triggered when the trade is confirmed.
   *
   * @param callback the confirmation callback
   */
  public void setOnTradeConfirmed(Consumer<Trades> callback) {
    this.onTradeConfirmed = callback;
  }

  /**
   * Loads buyers (users) from users.json into the ComboBox.
   */
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

  /**
   * Loads user points from userPoints.json into a map.
   */
  private void loadUserPoints() {
    if (!usersPoints.exists()) return;
    try {
      ObjectMapper mapper = new ObjectMapper();
      userPointsMap = mapper.readValue(usersPoints, new TypeReference<Map<String, Integer>>() {});
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Displays the selected buyer's current points.
   */
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

  /**
   * Handles confirming the trade.
   * Validates buyer selection and available points,
   * updates user points, and saves changes.
   */
  @FXML
  public void handleSaveButton() {
    String buyerName = buyersList.getValue();
    String sellerName = trade.getResidentName();
    int tradePrice = trade.getPrice();

    // Ensure buyer is selected and not the seller
    if (buyerName == null || buyerName.equals(sellerName)) {
      textLabel.setVisible(true);
      textLabel.setText("You need to select a different buyer");
      textLabel.setStyle("-fx-text-fill: red; -fx-font-size: 13px; -fx-font-weight: bold");
      return;
    }

    int buyerPoints = userPointsMap.getOrDefault(buyerName, 0);

    // Check if buyer has enough points
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

    // Persist updated user points
    saveUserPoints();

    // Notify listener that trade was confirmed
    if (onTradeConfirmed != null) {
      onTradeConfirmed.accept(trade);
    }

    // Close the confirmation window
    Stage stage = (Stage) saveButton.getScene().getWindow();
    stage.close();
  }

  /**
   * Saves updated user points back to userPoints.json.
   */
  private void saveUserPoints() {
    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.writerWithDefaultPrettyPrinter().writeValue(usersPoints, userPointsMap);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Cancels the trade confirmation and closes the window.
   */
  @FXML
  public void handleCancelButton() {
    Stage stage = (Stage) cancelButton.getScene().getWindow();
    stage.close();
  }
}
