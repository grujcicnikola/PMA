package com.example.pma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pma.domain.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

}
