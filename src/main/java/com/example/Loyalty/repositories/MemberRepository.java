package com.example.Loyalty.repositories;

import com.example.Loyalty.models.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAll() ;
    Optional<Member> findById(Long id) ;
    Member findByLevelId(Long id) ;
    Member save(Member member);
    void delete(Member member);
    void deleteById(Long id) ;

    int countMembersByLevelId(Long levelId);
}
