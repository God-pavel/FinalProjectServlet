package com.pasha.trainingcourse.model.service;

import com.pasha.trainingcourse.model.dao.CheckDao;
import com.pasha.trainingcourse.model.dao.DaoFactory;
import com.pasha.trainingcourse.model.entity.Check;
import com.pasha.trainingcourse.model.entity.Product;
import com.pasha.trainingcourse.model.entity.User;
import com.pasha.trainingcourse.model.exception.CheckCantBeDeleted;
import com.pasha.trainingcourse.model.exception.NotEnoughProductsException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CheckService {

    private static final Logger log = LogManager.getLogger();
    private DaoFactory daoFactory = DaoFactory.getInstance();
    private final ProductService productService;


    public CheckService(ProductService productService) {
        this.productService = productService;
    }

    public List<Check> getAllChecks() {
        try (CheckDao dao = daoFactory.createCheckDao()) {
            return dao.findAll();
        } catch (Exception e) {
            log.warn("Cant get checks!");
            return Collections.emptyList();
        }
    }

    public Check getTemporaryCheckById(Long id) {
        try (CheckDao checkDao = daoFactory.createCheckDao()) {
            return checkDao.findTempById(id);
        } catch (Exception e) {
            log.warn("Cant get check!");
            return null;
        }
    }

    public Check getCheckById(Long id) {
        try (CheckDao checkDao = daoFactory.createCheckDao()) {
            return checkDao.findById(id);
        } catch (Exception e) {
            log.warn("Cant get check!");

            return null;
        }
    }

    private void updateTempCheck(Check check) {
        try (CheckDao checkDao = daoFactory.createCheckDao()) {
            checkDao.updateTemp(check);
        } catch (Exception e) {
            log.warn("Cant update check!");
        }
    }

    public void updateCheck(Check check) {
        try (CheckDao checkDao = daoFactory.createCheckDao()) {
            checkDao.update(check);
        } catch (Exception e) {
            log.warn("Cant update check!");
        }
    }

    private void deleteTempCheck(Check check) {
        try (CheckDao checkDao = daoFactory.createCheckDao()) {
            checkDao.deleteTemp(check.getId());
        } catch (Exception e) {
            log.warn("Cant delete check!");
        }
    }

    private void deleteCheck(Check check) {
        try (CheckDao checkDao = daoFactory.createCheckDao()) {
            checkDao.delete(check.getId());
        } catch (Exception e) {
            log.warn("Cant delete check!");
        }
    }

    void deleteUncompletedChecks() {
        try (CheckDao checkDao = daoFactory.createCheckDao()) {
            checkDao.findAllTemp()
                    .forEach(check -> checkDao.deleteTemp(check.getId()));
        } catch (Exception e) {
            log.warn("Cant delete temp checks!");

        }
    }

    public Check createTempCheck(User user) {
        Check tempCheck = new Check(user, LocalDateTime.now(), new BigDecimal(0.0)
                .setScale(2, RoundingMode.HALF_UP), true, new HashMap<>());
        try (CheckDao dao = daoFactory.createCheckDao()) {
            dao.createTemp(tempCheck);
            tempCheck = dao.findLastTemp();
            return tempCheck;
        } catch (Exception e) {
            log.warn(e.getMessage());
            e.getStackTrace();
            return null;
        }
    }

    private void createCheck(Check check){
        try (CheckDao dao = daoFactory.createCheckDao()) {
            dao.create(check);

        } catch (Exception e) {
            log.warn(e.getMessage());
            e.getStackTrace();

        }
    }

    public void addProductToCheckByName(Long checkId, String name, Long amount) throws IllegalArgumentException {
        Check check = getTemporaryCheckById(checkId);
        Map<Product, Long> products = check.getProductAmount();

        if (products.containsKey(productService.getProductByName(name))) {
            Long newAmount = products.get(productService.getProductByName(name)) + amount;
            products.replace(productService.getProductByName(name), newAmount);
        } else {
            products.put(productService.getProductByName(name), amount);
        }
        if (productService.checkProductAvailabilityByName(name,
                products.get(productService.getProductByName(name)))) {
            check.setTotal(new BigDecimal(calcTotal(products)));
            updateTempCheck(check);
        } else {
            log.warn("Not enough products in storage!");
            throw new NotEnoughProductsException("Not enough products in storage!");
        }
    }

    public void addProductToCheckById(Long checkId, Long id, Long amount) throws IllegalArgumentException {
        Check check = getTemporaryCheckById(checkId);
        Map<Product, Long> products = check.getProductAmount();

        if (products.containsKey(productService.getProductById(id))) {
            Long newAmount = products.get(productService.getProductById(id)) + amount;
            products.replace(productService.getProductById(id), newAmount);
        } else {
            products.put(productService.getProductById(id), amount);
        }
        if (productService.checkProductAvailabilityById(id,
                products.get(productService.getProductById(id)))) {
            check.setTotal(new BigDecimal(calcTotal(products)));
            updateTempCheck(check);
        } else {
            log.warn("Not enough products in storage!");
            throw new NotEnoughProductsException("Not enough products in storage!");
        }
    }

    private double calcHelper(Product product, Long amount) {
        return product.getPrice().doubleValue() * amount;
    }

    private double calcTotal(Map<Product, Long> products) {
        List<Double> sums = products.keySet().stream()
                .map(key -> calcHelper(key, products.get(key)))
                .collect(Collectors.toList());
        return sums.stream()
                .reduce(0.0, (x, y) -> x + y);
    }

    public void closeCheck(Long checkId) throws NotEnoughProductsException{
        Check check = getTemporaryCheckById(checkId);
        deleteTempCheck(check);
        check.getProductAmount().forEach(productService::takeAway);
        check.setToDelete(true);
        if(check.getTotal().compareTo(new BigDecimal(0))==0){return;}
        createCheck(check);
    }


    public void deleteCheckById(Long id) throws CheckCantBeDeleted {
        log.info(getCheckById(id).isToDelete());
        if (!getCheckById(id).isToDelete()) {
            throw new CheckCantBeDeleted("This check already in report so it cant be deleted!");
        } else {
            getCheckById(id).getProductAmount().forEach(productService::takeBack);
            deleteCheck(getCheckById(id));
            log.info("Check was deleted.");
        }
    }

    public void deleteProductFromCheck(Long checkId, String name) throws CheckCantBeDeleted {
        Check check = getCheckById(checkId);
        if (!check.isToDelete()) {
            throw new CheckCantBeDeleted("This check already in report so product cant be deleted!");
        } else {
            Product productToDelete = productService.getProductByName(name);
            Long amountToDelete = check.getProductAmount().get(productToDelete);
            check.getProductAmount().remove(productToDelete);
            if (check.getProductAmount().isEmpty()) {
                deleteCheckById(checkId);
            } else {
                check.setTotal(new BigDecimal(calcTotal(check.getProductAmount())));
                updateCheck(check);
                productService.takeBack(productToDelete, amountToDelete);
            }
        }

    }

}