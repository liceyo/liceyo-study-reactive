package liceyo.study.reactive;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * IndexController
 * @description 控制跳转
 * @author lichengyong
 * @date 2019/4/23 15:21
 * @version 1.0
 */
@Controller
@RequestMapping("/index")
public class IndexController {

    @GetMapping
    public String index(){
        return "index";
    }
}
