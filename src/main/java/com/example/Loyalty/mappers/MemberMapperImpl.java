package com.example.Loyalty.mappers;

import com.example.Loyalty.dtos.MemberDTO;
import com.example.Loyalty.models.Member;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class MemberMapperImpl {
//    public MemberDTO fromMember(Member member){
//        MemberDTO memberDTO=new MemberDTO();
//        BeanUtils.copyProperties(member,memberDTO);
//        return  memberDTO;
//    }
public MemberDTO fromMember(Member member){
    MemberDTO memberDTO = new MemberDTO();
    BeanUtils.copyProperties(member, memberDTO);
    if (member.getLevel() != null) {
        memberDTO.setLevelId(member.getLevel().getId());
    }
    return memberDTO;
}
    public Member fromMemberDTO(MemberDTO memberDTO){
        Member member=new Member();
        BeanUtils.copyProperties(memberDTO,member);
        return  member;
    }
}
