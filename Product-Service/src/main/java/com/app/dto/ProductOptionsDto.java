package com.app.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProductOptionsDto {

	private List<String> sizes;
	private List<String> colors;
}
