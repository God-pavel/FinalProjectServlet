package com.pasha.trainingcourse.controller.command;

import com.pasha.trainingcourse.model.service.ProductService;

import javax.servlet.http.HttpServletRequest;

public class StorageCommand implements Command {
    private ProductService productService;

    StorageCommand(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        request.setAttribute("products", productService.getAllProducts());
        return "/storage.jsp";
    }
}