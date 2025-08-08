package com.tokioschool.flightapp.repository;

import com.tokioschool.flightapp.domain.AirportRaw;
import com.tokioschool.flightapp.domain.AirportRawId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirportRawRepository extends JpaRepository<AirportRaw, AirportRawId> {}
