package org.example.clovervilleprogram.Trades;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Trades {

  private final StringProperty residentName = new SimpleStringProperty();
  private final StringProperty goodToOffer = new SimpleStringProperty();
  private final IntegerProperty price = new SimpleIntegerProperty();
  private final StringProperty date = new SimpleStringProperty();

  public Trades() {}

  public Trades(String residentName, String goodToOffer, int price, String date) {
    this.residentName.set(residentName);
    this.goodToOffer.set(goodToOffer);
    this.price.set(price);
    this.date.set(date);
  }

  public String getResidentName() { return residentName.get(); }
  public void setResidentName(String value) { residentName.set(value); }
  public StringProperty residentNameProperty() { return residentName; }

  public String getGoodToOffer() { return goodToOffer.get(); }
  public void setGoodToOffer(String value) { goodToOffer.set(value); }
  public StringProperty goodToOfferProperty() { return goodToOffer; }

  public int getPrice() { return price.get(); }
  public void setPrice(int value) { price.set(value); }
  public IntegerProperty priceProperty() { return price; }

  public String getDate() { return date.get(); }
  public void setDate(String value) { date.set(value); }
  public StringProperty dateProperty() { return date; }
}
