package com.smhrd.DTO;

public class ProductBasicInfoDto {
	  private Integer prodId;
	  
	  public ProductBasicInfoDto(Integer prodId) {
	        this.prodId = prodId;
	      }
	  
	  // Getter / Setter

	    public Integer getProdId() {
	        return prodId;
	    }

	    public void setProdId(Integer prodId) {
	        this.prodId = prodId;
	    }
}
