package com.proy.utp.backend_tambo.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public class CrearPedidoRequest {

    @NotEmpty(message = "La lista de productos no puede estar vacía")
    private List<Long> productosIds;

    @NotEmpty(message = "La lista de cantidades no puede estar vacía")
    @Size(min = 1, message = "Debe haber al menos una cantidad")
    private List<Integer> cantidades;

    private String estado;   // nuevo campo
    private String fecha;    // nuevo campo (puedes usar LocalDate si prefieres)

    // Getters y setters
    public List<Long> getProductosIds() {
        return productosIds;
    }

    public void setProductosIds(List<Long> productosIds) {
        this.productosIds = productosIds;
    }

    public List<Integer> getCantidades() {
        return cantidades;
    }

    public void setCantidades(List<Integer> cantidades) {
        this.cantidades = cantidades;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
