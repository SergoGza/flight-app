package com.tokioschool.flightapp.service;

import com.github.javafaker.Faker;
import com.tokioschool.flightapp.domain.Beer;
import com.tokioschool.flightapp.domain.Ingredient;
import com.tokioschool.flightapp.domain.Main;
import com.tokioschool.flightapp.domain.Menu;
import com.tokioschool.flightapp.repository.BeerDAO;
import com.tokioschool.flightapp.repository.MenuDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class MenuService {

  private final MenuDAO menuDAO;
  private final MongoTemplate mongoTemplate;
  private final SecureRandom secureRandom = new SecureRandom();
  private final Faker faker = new Faker();
  private final BeerDAO beerDAO;

  public List<Beer> createRandomBeers() {

    List<Beer> beer =
        IntStream.range(0, 50)
            .mapToObj(
                i -> Beer.builder().name(faker.beer().name()).style(faker.beer().style()).build())
            .toList();

    return beerDAO.saveAll(beer);
  }

  public Collection<Menu> createRandomMenus() {

    List<Beer> randomBeers = createRandomBeers();

    List<Menu> menusToCreate =
        IntStream.range(0, 100)
            .mapToObj(
                i -> {
                  List<Main> mains =
                      IntStream.range(0, i)
                          .mapToObj(
                              j ->
                                  Main.builder()
                                      .name(faker.food().dish())
                                      .ingredients(
                                          List.of(
                                              Ingredient.builder()
                                                  .name(faker.food().ingredient())
                                                  .build(),
                                              Ingredient.builder()
                                                  .name(faker.food().vegetable())
                                                  .build()))
                                      .build())
                          .toList();

                  return Menu.builder()
                      .title("menu-%02d".formatted(i))
                      .created(Instant.now())
                      .calories(BigDecimal.valueOf(secureRandom.nextDouble(300, 1000)))
                      .vegetarian(secureRandom.nextBoolean())
                      .mains(mains)
                      .beer(randomBeers.get(secureRandom.nextInt(randomBeers.size())))
                      .build();
                })
            .toList();

    return mongoTemplate.insertAll(menusToCreate);
  }

  public List<String> getMenuIds() {
    Query query = new Query();
    query.fields().include("_id");

    return mongoTemplate.find(query, Menu.class).stream().map(Menu::getId).toList();
  }
}
