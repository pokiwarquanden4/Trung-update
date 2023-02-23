package com.example.repositoty;

import com.example.entity.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IConfigCacheRepository extends JpaRepository<Config,String> {
    @Query(value = "SELECT * FROM config WHERE `key` = :valueFromDatabase ", nativeQuery = true)
    Config findConfig(String valueFromDatabase);
}
