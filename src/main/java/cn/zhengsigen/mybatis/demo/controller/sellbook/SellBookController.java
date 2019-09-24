package cn.zhengsigen.mybatis.demo.controller.sellbook;

import cn.zhengsigen.mybatis.demo.mapper.BookMapper;
import cn.zhengsigen.mybatis.demo.mapper.SellBookMapper;
import cn.zhengsigen.mybatis.demo.model.Book;
import cn.zhengsigen.mybatis.demo.model.ResData;
import cn.zhengsigen.mybatis.demo.model.SellBook;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/dzy/sellbook")
public class SellBookController {

    @Autowired
    SellBookMapper sellBookMapper;
    @Autowired
    BookMapper bookMapper;
    @Autowired
    private RestTemplate restTemplate;

    //查询所有卖书列表
    @GetMapping
    public ResData getSellBookList(){
        ResData resData = new ResData();
        resData.setCode(0);
        resData.setData(sellBookMapper.getSellBookList());
        System.out.println("查询所有卖书列表："+resData);
        return resData;
    }
    //查询用户卖书列表
    @GetMapping("/{userId}")
    public ResData getSellBookListByUserId(@PathVariable Integer userId) {
        System.out.println("用户卖书列表 userId："+userId);
        List<Book> bookIdList = sellBookMapper.getSellBookListByUserId(userId);
        ResData resData = new ResData();
        resData.setCode(0);
        resData.setData(bookIdList);
        System.out.println("返回结果："+resData);
        return resData;
    }

    //输入isbn码将书籍加入卖书列表
    @GetMapping("/isbn")
    public ResData getDateByISBN(@RequestParam("isbn")String isbn,@RequestParam("userId")Integer userId) throws ParseException {
        System.out.println("输入isbn码：isbn："+isbn+"，userId："+userId);
        //查询书籍是否存在于数据库
        Book book = bookMapper.queryBookByIBSN(isbn);
        if(book!=null){
            System.out.println("数据库查询");
            //查询用户卖书列表是否有当前这本书
            SellBook sellBook = sellBookMapper.getSellBookBySellBookIdAndUserId(book.getId(), userId);
            if(sellBook!=null){
                ResData resData = new ResData();
                resData.setCode(0);
                resData.setDesc("该用户卖书列表已有该书籍");
                System.out.println("返回结果："+resData);
                return resData;
            }else {
                //将该书籍加入到当前用户的卖书列表
                sellBookMapper.addSellBook(book.getId(), userId);
                ResData resData = new ResData();
                resData.setCode(0);
                resData.setDesc("成功");
                System.out.println("返回结果："+resData);
                return resData;
            }
        }else {
            System.out.println("api远程查询");
            String url="http://feedback.api.juhe.cn/ISBN?sub="+isbn+"&key=ad88fd34500be47f4dbac9eef2dc8373";
            ResponseEntity<String> results = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
            JSONObject object = JSON.parseObject(results.getBody());
            //如果查不到当前这本书
            if(!object .getString("error_code").equals("0")){
                ResData resData = new ResData();
                resData.setCode(200013);
                resData.setDesc(object .getString("reason"));
                System.out.println("返回结果："+resData);
                return resData;
            }
            System.out.println(object);
            Book newbook = new Book();
            if(null!=JSON.parseObject(object .getString("result")).getString("isbn10")){
                newbook.setISBN10(JSON.parseObject(object .getString("result")).getString("isbn10"));
            }
            if(null!=JSON.parseObject(object .getString("result")).getString("isbn13")){
                newbook.setISBN13(JSON.parseObject(object .getString("result")).getString("isbn13"));
            }
            newbook.setName(JSON.parseObject(object .getString("result")).getString("title"));
            newbook.setCover(JSON.parseObject(object .getString("result")).getString("images_large"));
            newbook.setAuthor(JSON.parseObject(object .getString("result")).getString("author"));
            newbook.setPrice(JSON.parseObject(object .getString("result")).getDouble("price"));
            newbook.setPublisher(JSON.parseObject(object .getString("result")).getString("publisher"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            newbook.setPubDate(sdf.parse(JSON.parseObject(object .getString("result")).getString("pubdate")));
            newbook.setDoubanRate(JSON.parseObject(object .getString("result")).getDouble("levelNum"));
            newbook.setGist(JSON.parseObject(object .getString("result")).getString("summary"));
            newbook.setBinding(JSON.parseObject(object .getString("result")).getString("binding"));
            //将书籍保存到本地数据库，下次不需要调用远程api查询该书籍数据
            bookMapper.addBook(newbook);
            //加入后查询这本书的id
            Book tempbook = bookMapper.queryBookByIBSN(isbn);
            //将书籍加入到当前用户卖书列表
            sellBookMapper.addSellBook(tempbook.getId(), userId);
            ResData resData = new ResData();
            resData.setCode(0);
            resData.setDesc("成功");
            System.out.println("返回结果："+resData);
            return resData;
        }
    }

    //删除当前用户单个卖书
    @DeleteMapping
    public ResData delSellBook(@RequestParam("bookId") Integer bookId,@RequestParam("userId")Integer userId){
        System.out.println("删除操作：bookId："+bookId+"，userId："+userId);
        sellBookMapper.delSellBook(bookId,userId);
        ResData resData = new ResData();
        resData.setCode(0);
        resData.setData("删除成功");
        System.out.println("返回结果："+resData);
        return resData;
    }

    //用户卖书列表集体加入订单或从订单中取消
    @PutMapping
    public ResData sellBookWhetherInOrder(@RequestParam("userId")Integer userId,@RequestParam("state")Integer state,@RequestParam("bookId")List<Integer> bookId){
        System.out.println("订单集体操作：userId："+userId+"，state："+state+"，List："+bookId);
        sellBookMapper.sellBookWhetherInOrder(userId,bookId,state);
        ResData resData = new ResData();
        resData.setCode(0);
        resData.setData("成功");
        return resData;
    }

}
