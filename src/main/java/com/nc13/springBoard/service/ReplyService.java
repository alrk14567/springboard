package com.nc13.springBoard.service;

import com.nc13.springBoard.model.ReplyDTO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplyService {
    private final String NAMESPACE="com.nc13.mappers.ReplyMapper";

    @Autowired
    private SqlSession session;

    public List<ReplyDTO> selectAll(int boardId) {
        return session.selectList(NAMESPACE + ".selectAll",boardId);
    }

    public ReplyDTO selectOne(int id) {
        return session.selectOne(NAMESPACE+".selectOne",id);
    }

    public void insert(ReplyDTO replyDTO){
        session.insert(NAMESPACE+".insert",replyDTO);
    }

    public void update(ReplyDTO replyDTO) {
        session.update(NAMESPACE+".update",replyDTO);
    }

    public void delete(int id) {
        session.delete(NAMESPACE + ".delete",id);
    }
}
