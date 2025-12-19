package org.example.clovervilleprogram.Trades;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model class representing a Trade between users.
 * Contains seller name, item offered, price, and trade date.
 * Uses JavaFX properties for TableView and data binding support.
 */
public class Trades {

  // Name of the user offering the item (seller)
  private final StringProperty residentName = new SimpleStringProperty();

  // Description of the item being offered for trade
  private final StringProperty goodToOffer = new SimpleStringProperty();

  // Price of the trade in points
  private final IntegerProperty price = new SimpleIntegerProperty();

  // Date of the trade
  private final StringProperty date = new SimpleStringProperty();

  // No-argument constructor required for JavaFX and Jackson
  public Trades() {}

  /**
   * Full constructor to initialize a trade.
   *
   * @param residentName seller's name
   * @param goodToOffer item being offered
   * @param price trade price in points
   * @param date date of the trade
   */
  public Trades(String residentName, String goodToOffer, int price, String date) {
    this.residentName.set(residentName);
    this.goodToOffer.set(goodToOffer);
    this.price.set(price);
    this.date.set(date);
  }

  // Getter and setter for residentName
  public String getResidentName() { return residentName.get(); }
  public void setResidentName(String value) { residentName.set(value); }
  public StringProperty residentNameProperty() { return residentName; }

  // Getter and setter for goodToOffer
  public String getGoodToOffer() { return goodToOffer.get(); }
  public void setGoodToOffer(String value) { goodToOffer.set(value); }
  public StringProperty goodToOfferProperty() { return goodToOffer; }

  // Getter and setter for price
  public int getPrice() { return price.get(); }
  public void setPrice(int value) { price.set(value); }
  public IntegerProperty priceProperty() { return price; }

  // Getter and setter for date
  public String getDate() { return date.get(); }
  public void setDate(String value) { date.set(value); }
  public StringProperty dateProperty() { return date; }
}
