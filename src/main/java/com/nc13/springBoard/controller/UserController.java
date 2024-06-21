package com.nc13.springBoard.controller;

import com.nc13.springBoard.model.UserDTO;
import com.nc13.springBoard.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller     // http 요청을 받고 적절한 응답을 반환하는 역할, RequestMapping 뷰반환, 모델 데이터(model.addAtribute등 사용할 수 있게)
@RequestMapping("/user/")   //클래스 내 모든 메서드에 기본 URL설정
@RequiredArgsConstructor        // 롬복 데이터 같은 느낌으로  생성자를 자동으로 생성시켜 어디서든 사용할 수 있게 한다.
public class UserController {
    // 실제 SQL 통신을 담당할 Service 객체
    @Autowired          //생성자 자동주입할수 있게 하는애
    private UserService userService;
    // 사용자가 로그인을 할 시 실행할
    // auth 메소드                     -> 로그아웃시 정지와 같은 역할을 한다??
    @PostMapping("auth")            // 인덱스에서 포스트로 값을 받아왔기 때문에 포스트 매핑을 이용
    // POST 혹은 GET 방식으로 웹페이지의 값을 받아올 때에는
    // 파라미터에  해당 form의 name 어트리 뷰트와 같은 이름을 가진
    // 파라미터를 적어주면 된다.
    // 또한, 해당 name 어트리뷰트를 필드로 가진 클래스 객체를 파라미터로 잡아주면
    // 자동으로 데이터가 바인딩 된다.
    public String auth(UserDTO userDTO, HttpSession session) {
        UserDTO result=userService.auth(userDTO);
        if (result != null) {
            session.setAttribute("logIn",result);
            return "redirect:/board/showAll";
        }
                // 값이 무작정 들어가는 것이 아니라 form의 이름이 같은 애들이 존재하면 그값들이 파라미터 안에 들어가게 된다.

        // 만약 우리가 해당 메소드를 실행시키고 나서 특정 URL로 이동시킬 떄에는 다음과 같이 적어준다.
        return "redirect:/"; //URL을 강제로 보낼때 사용하는 친구이다.
    }

    @GetMapping("register")
    public String showRegister(){
        return "user/register";
    }

    @PostMapping("register")
    public String register(UserDTO userDTO, RedirectAttributes redirectAttributes){
        if (userService.validateUsername(userDTO.getUsername())){
            userService.register(userDTO);
            System.out.println("회원가입 성공!!!");
        } else{
            // 회원 가입 실패 메시지 전송
            // 회원 가입 실패시, 우리가 URL을 /error 라는 곳으로 전송을 해주되, 해당 페이지에서 무슨에러인지 알 수 있도록
            // 메시지 내용을 여기서 담아서 보낸다. 만약 다른 URL로 이동을 할 때 어떠한 값을 보내주어야 하는 경우
            // RedirectAttributes 라는 것을 사용한다.
            redirectAttributes.addFlashAttribute("message","중복된 아이디로는 가입하실 수 없습니다.");

            return "redirect:/showMessage";
        }
        return "redirect:/";
    }
}
