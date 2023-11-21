package com.hellospring.service;

import com.hellospring.domain.Member;
import com.hellospring.repository.MemberRepository;
import com.hellospring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
@SpringBootTest
@Transactional //테스트 진행 시 롤백처리
class MemberServiceIntegrationTest {
    //integration test(통합 테스트)

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    /*
    * @BeforeEach
    * public void beforeEach() {
    *   memberRepository = new MemoryMemberRepository();
    *   memberService = new MemberService(memberRepository);
    * }
    *
    * 직접 객체를 생성하는 것이 아닌, 컨테이너에게 MemberService, MemberRepository를 받아야 한다.
    * */

   /*
   * @AfterEach
   * public void afterEach() {
   *    memberRepository.clearStore();
   * }
   *
   * MemoryDB에 있던 데이터가 다음 테ㅅ트에 영향을 주는 것을 막기 위해 작성했던 코드
   * 테스트는 모두 독립적이라는 것을 기억하자
   * */

    @Test
    public void 회원가입() {
        //given
        Member member = new Member();
        member.setName("spring");

        //when
        Long saveId = memberService.join(member); //join method는 회원가입한 회원의 id를 반환함

        //then
        Member findMember = memberService.findOne(saveId).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    public void 중복_회원_예외(){
        //given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");

        //when
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }

}