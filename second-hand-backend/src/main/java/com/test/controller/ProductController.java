package com.test.controller;

import com.test.dto.Result;
import com.test.entity.Product;
import com.test.entity.User;
import com.test.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product")
@CrossOrigin(origins = {"http://localhost:8080", "http://127.0.0.1:8080"}, allowCredentials = "true")
public class ProductController {

    @Autowired
    private ProductService productService;

    // 图片上传保存路径（绝对路径，指向项目的 uploads/products 文件夹）
    private static final String UPLOAD_DIR = "C:/Users/lenovo/IdeaProjects/spring-boot-test/uploads/products/";

    // 前台首页：只显示出售中
    @GetMapping("/list")
    public Result<List<Product>> list(@RequestParam(required = false) Integer categoryId,
                                      @RequestParam(required = false) String keyword) {
        List<Product> list = productService.findList(categoryId, keyword);
        return Result.success(list);
    }

    // 后台管理：显示所有商品
    @GetMapping("/admin/list")
    public Result<List<Product>> adminList(@RequestParam(required = false) Integer categoryId,
                                           @RequestParam(required = false) String keyword) {
        List<Product> list = productService.findAllList(categoryId, keyword);
        return Result.success(list);
    }

    @GetMapping("/detail/{id}")
    public Result<Product> detail(@PathVariable Integer id) {
        productService.viewProduct(id);
        Product product = productService.findById(id);
        if (product == null) {
            return Result.error("商品不存在");
        }
        return Result.success(product);
    }

    @GetMapping("/my")
    public Result<List<Product>> my(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error(401, "未登录");
        }
        return Result.success(productService.findBySellerId(user.getId()));
    }

    @PostMapping("/add")
    public Result<String> add(@RequestParam String title,
                              @RequestParam String description,
                              @RequestParam Double price,
                              @RequestParam(required = false) Double originalPrice,
                              @RequestParam Integer categoryId,
                              @RequestParam(required = false) String images,
                              HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error(401, "未登录");
        }

        Product product = new Product();
        product.setTitle(title);
        product.setDescription(description);
        product.setPrice(BigDecimal.valueOf(price));
        if (originalPrice != null) {
            product.setOriginalPrice(BigDecimal.valueOf(originalPrice));
        }
        product.setCategoryId(categoryId);
        product.setSellerId(user.getId());
        product.setImages(images);

        boolean success = productService.addProduct(product);
        if (!success) {
            return Result.error("发布失败");
        }
        return Result.success("发布成功");
    }

    @PostMapping("/update")
    public Result<String> update(@RequestParam Integer id,
                                 @RequestParam String title,
                                 @RequestParam String description,
                                 @RequestParam Double price,
                                 @RequestParam(required = false) Double originalPrice,
                                 @RequestParam Integer categoryId,
                                 @RequestParam(required = false) String images,
                                 HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error(401, "未登录");
        }

        Product product = productService.findById(id);
        if (product == null || !product.getSellerId().equals(user.getId())) {
            return Result.error("无权操作该商品");
        }

        product.setTitle(title);
        product.setDescription(description);
        product.setPrice(BigDecimal.valueOf(price));
        if (originalPrice != null) {
            product.setOriginalPrice(BigDecimal.valueOf(originalPrice));
        }
        product.setCategoryId(categoryId);
        product.setImages(images);

        boolean success = productService.updateProduct(product);
        if (!success) {
            return Result.error("修改失败");
        }
        return Result.success("修改成功");
    }

    @PostMapping("/status")
    public Result<String> updateStatus(@RequestParam Integer id,
                                       @RequestParam Integer status,
                                       HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error(401, "未登录");
        }

        Product product = productService.findById(id);
        if (product == null) {
            return Result.error("商品不存在");
        }

        if (!"admin".equals(user.getRole())) {
            if (!product.getSellerId().equals(user.getId())) {
                return Result.error("无权操作");
            }
            if (status != 2) {
                return Result.error("只能下架自己的商品");
            }
        }

        boolean success = productService.updateStatus(id, status);
        if (!success) {
            return Result.error("操作失败");
        }
        return Result.success("操作成功");
    }

    @PostMapping("/upload")
    public Result<Map<String, String>> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("文件不能为空");
        }

        try {
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) {
                boolean created = dir.mkdirs();
                System.out.println("创建上传目录: " + UPLOAD_DIR + " 是否成功: " + created);
            }

            String originalName = file.getOriginalFilename();
            String ext = "";
            if (originalName != null && originalName.contains(".")) {
                ext = originalName.substring(originalName.lastIndexOf("."));
            }
            String newName = System.currentTimeMillis() + "_" + (int)(Math.random() * 10000) + ext;
            String filePath = UPLOAD_DIR + newName;

            file.transferTo(new File(filePath));
            System.out.println("文件保存成功: " + filePath);

            Map<String, String> data = new HashMap<>();
            data.put("url", "/uploads/products/" + newName);
            return Result.success(data);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("上传失败：" + e.getMessage());
        }
    }
}
