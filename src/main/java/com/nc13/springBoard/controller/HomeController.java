package com.nc13.springBoard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// 우리가 해당 클래스를 스프링 프레임워크가 직접 생성/초기화를 할 수 있도록 어노테이션을 붙여준다.
// 해당 컨트롤러의 경우, 우리가 사용자가 특정 주소를 접속할 때에 어느 페이지를 보여줄 지를 정해주므로 @Controller 라고 적어준다.
@Controller
public class HomeController {
    // 스프링은 URL 기반으로 특정페이지의 실행 여부를 결정하게 된다.
    // 따라서 우리가 사용자가 어떠한 URL을 접속했을 때 어떤 메소드를 실행시킬지를 연결(=mapping)
    // 시켜주어야 한다. 매핑을 시키는 방법은
    // 1. @RequestMapping(주소, 연결방식) 예전방식
    // 2. @GetMapping(주소)   -> 보통은 얘를 씀
    // 3. @PostMapping(주소) -> form에 method post
    // 3가지가 있다. 예전에는 전부 리퀘스트매핑을 사용했었지만 이제는
    // 겟매핑 혹은 포스트매핑 중 하나를 사용하게 된다.

    // 맨 처음 인덱스 페이지를 보여주기 위한 페이지
    @GetMapping("/")
    public String showIndex() {

        // 특정 페이지를 실행시키는 메소드는 해당 페이지의 폴더명+파일 이름 을 리턴하게 된다.
        return "index";
    }
}
