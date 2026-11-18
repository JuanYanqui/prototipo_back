package com.comercialnova.dto;

import java.util.ArrayList;
import java.util.List;

public class Dashboard {
    private List<String> nombresProductos = new ArrayList<>();
    private List<Integer> stocksProductos = new ArrayList<>();
    private long stockNormal;
    private long stockBajo;
    private long stockAgotado;

    private List<String> nombresTopProductos = new ArrayList<>();
    private List<Integer> cantidadesVendidas = new ArrayList<>();

    // Getters y Setters
    public List<String> getNombresProductos() { return nombresProductos; }
    public void setNombresProductos(List<String> nombresProductos) { this.nombresProductos = nombresProductos; }
    public List<Integer> getStocksProductos() { return stocksProductos; }
    public void setStocksProductos(List<Integer> stocksProductos) { this.stocksProductos = stocksProductos; }
    public long getStockNormal() { return stockNormal; }
    public void setStockNormal(long stockNormal) { this.stockNormal = stockNormal; }
    public long getStockBajo() { return stockBajo; }
    public void setStockBajo(long stockBajo) { this.stockBajo = stockBajo; }
    public long getStockAgotado() { return stockAgotado; }
    public void setStockAgotado(long stockAgotado) { this.stockAgotado = stockAgotado; }
    public List<String> getNombresTopProductos() { return nombresTopProductos; }
    public void setNombresTopProductos(List<String> nombresTopProductos) { this.nombresTopProductos = nombresTopProductos; }
    public List<Integer> getCantidadesVendidas() { return cantidadesVendidas; }
    public void setCantidadesVendidas(List<Integer> cantidadesVendidas) { this.cantidadesVendidas = cantidadesVendidas; }
}