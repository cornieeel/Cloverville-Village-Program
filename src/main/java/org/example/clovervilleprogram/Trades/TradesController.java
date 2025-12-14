package org.example.clovervilleprogram.Trades;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.example.clovervilleprogram.Users.User;

import java.io.File;
import java.util.Date;
import java.util.List;

public class TradesController
{


  @FXML private Button addTrade;
  @FXML private Button resetFields;
  @FXML private Label noTradesAvailable;
  @FXML private ScrollPane scrollPane;
  @FXML private VBox vBox;
  @FXML private ComboBox<String> residentName;
  @FXML private TextField priceOfProduct;
  @FXML private TextField goodToOffer;
  @FXML private DatePicker dateOfActivity;

  private final File usersFile = new File("users.json");

  public void initialize(){
    loadCitizensFromJson();
  }

  @FXML
  private void handleAddTrade() {

    if (residentName.getValue() == null ||
        goodToOffer.getText().isEmpty() ||
        priceOfProduct.getText().isEmpty()) {
      return;
    }

    Label owner = new Label("Owner: " + residentName.getValue());
    Label product = new Label(goodToOffer.getText());
    Label price = new Label(priceOfProduct.getText());

    Button accept = new Button("✔");
    Button cancel = new Button("✖");

    Trades trades = new Trades(
        residentName.getValue(),
        goodToOffer.getText(),
        Integer.parseInt(priceOfProduct.getText()),
        dateOfActivity.getValue().toString()
    );

    Region leftSpacer = new Region();
    HBox.setHgrow(leftSpacer, Priority.ALWAYS);

    Region rightSpacer = new Region();
    HBox.setHgrow(rightSpacer, Priority.ALWAYS);

    HBox centerBox = new HBox(7, product, price);
    centerBox.setAlignment(Pos.CENTER);

    owner.getStyleClass().add("trade-owner");
    product.getStyleClass().add("trade-product");
    price.getStyleClass().add("trade-price");
    accept.getStyleClass().add("trade-accept");
    cancel.getStyleClass().add("trade-cancel");

    ToolBar toolBar = new ToolBar(
        owner,
        leftSpacer,
        centerBox,
        rightSpacer,
        accept,
        cancel
    );
    toolBar.setStyle("-fx-background-color: #CBEACB ; -fx-border-color: #44E151; -fx-min-height: 31px; -fx-min-width: 441px");

    accept.setOnAction(e -> {
      System.out.println("Accepted: " + trades);
    });

    cancel.setOnAction(e -> vBox.getChildren().remove(toolBar));

    vBox.getChildren().add(toolBar);
  }
  @FXML private void handleResetFields(){

  }
  public void handleExportButton(){

  }

  public void loadCitizensFromJson(){
    if(!usersFile.exists()) return;
    try
    {
      ObjectMapper mapper = new ObjectMapper();
      List<User> users = mapper.readValue(usersFile, new TypeReference<List<User>>() {});

      ObservableList<String> names = FXCollections.observableArrayList();
      for(User u : users){
        names.add(u.getFullName());
      }
      residentName.setItems(names);
    } catch (Exception e){
      e.printStackTrace();
    }


  }
}
