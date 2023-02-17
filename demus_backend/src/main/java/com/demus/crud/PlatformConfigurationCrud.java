/**
 * 
 */
package com.demus.crud;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demus.entity.PlatformConfiguration;

/**
 * Crud interface for country
 * @author Lekan Baruwa and Folarin Omotoriogun
 * @for EcleverMobile, http://eclevermobile.com
 */
@Transactional
public interface PlatformConfigurationCrud extends JpaRepository<PlatformConfiguration, Long> {
	
}
