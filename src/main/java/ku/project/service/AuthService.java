package ku.project.service;

import ku.project.dto.SignupDto;
import ku.project.model.Member;
import ku.project.repository.MemberRepository;
import net.bytebuddy.utility.RandomString;

import javax.validation.constraints.Null;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private MemberRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    public SignupDto getMember(String username) {
        Member member = repository.findByUsername(username);
        SignupDto newMember = null;
        if(member != null){
            newMember = modelMapper.map(member, SignupDto.class);
        }
        return newMember;
    }

}
