package com.ntp.sasin_be.mapper;

import com.ntp.sasin_be.dto.ProductDTO;
import com.ntp.sasin_be.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "supplier.id", target = "supplierId")
    @Mapping(source = "franchise.id", target = "franchiseId")
    ProductDTO toDto(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "franchise", ignore = true)
    Product toEntity(ProductDTO productDTO);
}
