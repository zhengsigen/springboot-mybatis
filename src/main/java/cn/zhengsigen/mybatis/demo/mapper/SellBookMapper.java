package cn.zhengsigen.mybatis.demo.mapper;

import cn.zhengsigen.mybatis.demo.model.Book;
import cn.zhengsigen.mybatis.demo.model.SellBook;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SellBookMapper {
    //查询所有卖书列表
    public List<SellBook> getSellBookList();
    //根据用户id获取用户卖书列表
    public List<Book> getSellBookListByUserId(Integer userId);
    //查询用户指定卖书书籍
    public SellBook getSellBookBySellBookIdAndUserId(@Param("sellBookId")Integer sellBookId, @Param("userId")Integer userId);
    //新增一本卖书
    public void addSellBook(@Param("sellBookId")Integer sellBookId, @Param("userId")Integer userId);
    //删除一本卖书
    public void delSellBook(@Param("bookId")Integer bookId,@Param("userId")Integer userId);
    //用户卖书列表集体加入订单或从订单中取消
    public void sellBookWhetherInOrder(@Param("userId")Integer userId,@Param("bookId")List<Integer> bookId,@Param("state")Integer state);
}
