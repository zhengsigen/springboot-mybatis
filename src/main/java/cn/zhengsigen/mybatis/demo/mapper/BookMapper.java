package cn.zhengsigen.mybatis.demo.mapper;

import cn.zhengsigen.mybatis.demo.model.Admin;
import cn.zhengsigen.mybatis.demo.model.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BookMapper {
    //查询指定书
    public Book queryBookByIBSN(String isbn);
    //新增一本书
    public void addBook(Book book);
}
