package ku.review.service;

import ku.review.model.Member;
import ku.review.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(member.getRole()));

        return new org.springframework.security.core.userdetails.User(
                member.getUsername(), member.getPassword(), authorities);

    }

    public boolean isAccountNonLocked() {
        return member.isAccountNonLocked();
    }

}
