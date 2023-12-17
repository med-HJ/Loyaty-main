package com.example.Loyalty.repositories;

import com.example.Loyalty.models.Campaign;
import com.example.Loyalty.models.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    List<Voucher> findAll() ;
    Optional<Voucher> findById(Long id) ;
    Voucher save(Voucher voucher);
    void delete(Voucher voucher);
    void deleteById(Long id) ;
}
