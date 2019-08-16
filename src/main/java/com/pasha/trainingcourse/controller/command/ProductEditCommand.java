package com.pasha.trainingcourse.controller.command;

import com.pasha.trainingcourse.controller.validator.AmountValidator;
import com.pasha.trainingcourse.controller.validator.PriceValidator;
import com.pasha.trainingcourse.controller.validator.Result;
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

        String amount = request.getParameter("amount");
        String price = request.getParameter("price");

        if (!(Objects.nonNull(amount) &&
                Objects.nonNull(price))) {
            return "/product_edit.jsp";
        }

        Result checkAmount = new AmountValidator().validate(Long.parseLong(amount));

        if (!checkAmount.isValid()) {
            request.setAttribute("message", checkAmount.getMessage());
            return "/product_edit.jsp";
        }

        Result checkPrice = new PriceValidator().validate(new BigDecimal(price));

        if (!checkPrice.isValid()) {
            request.setAttribute("message", checkPrice.getMessage());
            return "/product_edit.jsp";
        }

        product.setAmount(Long.parseLong(amount));
        product.setPrice(new BigDecimal(price));

        if (!productService.updateProduct(product)) {
            request.setAttribute("message", "Product exist!");
            return "/product_edit.jsp";
        }

        return "redirect:/storage";
    }
}