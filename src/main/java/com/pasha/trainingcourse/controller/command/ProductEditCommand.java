package com.pasha.trainingcourse.controller.command;

import com.pasha.trainingcourse.model.entity.Product;
import com.pasha.trainingcourse.model.service.ProductService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Objects;

public class ProductEditCommand implements Command {
    private ProductService productService;

    ProductEditCommand(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public String execute(HttpServletRequest request) {

        String productId = request.getRequestURI().replaceAll(".*/productEdit/", "");
        Product product = productService.getProductById(Long.parseLong(productId));
        request.setAttribute("product", product);
        System.out.println("here1");

        if (!(Objects.nonNull(request.getParameter("price")) &&
                Objects.nonNull(request.getParameter("amount")))) {
            System.out.println("here2");
            return "/product_edit.jsp";
        }
        System.out.println("here3");
        product.setAmount(Long.parseLong(request.getParameter("amount")));
        product.setPrice(new BigDecimal(request.getParameter("price")));
        if (!productService.updateProduct(product)) {
            request.setAttribute("error", true);
            return "/product_edit.jsp";
        }

        return "redirect:/storage";
    }
}