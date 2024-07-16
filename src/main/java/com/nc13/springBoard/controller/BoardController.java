package com.nc13.springBoard.controller;


import com.nc13.springBoard.model.BoardDTO;
import com.nc13.springBoard.model.ReplyDTO;
import com.nc13.springBoard.model.UserDTO;
import com.nc13.springBoard.service.BoardService;
import com.nc13.springBoard.service.ReplyService;
import com.nc13.springBoard.service.UserService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;



@Controller
@RequestMapping("/board/")
public class BoardController {
    @Autowired
    private BoardService boardService;
    @Autowired
    private ReplyService replyService;

    @GetMapping("showAll")
    public String moveToFirstPage() {
        return "redirect:/board/showAll/1";
    }

    @GetMapping("showAll/{pageNo}")

    public String showAll(Model model, @PathVariable int pageNo, HttpSession session, Authentication authentication) {

        // 가장 마지막 페이지의 번호
        int maxPage = boardService.selectMaxPage();
        model.addAttribute("maxPage", maxPage);

        // 우리가 이제 pageNo를 사용하여 시작 페이지 번호, 끝 페이지 번호를 계산해주어야 한다.
        // 이때에는 크게 3가지가 있다.
        // 1. 현재 페이지가 3이하일 경우
        // 시작: 1, 끝: 5
        // 2. 현재 페이지가 최대 페이지 -2 이상일 경우
        // 시작: 최대 페이지 -4 끝 최대페이지
        // 3. 그 외
        // 시작: 현재 페이지 -2 끝: 현재 페이지 +2

        // 시작 페이지
        int startPage;

        // 끝 페이지
        int endPage;

        if (maxPage < 5) {
            startPage = 1;
            endPage = maxPage;
        } else if (pageNo <= 3) {
            startPage = 1;
            endPage = 5;
        } else if (pageNo >= maxPage - 2) {
            startPage = maxPage - 4;
            endPage = maxPage;
        } else {
            startPage = pageNo - 2;
            endPage = pageNo + 2;
        }

        model.addAttribute("curPage", pageNo);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        List<BoardDTO> list = boardService.selectAll(pageNo);
        model.addAttribute("list", list);

        return "board/showAll";
    }

    @GetMapping("write")
    public String showWrite() {
        return "board/write";
    }

    @PostMapping("write")
    public String write(BoardDTO boardDTO, Authentication authentication) {
        //String path = "c:\\uploads\\a\\bb\\dd";        //어디에 파일 업로드 할래?

        /*File pathDir = new File(path);
        if (!pathDir.exists()){
            pathDir.mkdirs();
        }
        //new File(path).mkdirs();        //없으면 이 위치에 폴더를 만들어줘 임

        //File f = new File(path, file.getOriginalFilename()); // 어디에 (path)dp 어떤 파일을 저장할껀지 file.getOriginalFilename는 파일의 업로드 이름임
        // 멀티파트파일은 스프링 프레임워크가 어느정도 보정을 해준다.

        try {
            for(MultipartFile mf: file){
                File f=new File(path,mf.getOriginalFilename());// 이 들어온 파일을 복사해서 업로드해줘 이거임
                mf.transferTo(f);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }*/
        UserDTO logIn= (UserDTO) authentication.getPrincipal();
        boardDTO.setWriterId(logIn.getId());
        boardService.insert(boardDTO);

        return "redirect:/board/showOne/" + boardDTO.getId();
    }

    // 우리가 주소창에 있는 값을 매핑해줄 수 있다.
    @GetMapping("showOne/{id}")
    public String showOne(HttpSession session, @PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        UserDTO logIn = (UserDTO) session.getAttribute("logIn");
        if (logIn == null) {
            return "redirect:/";
        }

        BoardDTO boardDTO = boardService.selectOne(id);

        if (boardDTO == null) {
            redirectAttributes.addFlashAttribute("message", "해당 글 번호는 유효하지 않습니다.");
            return "redirect:/showMessage";
        }

        List<ReplyDTO> replyList = replyService.selectAll(id);

        model.addAttribute("boardDTO", boardDTO);
        model.addAttribute("replyList", replyList);

        return "board/showOne";
    }

    @GetMapping("update/{id}")
    public String showUpdate(@PathVariable int id, HttpSession session, RedirectAttributes redirectAttributes, Model model) {
        UserDTO logIn = (UserDTO) session.getAttribute("logIn");
        if (logIn == null) {
            return "redirect:/";
        }

        BoardDTO boardDTO = boardService.selectOne(id);

        if (boardDTO == null) {
            redirectAttributes.addFlashAttribute("message", "존재하지 않는 글 번호입니다.");
            return "redirect:/showMessage";
        }

        if (boardDTO.getWriterId() != logIn.getId()) {
            redirectAttributes.addFlashAttribute("message", "권한이 없습니다.");
            return "redirect:/showMessage";
        }
        model.addAttribute("boardDTO", boardDTO);
        return "board/update";
    }

    @PostMapping("update/{id}")
    public String update(@PathVariable int id, HttpSession session, RedirectAttributes redirectAttributes, BoardDTO attempt) {
        UserDTO logIn = (UserDTO) session.getAttribute("logIn");
        if (logIn == null) {
            return "redirect:/";
        }

        BoardDTO boardDTO = boardService.selectOne(id);
        if (boardDTO == null) {
            redirectAttributes.addFlashAttribute("message", "유효하지 않는 글 번호입니다.");
            return "redirect:/showMessage";
        }

        if (logIn.getId() != boardDTO.getWriterId()) {
            redirectAttributes.addFlashAttribute("message", "권한이 없습니다.");
            return "redirect:/showMessage";
        }

        attempt.setId(id);

        boardService.update(attempt);

        return "redirect:/board/showOne/" + id;
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable int id, HttpSession session, RedirectAttributes redirectAttributes) {
        UserDTO logIn = (UserDTO) session.getAttribute("logIn");
        if (logIn == null) {
            return "redirect:/";
        }

        BoardDTO boardDTO = boardService.selectOne(id);
        if (boardDTO == null) {
            redirectAttributes.addFlashAttribute("message", "존재하지 않는 글번호");
            return "redirect:/showMessage";
        }

        if (boardDTO.getWriterId() != logIn.getId()) {
            redirectAttributes.addFlashAttribute("message", "권한 없음");
            return "redirect:/showMessage";
        }

        boardService.delete(id);

        return "redirect:/board/showAll";
    }

    // 일반 컨트롤러 안에
    // Restful API로써, JSON의 결과값을 리턴해야하는 경우
    // 맵핑 어노테이션 위에 ResponseBody 어노테이션을 붙여준다. 어떤 주소로 이동해라가 아니라 바로바로 리턴해줌
    // 따라서 우리가 하고 싶은건 글 작성할때 그림을 업로드 하고싶을때 바로 업로드가 되도록 하는 애
    @ResponseBody
    @PostMapping("uploads")
    public Map<String, Object> uploads(MultipartHttpServletRequest request){
        Map<String, Object> resultMap=new HashMap<>();

        String uploadPath="";

        MultipartFile file = request.getFile("upload");
        String fileName=file.getOriginalFilename();
        String extension=fileName.substring(fileName.lastIndexOf("."));// 파일 이름 보면 다운로드/jpg , 내음악.mp3 이런거 찾아내는 애
        String uploadName = UUID.randomUUID()+extension;

        String realPath=request.getServletContext().getRealPath("/board/uploads/");//돌아가는 톰캣의 실제 주소 찾는 메서드
        Path realDir= Paths.get(realPath);
        if(!Files.exists(realDir)){
            try{
                Files.createDirectories(realDir);
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        File uploadFile = new File(realPath+uploadName);
        try {
            file.transferTo(uploadFile);
        } catch (IOException e){
            System.out.println("파일 전송 중 에러");
            e.printStackTrace();
        }

        // 업로드 경로의 내용
        uploadPath = "/board/uploads/"+uploadName;

        resultMap.put("uploaded",true);
        resultMap.put("url", uploadPath);
        return resultMap;
    }
    /*@GetMapping("test")
    public String test(HttpSession session) {
        UserDTO logIn = (UserDTO) session.getAttribute("logIn");
        if (logIn==null) {
            return "redirect:/";
        }

        for(int i=1;i<=300; i++) {
            BoardDTO boardDTO =new BoardDTO();
            boardDTO.setTitle("테스트 제목"+i);
            boardDTO.setContent("테스트 "+i+"번 글의 내용입니다.");
            boardDTO.setWriterId(logIn.getId());
            boardService.insert(boardDTO);
        }

        return "redirect:/board/showAll";
    }*/
}
