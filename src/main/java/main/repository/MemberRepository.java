package main.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import main.model.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

	public Member findMemberByUserIdAndGroupId(Long userId, Long groupId);

}