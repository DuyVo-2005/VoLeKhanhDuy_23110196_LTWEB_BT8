package vn.khanhduy.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import jakarta.validation.Valid;
import vn.khanhduy.entities.CategoryEntity;
import vn.khanhduy.model.CategoryModel;
import vn.khanhduy.services.ICategoryService;

@Controller
@RequestMapping("admin/category")
public class CategoriesController {

	@Autowired
	ICategoryService categoryService;

	@GetMapping("add")
	public String add(ModelMap model) {
		CategoryModel categoryModel = new CategoryModel();
		categoryModel.setEdit(false);

		// chuyen data tu model vao bien category de data len view
		model.addAttribute("category", categoryModel);
		return "admin/category/addOrEdit";
	}

	@PostMapping("saveOrUpdate")
	public ModelAndView saveOrUpdate(ModelMap model, @Valid @ModelAttribute("category") CategoryModel categoryModel,
			BindingResult result) {
		if (result.hasErrors()) {
			return new ModelAndView("admin/category/addOrEdit");
		}
		CategoryEntity entity = new CategoryEntity();
		// copy tu model sang entity
		BeanUtils.copyProperties(categoryModel, entity);
		// goi ham save trong service
		categoryService.save(entity);
		// dua thong bao ve cho bien message
		String message = "";
		if (categoryModel.isEdit()) {
			message = "Category is Edited!!!!!";
		} else {
			message = "Category is Saved!!!!!!";
		}
		model.addAttribute("message", message);
		// redirect về url controller
		return new ModelAndView("redirect:/admin/category/searchpaginated?page=1&size=3", model);
	}

	@RequestMapping("")
	public String list(ModelMap model) {

		// gọi hàm file all trong service
		List<CategoryEntity> list = categoryService.findAll();

		// chuyển dữ liệu từ list lên biến categories
		model.addAttribute("categories", list);
		return "admin/category/list";

		/*
		 * //gọi hàm file all trong service List<CategoryEntity> list =
		 * categoryService.findAll(); if(list == null) { list = new ArrayList<>(); //
		 * tránh null } //chuyển dữ liệu từ list lên biến categories
		 * model.addAttribute("categories", list); return "admin/category/list";
		 */
	}

	@GetMapping("edit/{categoryId}")
	public ModelAndView edit(ModelMap model, @PathVariable("categoryId") Long categoryId) {
		Optional<CategoryEntity> optCategory = categoryService.findById(categoryId);
		CategoryModel categoryModel = new CategoryModel();
		// kiểm tra sự tồn tại của category
		if (optCategory.isPresent()) {
			CategoryEntity entity = optCategory.get();
			// copy từ entity sang model
			BeanUtils.copyProperties(entity, categoryModel);
			categoryModel.setEdit(true);
			// đẩy dữ liệu ra view
			model.addAttribute("category", categoryModel);
			return new ModelAndView("admin/category/addOrEdit", model);
		}

		model.addAttribute("message", "Category is not existed!!!");
		return new ModelAndView("forward:/admin/category", model);
	}

	@GetMapping("delete/{categoryId}")
	public ModelAndView delete(ModelMap model, @PathVariable("categoryId") Long categoryId) {
		categoryService.deleteById(categoryId);
		model.addAttribute("message", "Category is Deleted");
		return new ModelAndView("forward:/admin/category/searchpaginated", model);
	}

	@GetMapping("search")
	public String search(ModelMap model, @RequestParam(name = "name", required = false) String name) {
		List<CategoryEntity> list = null;
		// có nội dung truyền về hoặc không (name là tùy chọn)
		if (StringUtils.hasText(name)) {
			list = categoryService.findByNameContaining(name);
		} else {
			list = categoryService.findAll();
		}
		model.addAttribute("categories", list);
		return "admin/category/search";
	}

	@RequestMapping("searchpaginated")
	public String search(ModelMap model, @RequestParam(name = "name", required = false) String name,
			@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
		int count = (int) categoryService.count();
		int currentPage = page.orElse(1);
		int pageSize = size.orElse(3);
		Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("name"));
		Page<CategoryEntity> resultPage = null;
		if (StringUtils.hasText(name)) {
			resultPage = categoryService.findByNameContaining(name, pageable);
			model.addAttribute("name", name);
		} else {
			resultPage = categoryService.findAll(pageable);
		}

		int totalPages = resultPage.getTotalPages();
		if (totalPages > 0) {
			int start = Math.max(1, currentPage - 2);
			int end = Math.min(currentPage + 2, totalPages);
			/*
			 * if (totalPages > 5) { if (end == totalPages) { start = end - 4; } else if
			 * (start == 1) { end = start + 4; } }
			 */
			if (totalPages > count) {
				if (end == totalPages) {
					start = end - count;
				} else if (start == 1) {
					end = start + count;
				}
			}
			/*
			 * if (totalPages > count) {
			 * 
			 * if (end == totalPages) start = end - count; else if (start == 1) end = start
			 * + count;
			 * 
			 * 
			 * // hiển thị tối đa 5 số trang if (totalPages > 5) { if (end == totalPages)
			 * start = end - 4; else if (start == 1) end = start + 4; } }
			 * 
			 */
			List<Integer> pageNumbers = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);

		}
		model.addAttribute("categoryPage", resultPage);
		return "admin/category/searchpaginated";
	}

	@GetMapping("view/{categoryId}")
	public ModelAndView view(ModelMap model, @PathVariable("categoryId") Long categoryId) {
		Optional<CategoryEntity> optCategory = categoryService.findById(categoryId);

		if (optCategory.isPresent()) {
			model.addAttribute("category", optCategory.get());
			return new ModelAndView("admin/category/view", model);
		}

		model.addAttribute("message", "Category not found!");
		return new ModelAndView("redirect:/admin/category/searchpaginated?page=1&size=3");
	}
}
