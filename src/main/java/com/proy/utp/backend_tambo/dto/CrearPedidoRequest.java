package com.proy.utp.backend_tambo.dto;

import java.util.List;

public class CrearPedidoRequest {
    private List<Long> productosIds;

    public List<Long> getProductosIds() {
        return productosIds;
    }

    public void setProductosIds(List<Long> productosIds) {
        this.productosIds = productosIds;
    }
}