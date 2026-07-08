package com.auction.controller;

import com.auction.entity.Product;
import com.auction.entity.ProductType;
import com.auction.service.IProductService;
import com.auction.service.IProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private IProductService productService;

    @Autowired
    private IProductTypeService productTypeService;

    @GetMapping({"", "/list"})
    public String showList(
            @RequestParam(value = "searchName", required = false, defaultValue = "") String searchName,
            @RequestParam(value = "searchPrice", required = false) Double searchPrice,
            @RequestParam(value = "searchTypeId", required = false) Integer searchTypeId,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            Model model) {
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, 5);
        Page<Product> productPage = productService.searchAndPaginate(searchName, searchPrice, searchTypeId, pageable);

        model.addAttribute("productPage", productPage);
        model.addAttribute("productTypes", productTypeService.findAll());
        model.addAttribute("searchName", searchName);
        model.addAttribute("searchPrice", searchPrice);
        model.addAttribute("searchTypeId", searchTypeId);
        model.addAttribute("currentPage", page);
        return "product/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        Product product = new Product();
        product.setProductType(new ProductType());
        model.addAttribute("product", product);
        model.addAttribute("productTypes", productTypeService.findAll());
        return "product/create";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") Product product, Model model, RedirectAttributes redirectAttributes) {
        try {
            productService.save(product);
            redirectAttributes.addFlashAttribute("message", "Thêm mới sản phẩm đấu giá thành công!");
            return "redirect:/product/list";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMsg", e.getMessage());
            model.addAttribute("productTypes", productTypeService.findAll());
            return "product/create";
        }
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam(value = "id", required = false) Integer id, Model model) {

        if (id == null) {
            Product emptyProduct = new Product();
            emptyProduct.setProductType(new com.auction.entity.ProductType());
            model.addAttribute("product", emptyProduct);
            model.addAttribute("productTypes", productTypeService.findAll());
            model.addAttribute("errorMsg", "Vui lòng cung cấp ID sản phẩm hợp lệ để chỉnh sửa!");
            return "product/edit";
        }

        Product product = productService.findById(id);

        if (product == null) {
            Product emptyProduct = new Product();
            emptyProduct.setProductType(new com.auction.entity.ProductType());

            model.addAttribute("product", emptyProduct);
            model.addAttribute("productTypes", productTypeService.findAll());

            model.addAttribute("errorMsg", "Sản phẩm có ID " + id + " không tồn tại trong hệ thống!");
            return "product/edit";
        }

        model.addAttribute("product", product);
        model.addAttribute("productTypes", productTypeService.findAll());
        return "product/edit";
    }

    @PostMapping("/update")
    public String updateProduct(@ModelAttribute("product") Product product, Model model, RedirectAttributes redirectAttributes) {
        try {
            productService.update(product);
            redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin sản phẩm thành công!");
            return "redirect:/product/list";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMsg", e.getMessage());
            model.addAttribute("productTypes", productTypeService.findAll());
            return "product/edit";
        }
    }

    @PostMapping("/delete")
    public String deleteProducts(@RequestParam(value = "selectedIds", required = false) List<Integer> selectedIds,
                                 RedirectAttributes redirectAttributes) {
        try {
            productService.deleteByIds(selectedIds);
            redirectAttributes.addFlashAttribute("message", "Xóa thành công các sản phẩm đã chọn!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/product/list";
    }

    @ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
    public String handleTypeMismatch(Exception ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Định dạng dữ liệu nhập vào không hợp lệ!");
        return "redirect:/product/list";
    }
}