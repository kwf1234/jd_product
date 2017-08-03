package jd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jd.service.ProductService;
import jd.utils.ResultModel;
@Controller
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@RequestMapping("/list")
	public String showProduct(Model model,String queryString, 
			String catalog_name, String price,@RequestParam(defaultValue="0") int sort
			,@RequestParam(defaultValue="1") Integer page) throws Exception{
		//调用service
		ResultModel resultModel = productService.queryProduct(queryString, catalog_name, price, sort, page);
		//封装model
		model.addAttribute("curPage", page);
		model.addAttribute("price", price);
		model.addAttribute("catalog_name", catalog_name);
		model.addAttribute("queryString",queryString);
		model.addAttribute("sort", sort);
		model.addAttribute("pageCount", resultModel.getPageCount());
		model.addAttribute("recordCount", resultModel.getRecordCount());
		model.addAttribute("list", resultModel.getProductList());
		
		return "product_list";
	}
}
