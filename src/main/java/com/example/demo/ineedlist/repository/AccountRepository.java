package com.example.demo.ineedlist.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.ineedlist.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>{
	
	Optional<Account>findByLoginId(String loginId);

}
