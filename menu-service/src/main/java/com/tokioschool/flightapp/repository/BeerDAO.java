package com.tokioschool.flightapp.repository;

import com.tokioschool.flightapp.domain.Beer;
import com.tokioschool.flightapp.projection.BeerStyleCountAggregate;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface BeerDAO extends MongoRepository<Beer, UUID> {

  @Aggregation(
      pipeline = {
        "{$group:  {'_id':  '$style', 'count':  {'$sum':1 }}}",
        "{$project :  {'_id': null, 'style': '$_id', 'count' : '$count' }}",
        "{$sort:  {'count':  -1}}"
      })
  List<BeerStyleCountAggregate> countByStyle();

    List<Beer> findByStyle(String style);

    List<Beer> findByStyleIsNot(String style);
}
