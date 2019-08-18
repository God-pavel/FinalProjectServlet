package com.pasha.trainingcourse.controller.command;


import com.pasha.trainingcourse.controller.validator.AmountValidator;
import com.pasha.trainingcourse.controller.validator.PriceValidator;
import com.pasha.trainingcourse.controller.validator.ProductNameValidator;
import com.pasha.trainingcourse.controller.validator.Result;
import com.pasha.trainingcourse.model.entity.Product;
import com.pasha.trainingcourse.model.entity.enums.ProductType;
import com.pasha.trainingcourse.model.service.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class AddProductCommand implements Command {
    private static final Logger log = LogManager.getLogger();
    private ProductService productService;

    public AddProductCommand(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public String execute(HttpServletRequest request) {

        Set<String> types = new HashSet<>();
        Arrays.stream(ProductType.values()).forEach(type -> types.add(type.name()));
        request.setAttribute("types", types);

        if (!(Objects.nonNull(request.getParameter("name")) &&
                Objects.nonNull(request.getParameter("price")) &&
                Objects.nonNull(request.getParameter("amount")) &&
                Objects.nonNull(request.getParameter("type")))) {
            return "/WEB-INF/pages/merchandise.jsp";
        }

        String name = request.getParameter("name");
        BigDecimal price = new BigDecimal(request.getParameter("price"));
        Long amount = Long.parseLong(request.getParameter("amount"));
        ProductType type = ProductType.valueOf(request.getParameter("type"));

        Result checkProductName = new ProductNameValidator().validate(name);

        if (!checkProductName.isValid()) {
            request.setAttribute("message", checkProductName.getMessage());
            return "/WEB-INF/pages/merchandise.jsp";
        }

        Result checkAmount = new AmountValidator().validate(amount);

        if (!checkAmount.isValid()) {
            request.setAttribute("message", checkAmount.getMessage());
            return "/WEB-INF/pages/merchandise.jsp";
        }

        Result checkPrice = new PriceValidator().validate(price);

        if (!checkPrice.isValid()) {
            request.setAttribute("message", checkPrice.getMessage());
            return "/WEB-INF/pages/merchandise.jsp";
        }

        Product product = new Product(name, price, amount, type);

        if (productService.createProduct(product)) {
            log.info("Product successfully created");
            return "redirect:/storage";
        } else {
            log.info("Product can not be created");
            request.setAttribute("message", "Product exist!");
            return "/WEB-INF/pages/merchandise.jsp";
        }
    }
}