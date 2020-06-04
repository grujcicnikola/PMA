package com.example.pma.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.example.pma.dto.AddressDTO;

@Entity
public class Address {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
	@Column
	private String city;
	@Column
    private String street;
	@Column
    private String number;		//zbog 148a koji sam pogodila
	@Column
    private Double lon;
	@Column
    private Double lat;
	
	
	public Address() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Address(String city, String street, String number, Double lon, Double lat) {
		this.city = city;
		this.street = street;
		this.number = number;
		this.lon = lon;
		this.lat = lat;
	}
	public Address(AddressDTO address) {
		// TODO Auto-generated constructor stub
		this.city = address.getCity();
		this.street = address.getStreet();
		this.number = address.getNumber();
		this.lon = address.getLon();
		this.lat = address.getLat();
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
