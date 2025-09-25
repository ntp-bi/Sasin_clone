package com.ntp.sasin_be.mapper;

import com.ntp.sasin_be.dto.CategoryDTO;
import com.ntp.sasin_be.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(source = "parent", target = "parentId", qualifiedByName = "parentIdOrNull")
    CategoryDTO toDTO(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    Category toEntity(CategoryDTO dto);

    @Named("parentIdOrNull")
    static Long parentIdOrNull(Category parent) {
        return parent == null ? null : parent.getId();
    }
}
