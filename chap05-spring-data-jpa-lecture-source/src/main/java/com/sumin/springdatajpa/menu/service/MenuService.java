package com.sumin.springdatajpa.menu.service;

import com.sumin.springdatajpa.menu.dto.CategoryDTO;
import com.sumin.springdatajpa.menu.dto.MenuDTO;
import com.sumin.springdatajpa.menu.entity.Category;
import com.sumin.springdatajpa.menu.entity.Menu;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MenuService {

    private final MenuRepository menuRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;

    @Autowired
    public MenuService(MenuRepository menuRepository, ModelMapper modelMapper, CategoryRepository categoryRepository) {
        this.menuRepository = menuRepository;
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
    }

    /* 설명. 1. findById() */
    public MenuDTO findMenuByCode(int menuCode) {

//        Menu menu = menuRepository.findById(menuCode).get();
        Menu menu = menuRepository.findById(menuCode).orElseThrow(IllegalArgumentException::new);
        log.debug("menu: {}", menu);

        return modelMapper.map(menu, MenuDTO.class);
    }

    /* 설명. 2. findAll() 페이징 처리 전 */
    public List<MenuDTO> findMenuList() {
        List<Menu> menus = menuRepository.findAll(Sort.by("menuCode").descending());

        return menus.stream()
                .map(menu -> modelMapper.map(menu, MenuDTO.class))
                .collect(Collectors.toList());
    }

    /* 설명. 3. findAll() 페이징 처리 후 */
    public Page<MenuDTO> findMenuList(@PageableDefault Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1,
                pageable.getPageSize(), Sort.by("menuCode").descending());
        Page<Menu> menuList = menuRepository.findAll(pageable);

        return menuList.map(menu -> modelMapper.map(menu, MenuDTO.class));
    }

    /* 설명. 4. QueryMethod 활용 */
    public List<MenuDTO> findMenuPrice(int menuPrice) {

        List<Menu> menus = menuRepository.findByMenuPriceGreaterThan(menuPrice);

        return menus.stream().map(menu -> modelMapper.map(menu, MenuDTO.class))
                .collect(Collectors.toList());
    }

    /* 설명. 5. jpql 및 native sql 활용 */
    public List<CategoryDTO> findAllCategory() {

        List<Category> categories = categoryRepository.findAllCategories();

        return categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());

    }

    /* 설명. 6. 추가하기 */
    @Transactional
    public void registMenu(MenuDTO newMenu) {
        menuRepository.save(modelMapper.map(newMenu, Menu.class));
    }
}
