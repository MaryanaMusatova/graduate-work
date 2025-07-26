package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.comment.CommentDTO;
import ru.skypro.homework.dto.comment.CreateOrUpdateComment;
import ru.skypro.homework.dto.comment.Comments;

import java.util.List;

public interface CommentService {
    List<CommentDTO> getCommentsForAd(Integer adId);
    CommentDTO addComment(Integer adId, CreateOrUpdateComment createComment, String username); // Изменили параметр на String
    CommentDTO editComment(Integer adId, Integer commentId, CreateOrUpdateComment updateComment, String username); // И здесь
    void deleteComment(Integer adId, Integer commentId, String username); // И здесь
}