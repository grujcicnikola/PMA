package com.example.pma.dto;

import com.example.pma.domain.Address;

public class AddressDTO {
	
	private Long id;
	private String city;
    private String street;
    private String number;
    private Double lon;
    private Double lat;
    
    
	public AddressDTO(Address address) {
		this.id = address.getId();
		this.city = address.getCity();
		this.street = address.getStreet();
		this.number = address.getNumber();
		this.lon = address.getLon();
		this.lat = address.getLat();
	}
	public AddressDTO(Long id, String city, String street, String number, Double lon, Double lat) {
		super();
		this.id = id;
		this.city = city;
		this.street = street;
		this.number = number;
		this.lon = lon;
		this.lat = lat;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public Double getLon() {
		return lon;
	}
	public void setLon(Double lon) {
		this.lon = lon;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}

    
}
