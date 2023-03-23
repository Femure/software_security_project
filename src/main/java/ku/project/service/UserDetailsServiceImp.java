package ku.project.service;

import ku.project.model.Member;
import ku.project.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private Member member;

    public void CustomUserDetails(Member member) {
        this.member = member;
    }

    public boolean isEnabled() {
        return member.isEnabled();
    }

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Member member = memberRepository.findByUsername(username);

        if (member == null) {
            throw new UsernameNotFoundException("Could not find user");
        }

        return new org.springframework.security.core.userdetails.User(
                member.getUsername(), member.getPassword(),
                new ArrayList<>());
    }

    public boolean isAccountNonLocked() {
        return member.isAccountNonLocked();
    }

}
