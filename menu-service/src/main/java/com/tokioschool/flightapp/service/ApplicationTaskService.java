package com.tokioschool.flightapp.service;

import com.tokioschool.flightapp.domain.Beer;
import com.tokioschool.flightapp.domain.Main;
import com.tokioschool.flightapp.domain.Menu;
import com.tokioschool.flightapp.projection.BeerStyleCountAggregate;
import com.tokioschool.flightapp.repository.BeerDAO;
import com.tokioschool.flightapp.repository.MenuDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApplicationTaskService implements ApplicationRunner {

  private final MenuService menuService;
  private final MenuDAO menuDAO;
  private final BeerDAO beerDAO;

  @Override
  public void run(ApplicationArguments args) throws Exception {

    log.info("ApplicationTaskService started");

    long countMenus = menuDAO.count();

    log.info("Count menus: {}", countMenus);

    if (countMenus == 0) {
      Collection<Menu> randomMenus = menuService.createRandomMenus();
      Menu menu = menuDAO.findById(randomMenus.toArray(new Menu[0])[10].getId()).get();
      log.info("Menu random: {}", menu);
    }

    // Recuperar los ids de menus
    List<String> menuIds = menuService.getMenuIds();
    log.info("Menu ids: {}", String.join(", ", menuIds));

    // Filtrar los menús vegetarianos y ordenarlos por títulos

    List<Menu> vegetarianMenus = menuDAO.findByVegetarianTrueOrderByTitle();
    log.info(
        "Vegetarian menus: {}, first: {}, last: {}",
        vegetarianMenus.size(),
        vegetarianMenus.get(0).getTitle(),
        vegetarianMenus.get(vegetarianMenus.size() - 1).getTitle());

    // Ordenar por campos nested en los documentos

    Menu menu11 = menuDAO.findById(menuIds.get(11)).get();
    log.info(
        "Menu 11 non-ordered: {}-{}",
        menu11.getTitle(),
        menu11.getMains().stream().map(Main::getName).toList());

    Menu menu11Ordered = menuDAO.findByIdWithMainsOrdered(menuIds.get(11)).get();
    log.info(
        "Menu 11 yes-ordered: {}-{}",
        menu11Ordered.getTitle(),
        menu11Ordered.getMains().stream().map(Main::getName).toList());

    // Modificar documentos, via save, vía insert daría error de duplicate index key

    Menu vegetarianMenu = vegetarianMenus.get(0);
    vegetarianMenu.setVegetarian(false);
    // menuDAO.insert(vegetarianMenu);
    menuDAO.save(vegetarianMenu);

    long countByVegetarianIsTrue = menuDAO.countByVegetarianIsTrue();
    log.info("Vegetarian menus: {}", countByVegetarianIsTrue);

    // Filtrar por texto en un campo nested

    List<Menu> pizzaMenuByMainName = menuDAO.findByMainsName("pizza");
    log.info("Pizza menus case-sensitive: {}", pizzaMenuByMainName.size());

    List<Menu> pizzaMenuByMainName2 = menuDAO.findByMainsName("Pizza");
    log.info("Pizza menus case-sensitive: {}", pizzaMenuByMainName2.size());

    List<Menu> pizzaMenusCI = menuDAO.findByMainsNameCaseInsensitive("pizza");
    log.info("Pizza menus case-sensitive: {}", pizzaMenusCI.size());

    // Filtrar por valores numéricos

    List<Menu> byCaloriesGreaterThan = menuDAO.findByCaloriesGreaterThan(BigDecimal.valueOf(650));
    log.info("Calories gt: {}", byCaloriesGreaterThan.size());

    // Calcular la media de calorias:

    Double averageCalories = menuDAO.findCaloriesAverage();
    log.info("Average calories: {}", averageCalories);

    // Filtrar por un criterio nested y devolver los campos que hacen matching (projection)

    List<Menu> menuWithMainsWithLettuce = menuDAO.findMainsByIngredient("lettuce");
    log.info("Menus with mains lettuce: {}", menuWithMainsWithLettuce.size());

    Menu menuAndMainsWithLettuce = menuWithMainsWithLettuce.get(0);
    Menu menuAndMainsWithLettuceAndOthers = menuDAO.findById(menuAndMainsWithLettuce.getId()).get();
    log.info("Menu mains complete: {}", menuAndMainsWithLettuceAndOthers.getMains());
    log.info("Menu mains lettuce: {}", menuAndMainsWithLettuce.getMains());

    // Beers

    // Filter by dbref

    //    List<Menu> lightLager = menuDAO.findByBeerStyle("Light lager");
    //    log.info("Menu with beer style: {}", lightLager.size());

    List<BeerStyleCountAggregate> beerStyleCountAggregates = beerDAO.countByStyle();

    log.info(
        "Beer style most present: {}/{}",
        beerStyleCountAggregates.get(0).getStyle(),
        beerStyleCountAggregates.get(0).getCount());

    // get beers by style
    List<Beer> beersWithStyle = beerDAO.findByStyle(beerStyleCountAggregates.get(0).getStyle());
    List<Beer> beersNotWithStyle =
        beerDAO.findByStyleIsNot(beerStyleCountAggregates.get(0).getStyle());
    log.info("Beers with style vs not: {}/{}", beersWithStyle.size(), beersNotWithStyle.size());

    // get menus with beer of style

    List<Menu> menusWithStyle =
        menuDAO.findByBeerIn(beersWithStyle.stream().map(Beer::getId).toList());
    log.info("Menus with style: {}", menusWithStyle.size());

    // pagination

    Page<Beer> page;
    int i = 0;
    do {
      PageRequest pageRequest = PageRequest.of(i++, 9, Sort.by(Sort.Direction.ASC, "name"));

      page = beerDAO.findAll(pageRequest);

      log.info(
          "Page {}/{}, elems:{}, first:{}, last:{}",
          page.getNumber(),
          page.getTotalPages(),
          page.getNumberOfElements(),
          page.getContent().get(0).getName(),
          page.getContent().get(page.getContent().size() - 1).getName());
    } while (!page.isLast());
  }
}
