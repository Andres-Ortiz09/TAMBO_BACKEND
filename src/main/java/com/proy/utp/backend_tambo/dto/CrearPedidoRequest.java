package com.proy.utp.backend_tambo.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class CrearPedidoRequest {

    @NotEmpty(message = "La lista de productos no puede estar vacía")
    private List<Long> productosIds;

    @NotEmpty(message = "La lista de cantidades no puede estar vacía")
    private List<Integer> cantidades;

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
}
