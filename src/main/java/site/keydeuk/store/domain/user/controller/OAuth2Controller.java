package site.keydeuk.store.domain.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {
    @GetMapping("/signUp")
    public String loadOAuthSignUp(@RequestParam String email, @RequestParam String provider, @RequestParam String providerId, @RequestParam String nickname, @RequestParam String imgUrl, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("email", email);
        redirectAttributes.addAttribute("provider", provider);
        redirectAttributes.addAttribute("providerId", providerId);
        redirectAttributes.addAttribute("nickname", nickname);
        redirectAttributes.addAttribute("imgUrl", imgUrl);
        return "redirect:"; // 프론트 회원가입 주소로 리다이렉트
    }

}
