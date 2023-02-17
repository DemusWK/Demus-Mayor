package com.demus.crud;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demus.entity.OneCardUsage;

public interface OneCardUsageCrud extends JpaRepository<OneCardUsage, Long> {

	OneCardUsage findByPin (String pin);
	
}
