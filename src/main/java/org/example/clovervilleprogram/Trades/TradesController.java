package org.example.clovervilleprogram.Trades;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.clovervilleprogram.Users.User;

import java.io.File;
import java.util.List;

/**
 * Controller for managing trades between users.
 * Handles adding trades, filtering trades, exporting to JSON,
 * and displaying trade options with accept/cancel functionality.
 */
public class TradesController {

  // Buttons for adding a trade and resetting input fields
  @FXML private Button addTrade;
  @FXML private Button resetFields;

  // Labels for error messages and "no trades" notification
  @FXML private Label noTradesAvailable;
  @FXML private Label errorLabel;
  @FXML private Label errorLabel1;

  // ScrollPane and VBox for displaying trades dynamically
  @FXML private ScrollPane scrollPane;
  @FXML private VBox vBox;

  // Input fields for creating a trade
  @FXML private ComboBox<String> residentName; // Seller name
  @FXML private TextField priceOfProduct;      // Trade price
  @FXML private TextField goodToOffer;         // Item to offer
  @FXML private DatePicker dateOfActivity;     // Trade date
  @FXML private TextField searchBar;           // Search/filter trades

  // Observable list to store all trades
  private final ObservableList<Trades> tradesList = FXCollections.observableArrayList();

  // JSON files for trades and users
  private final File tradesFile = new File("trades.json");
  private final File usersFile = new File("users.json");

  /**
   * Initializes the controller by loading users and trades from JSON,
   * and sets up the search/filter functionality.
   */
  public void initialize() {
    loadCitizensFromJson();
    loadTradesFromJson();
    setupSearchBar();
  }

  /**
   * Sets up listener for the search bar to filter trades dynamically.
   */
  private void setupSearchBar() {
    searchBar.textProperty().addListener((obs, oldVal, newVal) -> updateVBoxFilter(newVal));
  }

  /**
   * Updates the VBox to display only trades matching the filter text.
   *
   * @param filterText text entered in the search bar
   */
  private void updateVBoxFilter(String filterText) {
    vBox.getChildren().clear();
    String filter = filterText.toLowerCase().trim();
    for (Trades trade : tradesList) {
      if (filter.isEmpty() || trade.getResidentName().toLowerCase().contains(filter)
          || trade.getGoodToOffer().toLowerCase().contains(filter)) {
        addTradeToVBox(trade);
      }
    }
  }

  /**
   * Handles adding a new trade from input fields.
   * Validates that all fields are filled and adds trade to list.
   */
  @FXML
  private void handleAddTrade() {
    if (residentName.getValue() == null || goodToOffer.getText().isEmpty() || priceOfProduct.getText().isEmpty()) {
      errorLabel.setVisible(true);
      errorLabel.setText("You need to fill all the fields!");
      errorLabel.setStyle("-fx-text-fill: red");
      return;
    }

    int price;
    try {
      price = Integer.parseInt(priceOfProduct.getText());
    } catch (NumberFormatException e) {
      return;
    }

    Trades trade = new Trades(
        residentName.getValue(),
        goodToOffer.getText(),
        price,
        dateOfActivity.getValue() != null ? dateOfActivity.getValue().toString() : ""
    );

    tradesList.add(trade);
    updateVBoxFilter(searchBar.getText());
    clearFields();
  }

  /**
   * Creates a visual representation of a trade in the VBox with
   * accept and cancel buttons.
   *
   * @param trade the trade to display
   */
  private void addTradeToVBox(Trades trade) {
    Label owner = new Label("Owner: " + trade.getResidentName());
    Label product = new Label(trade.getGoodToOffer());
    Label price = new Label(String.valueOf(trade.getPrice()));

    Button accept = new Button("✔");
    Button cancel = new Button("✖");

    // Spacers to arrange elements in HBox
    Region leftSpacer = new Region();
    HBox.setHgrow(leftSpacer, Priority.ALWAYS);
    Region rightSpacer = new Region();
    HBox.setHgrow(rightSpacer, Priority.ALWAYS);

    // Center box to hold product and price
    HBox centerBox = new HBox(7, product, price);
    centerBox.setAlignment(Pos.CENTER);

    // Add CSS classes for styling
    owner.getStyleClass().add("trade-owner");
    product.getStyleClass().add("trade-product");
    price.getStyleClass().add("trade-price");
    accept.getStyleClass().add("trade-accept");
    cancel.getStyleClass().add("trade-cancel");

    // Create toolbar containing the trade information and buttons
    ToolBar toolBar = new ToolBar(owner, leftSpacer, centerBox, rightSpacer, accept, cancel);
    toolBar.setStyle("-fx-background-color: #CBEACB ; -fx-border-color: #44E151; -fx-min-height: 31px; -fx-min-width: 441px");

    // Accept button opens a confirmation window
    accept.setOnAction(e -> {
      try {
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("/org/example/clovervilleprogram/TradesPage/ConfirmTrades.fxml"));
        Parent root = fxmlloader.load();

        ConfirmTradesController controller = fxmlloader.getController();
        controller.setTrade(trade);
        controller.setOnTradeConfirmed(t -> {
          tradesList.remove(t);
          updateVBoxFilter(searchBar.getText());
        });

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Accept the trade");
        stage.show();
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    });

    // Cancel button removes the trade immediately
    cancel.setOnAction(e -> {
      tradesList.remove(trade);
      updateVBoxFilter(searchBar.getText());
    });

    // Add the toolbar to the VBox
    vBox.getChildren().add(toolBar);
  }

  /**
   * Resets all input fields and hides error label.
   */
  @FXML
  private void handleResetFields() {
    residentName.getSelectionModel().clearSelection();
    goodToOffer.clear();
    priceOfProduct.clear();
    dateOfActivity.setValue(null);
    errorLabel.setVisible(false);
  }

  /**
   * Exports current trades to trades.json file.
   */
  public void handleExportButton() {
    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
      mapper.writeValue(tradesFile, tradesList);
      errorLabel1.setVisible(true);
      errorLabel1.setText("Exported to JSON successfully!");
      errorLabel1.setStyle("-fx-text-fill: green");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Loads trades from trades.json into tradesList.
   */
  public void loadTradesFromJson() {
    if (!tradesFile.exists()) return;
    try {
      ObjectMapper mapper = new ObjectMapper();
      List<Trades> trades = mapper.readValue(tradesFile, new TypeReference<List<Trades>>() {});
      tradesList.setAll(trades);
      updateVBoxFilter(searchBar.getText());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Loads users from users.json into the residentName ComboBox.
   */
  public void loadCitizensFromJson() {
    if (!usersFile.exists()) return;
    try {
      ObjectMapper mapper = new ObjectMapper();
      List<User> users = mapper.readValue(usersFile, new TypeReference<List<User>>() {});
      ObservableList<String> names = FXCollections.observableArrayList();
      for (User u : users) names.add(u.getFullName());
      residentName.setItems(names);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Clears the input fields after adding a trade.
   */
  private void clearFields() {
    residentName.getSelectionModel().clearSelection();
    goodToOffer.clear();
    priceOfProduct.clear();
    dateOfActivity.setValue(null);
  }
}
