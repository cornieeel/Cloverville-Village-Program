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

  private final ObservableList<Trades> tradesList = FXCollections.observableArrayList();

  private final File tradesFile = new File("trades.json");
  private final File usersFile = new File("users.json");

  public void initialize(){
    loadCitizensFromJson();
    loadTradesFromJson();
  }

  @FXML
  private void handleAddTrade() {
    if (residentName.getValue() == null ||
        goodToOffer.getText().isEmpty() ||
        priceOfProduct.getText().isEmpty()) {
      return;
    }

    Trades trade = new Trades(
        residentName.getValue(),
        goodToOffer.getText(),
        Integer.parseInt(priceOfProduct.getText()),
        dateOfActivity.getValue().toString()
    );

    tradesList.add(trade);
    addTradeToVBox(trade);
  }
  private void addTradeToVBox(Trades trade) {
    Label owner = new Label("Owner: " + trade.getResidentName());
    Label product = new Label(trade.getGoodToOffer());
    Label price = new Label(String.valueOf(trade.getPrice()));

    Button accept = new Button("✔");
    Button cancel = new Button("✖");

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

    accept.setOnAction(e -> {System.out.println("Accepted: " + trade);
      try
      {
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource(
            "/org/example/clovervilleprogram/TradesPage/ConfirmTrades.fxml"));
        Parent root = fxmlloader.load();

        ConfirmTradesController controller = fxmlloader.getController();

        controller.setTrade(trade);
        controller.setOnTradeConfirmed(t -> {
          tradesList.remove(t);
          vBox.getChildren().remove(toolBar);
        });

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Accept the trade");
        stage.show();
      }
      catch (Exception ex)
      {
        throw new RuntimeException(ex);
      }
    });
    cancel.setOnAction(e -> {
      vBox.getChildren().remove(toolBar);
      tradesList.remove(trade);
    });


    vBox.getChildren().add(toolBar);
  }

  @FXML private void handleResetFields(){

  }
  public void handleExportButton(){
    try{
      ObjectMapper mapper = new ObjectMapper();
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
      mapper.writeValue(tradesFile, tradesList);
  } catch (Exception e){
    e.printStackTrace();
    }
  }
  public void loadTradesFromJson(){
    ObjectMapper mapper = new ObjectMapper();

    if(tradesFile.exists()){
      try{
        List<Trades> trades = mapper.readValue(tradesFile, new TypeReference<List<Trades>>() {});
        tradesList.setAll(trades);
        for (Trades trade : tradesList) {
          addTradeToVBox(trade);
        }
      } catch (Exception e){
        e.printStackTrace();
      }
    }
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
