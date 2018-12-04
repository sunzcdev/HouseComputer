package net.sunzc.housecomputer.entity;


import net.sunzc.housecomputer.db.inject.Column;
import net.sunzc.housecomputer.db.inject.Table;

import java.io.Serializable;

/**
 * @author Administrator
 * @date 2018-12-04 12:58
 **/
@Table
public class HouseType implements Serializable {
    private static final long serialVersionUID = -3363367503963973241L;
    @Column(primaryKey = true, autoIncrement = true)
    private long id;
    @Column
    private String name;
    @Column
    private double price;
    @Column
    private double square;
    @Column
    private double loanRate = 0.06;
    @Column
    private double lat;
    @Column
    private double lng;
    /**
     * 0 新房，1 旧房
     */
    @Column
    private int type;
    @Column
    private double highPrice;
    @Column
    private int loanNum = 4;

    public HouseType(String name, double price, double square) {
        this.name = name;
        this.price = price;
        this.square = square;
        this.highPrice = price;
    }

    @Override
    public String toString() {
        return "HouseType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", square=" + square +
                ", loanRate=" + loanRate +
                ", lat=" + lat +
                ", lng=" + lng +
                ", type=" + type +
                ", highPrice=" + highPrice +
                ", loanNum=" + loanNum +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSquare() {
        return square;
    }

    public void setSquare(double square) {
        this.square = square;
    }

    public double getLoanRate() {
        return loanRate;
    }

    public void setLoanRate(double loanRate) {
        this.loanRate = loanRate;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLoanNum() {
        return loanNum;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    public void setLoanNum(int loanNum) {
        this.loanNum = loanNum;
    }
}
