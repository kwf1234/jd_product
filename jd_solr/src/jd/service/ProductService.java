package jd.service;

import jd.utils.ResultModel;

public interface ProductService {
	ResultModel queryProduct(String queryString, String catalog_name, String price, int sort, Integer page) throws Exception;
}
